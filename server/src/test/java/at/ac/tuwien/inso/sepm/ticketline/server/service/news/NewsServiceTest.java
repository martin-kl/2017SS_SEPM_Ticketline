package at.ac.tuwien.inso.sepm.ticketline.server.service.news;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalNewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsServiceTest {
    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PrincipalNewsRepository principalNewsRepository;

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private Principal admin;
    private Principal user;

    private static Pageable pageable = new PageRequest(0, 10000);

    @Before
    public void setUpCustomer() {
        admin = Principal.builder()
            .role(Principal.Role.ADMIN)
            .username("admin")
            .password(passwordEncoder.encode("password"))
            .enabled(true)
            .build();

        user = Principal.builder()
            .role(Principal.Role.SELLER)
            .username("user")
            .password(passwordEncoder.encode("password"))
            .enabled(true)
            .build();

        admin = principalRepository.save(admin);
        user = principalRepository.save(user);
    }

    private News getUnsavedNews() {
        News unsavedNews = new News();
        unsavedNews.setText("Main");
        unsavedNews.setSummary("Short");
        unsavedNews.setTitle("Title");
        return unsavedNews;
    }


    @After
    public void tearDown() {
        newsRepository.deleteAll();
        principalNewsRepository.deleteAll();
    }

    @Test
    public void canPublishNews() {
        News news = getUnsavedNews();
        assertFalse(newsService.findAll(pageable).contains(news));
        News saved = newsService.publishNews(news);
        assertTrue(newsService.findAll(pageable).contains(saved));
    }

    // TODO: fix test !!!
    /*
    @Test
    public void canReportAsSeenAndCanFindAllNotSeen() {
        News news1 = getUnsavedNews();
        News news2 = getUnsavedNews();
        News saved1 = newsService.publishNews(news1);
        News saved2 = newsService.publishNews(news2);
        List<News> unread = newsService.findAllNotSeenByUser(user.getId(),pageable);
        assertTrue(unread.contains(saved1));
        assertTrue(unread.contains(saved2));
        News news = newsService.reportSeen(saved1.getId(), user);
        unread = newsService.findAllNotSeenByUser(user.getId(), pageable);
        assertFalse(unread.contains(saved1));
        assertFalse(unread.contains(news));
        assertTrue(unread.contains(saved2));
    }
    */

    @Test
    public void canPageFindUnseen() {
        News news1 = getUnsavedNews();
        News news2 = getUnsavedNews();
        News saved1 = newsService.publishNews(news1);
        News saved2 = newsService.publishNews(news2);
        Pageable page = new PageRequest(0, 1);
        List<News> unread = newsService.findAllNotSeenByUser(user.getId(), page);
        assertTrue(unread.size() == 1);
        assertTrue(unread.contains(saved2));
        page = new PageRequest(1, 1);
        unread = newsService.findAllNotSeenByUser(user.getId(), page);
        assertTrue(unread.size() == 1);
        assertTrue(unread.contains(saved1));
        page = new PageRequest(0, 2);
        unread = newsService.findAllNotSeenByUser(user.getId(), page);
        assertTrue(unread.size() == 2);
        assertTrue(unread.contains(saved1));
        assertTrue(unread.contains(saved2));
    }

    @Test
    public void canPageFindAll() {
        News news1 = getUnsavedNews();
        News news2 = getUnsavedNews();
        News saved1 = newsService.publishNews(news1);
        News saved2 = newsService.publishNews(news2);
        Pageable page = new PageRequest(0, 1);
        List<News> unread = newsService.findAll(page);
        assertTrue(unread.size() == 1);
        assertTrue(unread.contains(saved2));
        page = new PageRequest(1, 1);
        unread = newsService.findAll(page);
        assertTrue(unread.size() == 1);
        assertTrue(unread.contains(saved1));
        page = new PageRequest(0, 2);
        unread = newsService.findAll(page);
        assertTrue(unread.size() == 2);
        assertTrue(unread.contains(saved1));
        assertTrue(unread.contains(saved2));
    }
}
