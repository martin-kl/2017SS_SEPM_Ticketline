package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/artist")
@Api(value = "artist")
public class ArtistEndpoint {
    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistMapper artistMapper;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Search for artist, which match (fuzzy) the given string")
    public List<ArtistDTO> search(@RequestParam(value = "fuzzyArtistName", required = false) String fuzzyArtistName){

        return artistService
            .search(fuzzyArtistName)
            .stream()
            .map(artistMapper::fromEntity)
            .collect(Collectors.toList());
    }
}
