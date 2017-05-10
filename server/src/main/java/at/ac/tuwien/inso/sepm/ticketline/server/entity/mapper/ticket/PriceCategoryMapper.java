package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.PriceCategoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.PriceCategory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PriceCategoryMapper {
    PriceCategory fromDTO(PriceCategoryDTO priceCategoryDTO);
    PriceCategoryDTO fromEntity(PriceCategory priceCategory);
}
