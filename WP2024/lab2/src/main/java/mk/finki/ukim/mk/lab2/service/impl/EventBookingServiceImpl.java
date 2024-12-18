package mk.finki.ukim.mk.lab2.service.impl;

import mk.finki.ukim.mk.lab2.model.Event;
import mk.finki.ukim.mk.lab2.model.EventBooking;
import mk.finki.ukim.mk.lab2.repository.inMemoryRepo.InMemoryEventBookingRepository;
import mk.finki.ukim.mk.lab2.repository.prodRepo.EventBookingRepository;
import mk.finki.ukim.mk.lab2.repository.prodRepo.EventRepository;
import mk.finki.ukim.mk.lab2.service.EventBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventBookingServiceImpl implements EventBookingService{
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventBookingRepository eventBookingRepository;

    @Override
    public EventBooking placeBooking(Long id, String username, int numberOfTickets) {
        Event event = eventRepository.findById(id).orElseThrow(()->new RuntimeException("Event not found"));

        EventBooking eventBooking = new EventBooking(event.getName(), username, (long) numberOfTickets);

        return eventBookingRepository.save(eventBooking);
    }

    @Override
    public List<EventBooking> findByName(String name) {
        return eventBookingRepository.findAllByAttendeeName(name);
    }
}
