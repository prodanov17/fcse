package mk.finki.ukim.mk.lab2.service.impl;

import mk.finki.ukim.mk.lab2.model.Location;
import mk.finki.ukim.mk.lab2.repository.prodRepo.LocationRepository;
import mk.finki.ukim.mk.lab2.service.LocationService;
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
        Location location = new Location();
        location.setName(name);
        location.setAddress(address);
        location.setCapacity(capacity);
        location.setDescription(description);

        this.locationRepository.save(location);
    }
}
