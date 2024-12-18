package mk.finki.ukim.mk.lab2.service;

import mk.finki.ukim.mk.lab2.model.EventBooking;

import java.util.List;

public interface EventBookingService {
    EventBooking placeBooking(Long id, String username, int numberOfTickets);
    List<EventBooking> findByName(String name);
}
