package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.repository.inMemoryRepo.InMemoryLocationRepository;
import mk.finki.ukim.mk.lab.repository.prodRepo.LocationRepository;
import mk.finki.ukim.mk.lab.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public void save(String name, String address, String capacity, String description) {
        Location location = Location.builder()
                .name(name)
                .address(address)
                .capacity(capacity)
                .description(description)
                .build();

        this.locationRepository.save(location);
    }
}
