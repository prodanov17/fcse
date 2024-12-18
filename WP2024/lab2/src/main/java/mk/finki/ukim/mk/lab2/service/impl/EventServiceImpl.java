package mk.finki.ukim.mk.lab2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.finki.ukim.mk.lab2.model.Event;
import mk.finki.ukim.mk.lab2.model.Location;
import mk.finki.ukim.mk.lab2.repository.prodRepo.EventRepository;
import mk.finki.ukim.mk.lab2.repository.prodRepo.LocationRepository;
import mk.finki.ukim.mk.lab2.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository repository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Event> listAll() {
        return repository.findAll();
    }

    @Override
    public List<Event> searchEvents(String text) {
        return repository.findAllByNameLike(text);
    }

    @Override
    public void remove(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<Event> findAllByLocationId(Long id) {
        return this.repository.findAllByLocationId(id);
    }

    @Override
    public void update(Long id, String name, String description, Double popularityScore, Long locationId) throws Exception {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        // Fetch the existing event by ID
        Event event = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        // Update the event's fields
        event.setName(name);
        event.setDescription(description);
        event.setPopularityScore(popularityScore);
        event.setLocation(location);

        // Save the updated event
        repository.save(event);
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
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setPopularityScore(popularityScore);
        event.setLocation(location.get());

        this.repository.save(event);
    }
}
