package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> search(Location location){
        QLocation qLocation = QLocation.location;
        BooleanBuilder builder = new BooleanBuilder();

        if(location.getName() != null){
            builder.and(
                qLocation.name.containsIgnoreCase(location.getName())
            );
        }

        if(location.getStreet() != null){
            builder.and(
                qLocation.street.containsIgnoreCase(location.getStreet())
            );
        }

        if(location.getCity() != null){
            builder.and(
                qLocation.city.containsIgnoreCase(location.getCity())
            );
        }

        if(location.getCountry() != null){
            builder.and(
                qLocation.country.containsIgnoreCase(location.getCountry())
            );
        }

        if(location.getZipCode() != null){
            builder.and(
                qLocation.zipCode.containsIgnoreCase(location.getZipCode())
            );
        }

        //convert iterable to list
        Iterable<Location> result = locationRepository.findAll(builder.getValue());
        List<Location> locations = new LinkedList<>();
        result.forEach(locations::add);

        return locations;
    }
}
