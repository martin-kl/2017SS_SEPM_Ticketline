package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.AuthenticationConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AccountLockedException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SimpleHeaderTokenAuthenticationService implements HeaderTokenAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final SecretKeySpec signingKey;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Duration validityDuration;
    private final Duration overlapDuration;

    @Autowired
    PrincipalRepository principalRepository;
    @Autowired
    PrincipalService principalService;

    public SimpleHeaderTokenAuthenticationService(
        @Lazy AuthenticationManager authenticationManager,
        AuthenticationConfigurationProperties authenticationConfigurationProperties,
        ObjectMapper objectMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        byte[] apiKeySecretBytes = DatatypeConverter
            .parseBase64Binary(authenticationConfigurationProperties.getSecret());
        signatureAlgorithm = authenticationConfigurationProperties.getSignatureAlgorithm();
        signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        validityDuration = authenticationConfigurationProperties.getValidityDuration();
        overlapDuration = authenticationConfigurationProperties.getOverlapDuration();
    }

    @Override
    public AuthenticationToken authenticate(String username, CharSequence password) {
        //this is authentication from spring
        Principal principal;
        try {
            principal = principalService.findPrincipalByUsername(username);
        } catch (NotFoundException f) {
            log.error(
                "Tried to get user by the username but user does not exist -> throw a BadCredentialException");
            //now we know, that there is no such user
            //so we just throw the caught exception for BadCredentials
            throw new BadCredentialsException(f.getMessage());
        }

        //here we know, that the user exists and we can continue

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

            //here the login worked, reset the failedLoginCount if it was != 0
            if(principal.getFailedLoginCount() != 0) {
                principalRepository.resetFailedLoginCount(principal.getId());
            }

            Instant now = Instant.now();
            String authorities = "";
            try {
                authorities = objectMapper.writeValueAsString(authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            } catch (JsonProcessingException e) {
                log.error("Failed to wrap authorities", e);
            }
            String currentToken = Jwts.builder()
                .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID, null)
                .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL, authentication.getName())
                .claim(AuthenticationConstants.JWT_CLAIM_AUTHORITY, authorities)
                .setIssuedAt(Date.from(now))
                .setNotBefore(Date.from(now))
                .setExpiration(Date.from(now.plus(validityDuration)))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
            String futureToken = Jwts.builder()
                .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID, null)
                .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL, authentication.getName())
                .claim(AuthenticationConstants.JWT_CLAIM_AUTHORITY, authorities)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now
                    .plus(validityDuration
                        .minus(overlapDuration)
                        .plus(validityDuration))))
                .setNotBefore(Date.from(now
                    .plus(validityDuration
                        .minus(overlapDuration))))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
            return AuthenticationToken.builder()
                .currentToken(currentToken)
                .futureToken(futureToken)
                .build();
        } catch (AuthenticationException e) {
            /*
            //we get in here if the credentials were wrong
            //and here we add our login counter if the login was not successful
            //at first we have to check if there is even a user with the username:

            Principal principal;
            try {
                principal = principalService.findPrincipalByUsername(username);
            } catch (NotFoundException f) {
                log.error(
                    "tAuthenticationException caught, then tried to get user but user does not exist -> throw a BadCredentialException");
                //now we know, that there is no such user
                //so we just throw the caught exception for BadCredentials
                throw new BadCredentialsException(e.getMessage());
            }
            */
            log.error(
                "AuthenticationException caught for an existing user"
                    + " -> increment FailedLoginCounter and throw a BadCredentialException");
            //now we have a user with the name, increment it`s failed login count
            if(principal.isEnabled() && principal.getFailedLoginCount() < 4) {
                principalRepository.incrementFailedLoginCount(principal.getId(), true);
                throw new BadCredentialsException(e.getMessage());
            }else {
                System.out.println("\n\n\tin here - user has more than 5 failed login attempts\n\n");
                principalRepository.incrementFailedLoginCount(principal.getId(), false);
                //throw exception to the client to show him that
                throw new AccountLockedException("User is locked because he tried the wrong password for 5 times");
            }
        }
    }

    @Override
    public AuthenticationTokenInfo authenticationTokenInfo(String headerToken) {
        final Claims claims = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(headerToken)
            .getBody();
        List<String> roles = readJwtAuthorityClaims(claims);
        return AuthenticationTokenInfo.builder()
            .username((String) claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL))
            .roles(roles)
            .issuedAt(
                LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()))
            .notBefore(
                LocalDateTime.ofInstant(claims.getNotBefore().toInstant(), ZoneId.systemDefault()))
            .expireAt(
                LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()))
            .validityDuration(validityDuration)
            .overlapDuration(overlapDuration)
            .build();
    }

    @Override
    public AuthenticationToken renewAuthentication(String headerToken) {
        final Claims claims = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(headerToken)
            .getBody();
        String futureToken = Jwts.builder()
            .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID,
                claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL_ID))
            .claim(AuthenticationConstants.JWT_CLAIM_PRINCIPAL,
                claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL))
            .claim(AuthenticationConstants.JWT_CLAIM_AUTHORITY,
                claims.get(AuthenticationConstants.JWT_CLAIM_AUTHORITY))
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(claims.getExpiration().toInstant()
                .plus(validityDuration
                    .minus(overlapDuration))))
            .setNotBefore(Date.from(claims.getExpiration().toInstant().minus(overlapDuration)))
            .signWith(signatureAlgorithm, signingKey)
            .compact();
        return AuthenticationToken.builder()
            .currentToken(headerToken)
            .futureToken(futureToken)
            .build();
    }

    @Override
    public User authenticate(String headerToken) {
        try {
            final Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(headerToken)
                .getBody();
            List<String> authoritiesWrapper = readJwtAuthorityClaims(claims);
            List<SimpleGrantedAuthority> authorities = authoritiesWrapper.stream()
                .map(roleName -> roleName.startsWith(AuthenticationConstants.ROLE_PREFIX) ?
                    roleName : (AuthenticationConstants.ROLE_PREFIX + roleName))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            return new User(
                (String) claims.get(AuthenticationConstants.JWT_CLAIM_PRINCIPAL),
                headerToken,
                authorities);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException(e.getMessage(), e);
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    private List<String> readJwtAuthorityClaims(Claims claims) {
        ArrayList<String> authoritiesWrapper = new ArrayList<>();
        try {
            authoritiesWrapper = objectMapper.readValue(claims.get(
                AuthenticationConstants.JWT_CLAIM_AUTHORITY, String.class),
                new TypeReference<List<String>>() {
                });
        } catch (IOException e) {
            log.error("Failed to unwrap roles", e);
        }
        return authoritiesWrapper;
    }
}