package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries")
    public List<SimpleNewsDTO> findAll() {
        return newsMapper.newsToSimpleNewsDTO(newsService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry")
    public DetailedNewsDTO find(@PathVariable UUID id) {
        return newsMapper.newsToDetailedNewsDTO(newsService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Publish a new news entry")
    public DetailedNewsDTO publishNews(@RequestBody DetailedNewsDTO detailedNewsDTO) {
        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        news = newsService.publishNews(news);
        return newsMapper.newsToDetailedNewsDTO(news);
    }

}
