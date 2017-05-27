package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
import org.springframework.stereotype.Service;


@Service
class PrincipalServiceImpl implements PrincipalService {

    private final PrincipalRepository principalRepository;

    public PrincipalServiceImpl(PrincipalRepository principalRepository) {
        this.principalRepository = principalRepository;
    }

    @Override
    public Principal findPrincipalByUsername(String username) {
        return principalRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }
}
