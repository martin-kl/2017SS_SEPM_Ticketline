package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/artist")
@Api(value = "artist")
public class ArtistEndpoint {

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Search for artist, which match (fuzzy) the given string")
    public List<ArtistDTO> search(@RequestParam(value = "fuzzyArtistName", required = false) String fuzzyArtistName){
        //TODO
        return null;
    }
}
