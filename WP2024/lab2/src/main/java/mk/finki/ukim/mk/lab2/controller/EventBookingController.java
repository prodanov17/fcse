package mk.finki.ukim.mk.lab2.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.finki.ukim.mk.lab2.model.EventBooking;
import mk.finki.ukim.mk.lab2.service.EventBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/eventBooking")
public class EventBookingController {
    private final EventBookingService eventBookingService;

    public EventBookingController(EventBookingService eventBookingService) {
        this.eventBookingService = eventBookingService;
    }

    @GetMapping
    public String getBookingConfirmation(@RequestParam(required = false) String error, Model model, Principal principal, HttpServletRequest req){
        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        String name = principal.getName();

        List<EventBooking> eventBookings = this.eventBookingService.findByName(name);
        if(eventBookings.isEmpty()){
            return "redirect:/events?error=NoBookingFound";
        }
        EventBooking lastBooking = eventBookings.get(eventBookings.size() - 1);

        model.addAttribute("eventName", lastBooking.getEventName());
        model.addAttribute("numTickets", lastBooking.getNumberOfTickets());
        model.addAttribute("clientIp", "127.0.0.1");
        model.addAttribute("clientName", lastBooking.getAttendeeName());

        model.addAttribute("bookings", eventBookings);

        return "bookingConfirmation";
    }

    @PostMapping
    public String saveBooking(@RequestParam String event, @RequestParam int numTickets, Principal principal){
        String username = principal.getName();
        this.eventBookingService.placeBooking(Long.parseLong(event), username, numTickets);

        return "redirect:/eventBooking";
    }

}
