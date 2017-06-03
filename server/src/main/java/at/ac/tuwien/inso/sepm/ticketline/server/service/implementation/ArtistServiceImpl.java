package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService{
    @Override
    public List<Artist> search(String fuzzyArtistName){
        return null;
    }
}
