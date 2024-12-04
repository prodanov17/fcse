package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();
    void save(String name, String address, String capacity, String description);
}
