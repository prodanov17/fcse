package mk.finki.ukim.mk.lab.repository.inMemoryRepo;

import mk.finki.ukim.mk.lab.model.EventBooking;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryEventBookingRepository {
    public void placeBooking(EventBooking booking){

    }

    public List<EventBooking> findAll(){
        return null;
    }
}
