package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QArtist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public List<Artist> search(String fuzzyArtistName){
        QArtist artist = QArtist.artist;
        BooleanBuilder builder = new BooleanBuilder();

        String keywords[] = fuzzyArtistName.split(" ");
        for(String s : keywords){
            builder.and(
                artist.firstname.containsIgnoreCase(s).or(
                    artist.lastname.containsIgnoreCase(s)
                )
            );
        }

        //convert iterable to list
        Iterable<Artist> result = artistRepository.findAll(builder.getValue());
        List<Artist> artists = new LinkedList<>();
        result.forEach(artists::add);

        return artists;
    }
}
