package mk.finki.ukim.mk.lab2.context;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.lab2.model.Event;
import mk.finki.ukim.mk.lab2.model.Location;
import mk.finki.ukim.mk.lab2.repository.prodRepo.EventRepository;
import mk.finki.ukim.mk.lab2.repository.prodRepo.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<Event> events = null;
    public static List<Location> locations = null;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostConstruct
    public void init() {
        events = new ArrayList<>();
        locations = new ArrayList<>();

        if(locationRepository.count() == 0){
            locations.add(new Location("Location1", "Address1", "Capacity1", "Description1"));
            locations.add(new Location("Location2", "Address2", "Capacity2", "Description2"));
            locations.add(new Location("Location3", "Address3", "Capacity3", "Description3"));
            locations.add(new Location("Location4", "Address4", "Capacity4", "Description4"));
            locations.add(new Location("Location5", "Address5", "Capacity5", "Description5"));
        } else {
            locations = locationRepository.findAll();
        }

        if(eventRepository.count() == 0){
            events.add(new Event("Event1", "Description1", 5.0, locations.get(0)));
            events.add(new Event("Event2", "Description2", 10.0, locations.get(1)));
            events.add(new Event("Event3", "Description3", 3.0, locations.get(2)));
            events.add(new Event("Event4", "Description4", 7.0, locations.get(3)));
            events.add(new Event("Event5", "Description5", 8.0, locations.get(4)));
        } else {
            events = eventRepository.findAll();
        }

    }
}
