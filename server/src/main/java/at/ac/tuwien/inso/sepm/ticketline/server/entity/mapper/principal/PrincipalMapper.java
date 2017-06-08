package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.principal;

import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrincipalMapper {
    Principal fromDTO(PrincipalDTO principalDTO);

    PrincipalDTO fromEntity(Principal principal);

    List<PrincipalDTO> fromEntity(List<Principal> all);
}
