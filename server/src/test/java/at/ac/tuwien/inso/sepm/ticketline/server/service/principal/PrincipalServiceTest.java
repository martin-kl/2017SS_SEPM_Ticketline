package at.ac.tuwien.inso.sepm.ticketline.server.service.principal;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import java.util.List;
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

    private final String username1 = "testuser1";
    private final Principal.Role testRole1 = Role.ADMIN;
    private Principal testUserOne;

    private final String username2 = "testuser_two";
    private final Principal.Role testRole2 = Role.SELLER;
    private Principal testUserTwo;

    @Before
    public void setUp() {
        principalRepository.deleteAll();
        testUserOne = Principal.builder()
            .role(testRole1)
            .username(username1)
            .password(passwordEncoder.encode("password"))
            .enabled(true)
            .build();
        testUserOne = principalService.save(testUserOne);

        testUserTwo = Principal.builder()
            .role(testRole2)
            .username(username2)
            .password("passwordTwo")
            .enabled(false)
            .build();
        testUserTwo = principalService.save(testUserTwo);
    }

    @Test
    public void canSaveAndUpdateNewSeller() {
        List<Principal> principalList = principalRepository.findAll();
        Assert.assertTrue(principalList.contains(testUserOne));

        testUserOne.setPassword("new_password");
        testUserOne = principalService.save(testUserOne);

        principalList.clear();
        principalList = principalRepository.findAll();
        Assert.assertTrue(principalList.contains(testUserOne));
    }

    @Test
    public void canSetEnabledForPrincipal() {
        Principal result = principalRepository.findOne(testUserTwo.getId());
        Assert.assertTrue(! result.isEnabled());
        result = principalService.setEnabledForPrincipalWithId(testUserTwo.getId(), true);
        Assert.assertTrue(result.isEnabled());
        Assert.assertTrue(result.getFailedLoginCount() == 0);
    }

    @Test
    public void canFindPrincipleByUsername() {
        Principal result = principalService.findPrincipalByUsername(username1);
        Assert.assertTrue(result.getUsername().equals(username1));
    }

    @Test
    public void canFindAll() {
        List<Principal> result = principalService.findAll(pageable);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue((
            (result.get(0).getUsername().equals(username1)) ||
                (result.get(0).getUsername().equals(username2))) &&
            ((result.get(0).getUsername().equals(username1)) ||
                (result.get(0).getUsername().equals(username2))));
    }

    @Test
    public void canFuzzySearchForUser() {
        List<Principal> result = principalService.search("stus", pageable);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue((
            (result.get(0).getUsername().equals(username1)) ||
                (result.get(0).getUsername().equals(username2))) &&
            ((result.get(0).getUsername().equals(username1)) ||
                (result.get(0).getUsername().equals(username2))));

        String newUserName = "administrator";
        testUserOne.setUsername(newUserName);
        testUserOne = principalService.save(testUserOne);
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
        principalRepository.save(testUserOne);
        //start resetting the password
        Principal newPrincipal = principalService.resetPassword(testUserOne.getId());
        //System.out.println("\n\n\t\tafter resetting the passwor the username is \""+newPrincipal.getUsername()+"\" the password is \""+newPrincipal.getPassword()+"\"");
        simpleHeaderTokenAuthenticationService.authenticate(newPrincipal.getUsername(), newPrincipal.getPassword());
    }
    */
}
