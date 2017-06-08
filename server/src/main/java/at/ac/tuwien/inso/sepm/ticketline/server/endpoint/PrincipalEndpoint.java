package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.principal.PrincipalMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    @ApiOperation(value = "Get list of all principals")
    public List<PrincipalDTO> findAll(Pageable pageable) {
        return principalMapper.fromEntity(principalService.findAll(pageable));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Get searched principals")
    public List<PrincipalDTO> search(@RequestParam(value = "query") String query, Boolean locked,
        Pageable pageable) {
        return principalMapper.fromEntity(principalService.search(query, locked, pageable));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Save a principal")
    public PrincipalDTO savePrincipal(@RequestBody PrincipalDTO principalDTO) {
        Principal principal = principalMapper.fromDTO(principalDTO);
        principal = principalService.save(principal);
        return principalMapper.fromEntity(principal);
    }

    @RequestMapping(value = "setEnabled", method = RequestMethod.GET)
    @ApiOperation(value = " a principal")
    public PrincipalDTO setEnabledForPrincipalWithId(@RequestParam(value = "id") UUID id,
        @RequestParam(value = "enabled") boolean enabled) {
        Principal principal = principalService.setEnabledForPrincipalWithId(id, enabled);
        return principalMapper.fromEntity(principal);
    }
}
