package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.principal;

import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(PrincipalRoleMapper.class)
public interface PrincipalMapper {
    Principal fromDTO(PrincipalDTO principalDTO);

    PrincipalDTO fromEntity(Principal principal);
}
