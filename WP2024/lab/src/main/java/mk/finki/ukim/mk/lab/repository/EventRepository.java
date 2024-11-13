package mk.finki.ukim.mk.lab.repository;

import mk.finki.ukim.mk.lab.model.Event;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mk.finki.ukim.mk.lab.data.DataHolder.events;

@Repository
public class EventRepository {
    public List<Event> findAll(){
        return events;
    }

    public List<Event> searchEvents(String text) {
        try {
            // Try to parse the input as a double for popularity score filtering
            Double rating = Double.parseDouble(text);

            // Filter by popularity score if parsing succeeds
            return events.stream()
                    .filter(event -> event.getPopularityScore() >= rating)
                    .toList();

        } catch (NumberFormatException e) {
            // If parsing fails, treat the input as a name filter
            return events.stream()
                    .filter(event -> event.getName().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }
    }

    public Optional<Event> findById(Long id){
        return events.stream().filter(e-> Objects.equals(e.getId(), id)).findFirst();
    }

    public void update(Event event) {
        events.removeIf(e->e.getId().equals(event.getId()));
        this.save(event);
    }

    public void save(Event event){
        events.add(event);
    }
    public void remove(Long id){
        events.removeIf(e->e.getId().equals(id));
    }

}
