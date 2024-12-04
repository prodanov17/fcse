package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.EventBooking;
import mk.finki.ukim.mk.lab.repository.inMemoryRepo.InMemoryEventBookingRepository;
import mk.finki.ukim.mk.lab.service.EventBookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventBookingServiceImpl implements EventBookingService{
    private final InMemoryEventBookingRepository inMemoryEventBookingRepository;

    public EventBookingServiceImpl(InMemoryEventBookingRepository inMemoryEventBookingRepository) {
        this.inMemoryEventBookingRepository = inMemoryEventBookingRepository;
    }


    @Override
    public EventBooking placeBooking(String eventName, String attendeeName, String attendeeAddress, int numberOfTickets) {
        EventBooking eventBooking = new EventBooking(eventName, attendeeName, attendeeAddress, (long) numberOfTickets);
        this.inMemoryEventBookingRepository.placeBooking(eventBooking);
        return eventBooking;
    }

    @Override
    public List<EventBooking> findByName(String name) {
        return this.inMemoryEventBookingRepository.findAll().stream().filter(e->e.getAttendeeName().equals(name)).toList();
    }
}
