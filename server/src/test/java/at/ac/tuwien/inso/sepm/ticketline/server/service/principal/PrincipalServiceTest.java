package at.ac.tuwien.inso.sepm.ticketline.server.service.principal;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;

import java.util.List;

import at.ac.tuwien.inso.sepm.ticketline.server.utils.SecurityContextHolderMocker;
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

    private final String USERNAME1 = "testuser1";
    private final Principal.Role TESTROLE1 = Role.ADMIN;
    private final String USERONEMAIL = "testuser@test.at";
    private Principal testUserOne;

    private final String USERNAME2 = "testuser_two";
    private final Principal.Role TESTROLE2 = Role.SELLER;
    private final String USERTWOMAIL = "test_second_mail@test.com";
    private Principal testUserTwo;

    @Before
    public void setUp() {
        principalRepository.deleteAll();
        testUserOne = Principal.builder()
            .role(TESTROLE1)
            .username(USERNAME1)
            .enabled(true)
            .email(USERONEMAIL)
            .build();
        testUserOne = principalService.save(testUserOne, "password");

        testUserTwo = Principal.builder()
            .role(TESTROLE2)
            .username(USERNAME2)
            .enabled(true)
            .email(USERTWOMAIL)
            .build();
        testUserTwo = principalService.save(testUserTwo, "passwordTwo");
    }

    @Test
    public void canSaveAndUpdateNewSeller() {
        List<Principal> principalList = principalRepository.findAll();
        Assert.assertTrue(principalList.contains(testUserOne));

        testUserOne.setEmail("new_mail@test.at");
        testUserOne = principalService.save(testUserOne, "");

        principalList.clear();
        principalList = principalRepository.findAll();
        Assert.assertTrue(principalList.contains(testUserOne));

        Assert.assertTrue(principalList.contains(testUserOne));
    }

    @Test
    public void canSetEnabledForPrincipal() {
        SecurityContextHolderMocker.setSecurityContextToUsername(testUserOne.getUsername());
        Principal result = principalRepository.findOne(testUserTwo.getId());
        Assert.assertTrue(result.isEnabled());
        result = principalService.setEnabledForPrincipalWithId(testUserTwo.getId(), true);
        Assert.assertFalse(result.isEnabled());
        Assert.assertTrue(result.getFailedLoginCount() == 0);
    }

    @Test
    public void canFindPrincipleByUsername() {
        Principal result = principalService.findPrincipalByUsername(USERNAME1);
        Assert.assertTrue(result.getUsername().equals(USERNAME1));
    }

    @Test
    public void canFindAll() {
        List<Principal> result = principalService.findAll(pageable);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue((
            (result.get(0).getUsername().equals(USERNAME1)) ||
                (result.get(0).getUsername().equals(USERNAME2))) &&
            ((result.get(0).getUsername().equals(USERNAME1)) ||
                (result.get(0).getUsername().equals(USERNAME2))));
    }

    @Test
    public void canFuzzySearchForUser() {
        List<Principal> result = principalService.search("stus", null, pageable);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue((
            (result.get(0).getUsername().equals(USERNAME1)) ||
                (result.get(0).getUsername().equals(USERNAME2))) &&
            ((result.get(0).getUsername().equals(USERNAME1)) ||
                (result.get(0).getUsername().equals(USERNAME2))));

        String newUserName = "administrator";
        testUserOne.setUsername(newUserName);
        testUserOne = principalService.save(testUserOne, "password_to_test");
        List<Principal> result2 = principalService.search("trat", null, pageable);
        Assert.assertTrue(result2 != null);
        Assert.assertTrue(result2.size() == 1);
        Assert.assertTrue(result2.get(0).getUsername().equals(newUserName));
    }

    @Test
    public void canFuzzySearchForUserWithStatus() {
        List<Principal> result = principalService.search("stus", false, pageable);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue(result.contains(testUserOne));
        Assert.assertTrue(result.contains(testUserTwo));
    }

}
