package mk.finki.ukim.mk.lab.repository;

import mk.finki.ukim.mk.lab.model.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRepository {
    private List<Event> events;

    public EventRepository(){
        this.events = new ArrayList<Event>();

        this.events.add(new Event("Marathon", "Marathon Desc", 13.0));
        this.events.add(new Event("Dadada", "Dadada Desc", 16.0));
        this.events.add(new Event("Fafafa", "fafafa Desc", 23.0));
        this.events.add(new Event("Gagagag", "gagagag Desc", 30.0));
        this.events.add(new Event("Cacacaca", "cacacaca Desc", 10.0));
    }

    public List<Event> findAll(){
        return this.events;
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

}
