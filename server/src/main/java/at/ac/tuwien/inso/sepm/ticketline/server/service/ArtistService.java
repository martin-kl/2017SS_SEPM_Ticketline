package at.ac.tuwien.inso.sepm.ticketline.server.service;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;

import java.util.List;

public interface ArtistService {
    /**
     * Find artists which fuzzy-match the searched string
     *
     * @param fuzzyArtistName the fuzzy string which will be searched for
     * @return list of artists which match the string
     * */
    List<Artist> search(String fuzzyArtistName);
}
