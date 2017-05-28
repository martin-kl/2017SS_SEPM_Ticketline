package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
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
    public DetailedNewsDTO publish(DetailedNewsDTO news) throws DataAccessException {
        return newsRestClient.publish(news);
    }

    public DetailedNewsDTO findDetailedNews(UUID id) throws DataAccessException {
        return newsRestClient.findDetailedNews(id);
    }
}
