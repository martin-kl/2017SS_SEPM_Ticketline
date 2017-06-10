package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.principal.PrincipalMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/principal")
@Api(value = "principal")
public class PrincipalEndpoint {

    @Autowired
    private PrincipalService principalService;
    @Autowired
    private PrincipalMapper principalMapper;


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Get list of all principals")
    public List<PrincipalDTO> findAll(Pageable pageable) {
        return principalService.findAll(pageable)
            .stream()
            .map(principalMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Get searched principals")
    public List<PrincipalDTO> search(@RequestParam(value = "query") String query, Boolean locked,
        Pageable pageable) {
        return principalService.search(query, locked, pageable)
            .stream()
            .map(principalMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Save a principal")
    public PrincipalDTO savePrincipal(@RequestBody PrincipalDTO principalDTO) {
        Principal principal = principalMapper.fromDTO(principalDTO);
        principal = principalService.save(principal, principalDTO.getNewPassword());
        return principalMapper.fromEntity(principal);
    }

    @RequestMapping(value = "setEnabled", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = " a principal")
    public PrincipalDTO setEnabledForPrincipalWithId(@RequestParam(value = "id") UUID id,
        @RequestParam(value = "enabled") boolean enabled) {
        Principal principal = principalService.setEnabledForPrincipalWithId(id, enabled);
        return principalMapper.fromEntity(principal);
    }
}
