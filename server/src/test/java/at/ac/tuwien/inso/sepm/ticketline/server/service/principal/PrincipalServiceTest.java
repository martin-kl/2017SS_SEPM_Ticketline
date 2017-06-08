package at.ac.tuwien.inso.sepm.ticketline.server.service.principal;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PrincipalServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PrincipalRepository principalRepository;
    @Autowired
    private PrincipalService principalService;

    private static Pageable pageable = new PageRequest(0, 100);

    private final String username = "testuser";
    private final Principal.Role testRole = Role.ADMIN;
    private Principal testUser;

    @Before
    public void setUp() {
        principalRepository.deleteAll();
         testUser = Principal.builder()
            .role(testRole)
            .username(username)
            .password(passwordEncoder.encode("password"))
            .enabled(true)
            .build();
        testUser = principalService.save(testUser);
    }

    @Test
    public void canSaveAndUpdateNewSeller() {
        List<Principal> principalList = principalRepository.findAll();
        Assert.assertTrue(principalList.contains(testUser));

        testUser.setPassword("new_password");
        testUser = principalService.save(testUser);

        principalList.clear();
        principalList = principalRepository.findAll();
        Assert.assertTrue(principalList.contains(testUser));
    }

    @Test
    public void canFindPrincipleByUsername() {
        Principal result = principalService.findPrincipalByUsername(username);
        Assert.assertTrue(result.getUsername().equals(username));
    }

    @Test
    public void canFuzzySearchForUser() {
        List<Principal> result = principalService.search("test", pageable);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get(0).getUsername().equals(username));

        String newUserName = "administrator";
        testUser.setUsername(newUserName);
        testUser = principalService.save(testUser);
        List<Principal> result2 = principalService.search("trat", pageable);
        Assert.assertTrue(result2 != null);
        Assert.assertTrue(result2.size() == 1);
        Assert.assertTrue(result2.get(0).getUsername().equals(newUserName));
    }

    /*
    //reset Password will be done on the client side so we can`t test it here
    @Test
    public void canResetPassword() {
        principalRepository.deleteAll();
        principalRepository.save(testUser);
        //start resetting the password
        Principal newPrincipal = principalService.resetPassword(testUser.getId());
        //System.out.println("\n\n\t\tafter resetting the passwor the username is \""+newPrincipal.getUsername()+"\" the password is \""+newPrincipal.getPassword()+"\"");
        simpleHeaderTokenAuthenticationService.authenticate(newPrincipal.getUsername(), newPrincipal.getPassword());
    }
    */
}
