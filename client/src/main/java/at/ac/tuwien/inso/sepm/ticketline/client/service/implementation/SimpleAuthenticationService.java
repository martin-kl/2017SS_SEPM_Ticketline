package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.AuthenticationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SimpleAuthenticationService implements AuthenticationService {

    private final AuthenticationRestClient authenticationRestClient;
    private final AuthenticationInformationService authenticationInformationService;
    private final ThreadPoolTaskScheduler taskScheduler;

    private AuthenticationToken authenticationToken;
    private ScheduledFuture<?> schedule;

    public SimpleAuthenticationService(AuthenticationRestClient authenticationRestClient, AuthenticationInformationService authenticationInformationService) {
        this.authenticationInformationService = authenticationInformationService;
        this.authenticationRestClient = authenticationRestClient;
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.initialize();
    }

    @Override
    public AuthenticationTokenInfo authenticate(AuthenticationRequest authenticationRequest) throws DataAccessException {
        if (log.isTraceEnabled()) {
            log.trace("trying to authenticate {}", authenticationRequest);
        } else {
            log.debug("trying to authenticate");
        }
        return authenticateAndScheduleNextAuthentication(authenticationRequest);
    }

    private AuthenticationTokenInfo authenticateAndScheduleNextAuthentication() throws DataAccessException {
        return authenticateAndScheduleNextAuthentication(null);
    }

    private AuthenticationTokenInfo authenticateAndScheduleNextAuthentication(AuthenticationRequest authenticationRequest) throws DataAccessException {
        if (null == authenticationRequest) {
            authenticationToken = authenticationRestClient.authenticate();
        } else {
            authenticationToken = authenticationRestClient.authenticate(authenticationRequest);
        }
        log.debug("authentication result {}", authenticationToken);
        authenticationInformationService.setCurrentAuthenticationToken(authenticationToken.getCurrentToken());
        AuthenticationTokenInfo authenticationTokenInfo = authenticationRestClient.tokenInfoCurrent();
        scheduleReauthenticationTask(authenticationTokenInfo.getExpireAt().minus(authenticationTokenInfo.getOverlapDuration().dividedBy(2)));
        authenticationInformationService.setCurrentAuthenticationTokenInfo(authenticationTokenInfo);
        return authenticationTokenInfo;
    }

    private void scheduleReauthenticationTask(LocalDateTime runAt) {
        schedule = taskScheduler.schedule(
            () -> {
                log.debug("setting current token to future token");
                authenticationInformationService.setCurrentAuthenticationToken(authenticationToken.getFutureToken());
                log.debug("trying to re-authenticate {}", authenticationToken);
                try {
                    authenticateAndScheduleNextAuthentication();
                } catch (DataAccessException e) {
                    deAuthenticate();
                }
            },
            Date.from(runAt.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public void deAuthenticate() {
        log.debug("de authenticating");
        authenticationInformationService.clearAuthentication();
        authenticationToken = null;
        taskScheduler.shutdown();
        if (schedule != null) {
            schedule.cancel(true);
        }
    }

    @PreDestroy
    public void preDestroy() {
        deAuthenticate();
    }

}
