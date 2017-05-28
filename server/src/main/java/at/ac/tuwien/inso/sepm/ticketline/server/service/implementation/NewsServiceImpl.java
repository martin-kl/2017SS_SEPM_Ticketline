package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.PrincipalNews;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalNewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final PrincipalNewsRepository principalNewsRepository;

    public NewsServiceImpl(NewsRepository newsRepository, PrincipalNewsRepository principalNewsRepository) {
        this.newsRepository = newsRepository;
        this.principalNewsRepository = principalNewsRepository;
    }

    @Override
    public List<News> findAll(Pageable page) {
        return newsRepository.findAllByOrderByPublishedAtDesc(page);
    }

    @Override
    public News findOne(UUID id) {
        return newsRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public News publishNews(News news) {
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Override
    public News reportSeen(UUID newsId, Principal principal) {
        News news = newsRepository.findOneById(newsId).orElseThrow(NotFoundException::new);
        PrincipalNews principalNews = principalNewsRepository.findByNewsIdAndPrincipal(news.getId(), principal);
        if (principalNews == null) {
            principalNews = new PrincipalNews();
            principalNews.setPrincipal(principal);
            principalNews.setNews(news);
        }
        principalNews.setSeen(true);
        principalNewsRepository.save(principalNews);
        return newsRepository.findOneById(news.getId()).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<News> findAllNotSeenByUser(UUID userId, Pageable page) {
        return newsRepository.findAllNotSeenByUserWithId(userId, page);
    }

}
