package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SimpleNewsService implements NewsService {

    private final NewsRestClient newsRestClient;

    public SimpleNewsService(NewsRestClient newsRestClient) {
        this.newsRestClient = newsRestClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll(int page) throws DataAccessException {
        return newsRestClient.findAll(page);
    }

    @Override
    public List<SimpleNewsDTO> findAllUnseen(int page) throws DataAccessException {
        return newsRestClient.findAllUnseen(page);
    }

    @Override
    public DetailedNewsDTO publish(DetailedNewsDTO news) throws DataAccessException, ValidationException {
        if (news.getText().length() == 0) {
            throw new ValidationException("news.no.text");
        }
        if (news.getText().length() > 10_000) {
            throw new ValidationException("news.long.text");
        }
        if (news.getSummary().length() == 0) {
            throw new ValidationException("news.no.summary");
        }
        if (news.getSummary().length() > 10_000) {
            throw new ValidationException("news.long.summary");
        }
        if (news.getTitle().length() == 0) {
            throw new ValidationException("news.no.title");
        }
        if (news.getTitle().length() > 100) {
            throw new ValidationException("news.long.title");
        }
        if (news.getImage() != null && news.getImage().length > 5_000_000) {
            throw new ValidationException("news.large.image");
        }
        return newsRestClient.publish(news);
    }

    public DetailedNewsDTO findDetailedNews(UUID id) throws DataAccessException {
        return newsRestClient.findDetailedNews(id);
    }
}
