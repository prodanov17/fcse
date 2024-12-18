package mk.finki.ukim.mk.lab2.repository.prodRepo;

import mk.finki.ukim.mk.lab2.model.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventBookingRepository extends JpaRepository<EventBooking, Long> {
    List<EventBooking> findAllByAttendeeName(String name);
}
