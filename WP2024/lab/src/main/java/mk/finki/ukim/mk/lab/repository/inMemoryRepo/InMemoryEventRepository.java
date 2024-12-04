package mk.finki.ukim.mk.lab.repository.inMemoryRepo;

import mk.finki.ukim.mk.lab.model.Event;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryEventRepository {
    public List<Event> findAll(){
        return null;
    }

    public List<Event> searchEvents(String text) {
        return null;
    }

    public Optional<Event> findById(Long id){
        return Optional.empty();
    }

    public void update(Event event) {
        return;
    }

    public void save(Event event){
        return;
    }
    public void remove(Long id){
        return;
    }

}
