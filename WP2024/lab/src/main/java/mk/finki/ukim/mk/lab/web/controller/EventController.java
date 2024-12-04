package mk.finki.ukim.mk.lab.web.controller;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.service.EventService;
import mk.finki.ukim.mk.lab.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;

    public EventController(EventService eventService, LocationService locationService) {
        this.eventService = eventService;
        this.locationService = locationService;
    }

    @GetMapping
    public String getEventsPage(@RequestParam(required = false) String error, Model model, @RequestParam(required = false) String search, @RequestParam(required = false, name = "location_id") Long locationId){
        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Event> events;


        if (search != null){
            events = eventService.searchEvents(search);
        } else if(locationId != null && locationId != 0){
            events = eventService.findAllByLocationId(locationId);
        }
        else {
            events = eventService.listAll();
        }

        List<Location> locations = locationService.findAll();

        model.addAttribute("bodyContent", "events");
        model.addAttribute("events", events);
        model.addAttribute("locations", locations);

        return "listEvents";
    }

    @GetMapping("/create")
    public String getEventCreatePage(@RequestParam(required = false) String error, Model model){
        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", true);
        }

        model.addAttribute("locations", locationService.findAll());

        return "createEvent";
    }

    @PostMapping
    public String saveEvent(@RequestParam String name, @RequestParam Double popularityScore, @RequestParam String description, @RequestParam Long location) throws Exception {
        this.eventService.save(name, description, popularityScore, location);
        return "redirect:/events";
    }

    @PostMapping("/edit/{id}")
    public String saveEditEvent(@PathVariable Long id, @RequestParam String name, @RequestParam Double popularityScore, @RequestParam String description, @RequestParam Long location) throws Exception {
        this.eventService.update(id, name, description, popularityScore, location);
        return "redirect:/events";
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id){
        this.eventService.remove(id);

        return "redirect:/events";
    }

    @GetMapping("/edit/{eventId}")
    public String editEvent(@PathVariable Long eventId, @RequestParam(required = false) String error, Model model){
        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        Optional<Event> event = this.eventService.findById(eventId);
        if(event.isEmpty()){
            return "redirect:/events?error=EventNotFound";
        }

        List<Location> locations = this.locationService.findAll();
        model.addAttribute("event", event.get());
        model.addAttribute("locations", locations);

        return "editEvent";
    }
}
