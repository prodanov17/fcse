package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.repository.EventRepository;
import mk.finki.ukim.mk.lab.repository.LocationRepository;
import mk.finki.ukim.mk.lab.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final LocationRepository locationRepository;

    @Autowired
    public EventServiceImpl(EventRepository repository, LocationRepository locationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Event> listAll() {
        return repository.findAll();
    }

    @Override
    public List<Event> searchEvents(String text) {
        return repository.searchEvents(text);
    }

    @Override
    public void remove(Long id) {
        this.repository.remove(id);
    }

    @Override
    public void update(Long id, String name, String description, Double popularityScore, Long locationId) throws Exception {
        Optional<Location> location = this.locationRepository.findById(locationId);
        if(location.isEmpty()){
            throw new Exception("Location not found");
        }
        Event event = new Event(id, name, description, popularityScore, location.get());

        this.repository.update(event);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void save(String name, String description, Double popularityScore, Long locationId) throws Exception {
        Optional<Location> location = this.locationRepository.findById(locationId);
        if(location.isEmpty()){
            throw new Exception("Location not found");
        }
        Event event = new Event(name, description, popularityScore, location.get());

        this.repository.save(event);
    }
}
