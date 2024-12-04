package mk.finki.ukim.mk.lab.repository.prodRepo;

import mk.finki.ukim.mk.lab.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByLocationId(Long id);
    List<Event> findAllByNameLike(String name);
}
