package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NewsMapper {

    News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    DetailedNewsDTO newsToDetailedNewsDTO(News one);

    List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    SimpleNewsDTO newsToSimpleNewsDTO(News one);

}