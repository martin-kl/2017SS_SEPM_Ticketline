package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.principal;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PrincipalRole;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PrincipalRoleMapper implements PrincipalMapper {

    @Autowired
    private PrincipalMapper principalMapper;

    @Override
    public Principal fromDTO(PrincipalDTO principalDTO) {
        Principal principal = principalMapper.fromDTO(principalDTO);

        if (principalDTO.getPrincipalRole().equals(PrincipalRole.ADMIN)) {
            principal.setRole(Role.ADMIN);
        } else {
            principal.setRole(Role.SELLER);
        }
        return principal;
    }

    @Override
    public PrincipalDTO fromEntity(Principal principal) {
        PrincipalDTO principalDTO = principalMapper.fromEntity(principal);

        if (principal.getRole().equals(Role.ADMIN)) {
            principalDTO.setPrincipalRole(PrincipalRole.ADMIN);
        } else {
            principalDTO.setPrincipalRole(PrincipalRole.SELLER);
        }
        return principalDTO;
    }
}
