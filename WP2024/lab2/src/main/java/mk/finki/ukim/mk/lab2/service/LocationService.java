package mk.finki.ukim.mk.lab2.service;

import mk.finki.ukim.mk.lab2.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();
    void save(String name, String address, String capacity, String description);
}
