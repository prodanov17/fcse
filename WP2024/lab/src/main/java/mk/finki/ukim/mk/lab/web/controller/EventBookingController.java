package mk.finki.ukim.mk.lab.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.EventBooking;
import mk.finki.ukim.mk.lab.service.EventBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/eventBooking")
public class EventBookingController {
    private final EventBookingService eventBookingService;

    public EventBookingController(EventBookingService eventBookingService) {
        this.eventBookingService = eventBookingService;
    }

    @GetMapping
    public String getBookingConfirmation(@RequestParam(required = false) String error, Model model, HttpServletRequest req){
        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        if(req.getSession().getAttribute("name") == null){
            return "redirect:/events?error=NoSession";
        }

        String name = req.getSession().getAttribute("name").toString();

        List<EventBooking> eventBookings = this.eventBookingService.findByName(name);
        if(eventBookings.isEmpty()){
            return "redirect:/events?error=NoBookingFound";
        }
        EventBooking lastBooking = eventBookings.get(eventBookings.size() - 1);

        model.addAttribute("eventName", lastBooking.getEventName());
        model.addAttribute("numTickets", lastBooking.getNumberOfTickets());
        model.addAttribute("clientIp", lastBooking.getAtendeeAddress());
        model.addAttribute("clientName", lastBooking.getAttendeeName());

        model.addAttribute("bookings", eventBookings);

        return "bookingConfirmation";
    }

    @PostMapping
    public String saveBooking(@RequestParam String event, @RequestParam int numTickets, @RequestParam String name, HttpServletRequest req){
        String clientIp = req.getRemoteAddr();
        this.eventBookingService.placeBooking(event, name, clientIp, numTickets);
        if(req.getSession().getAttribute("name") == null){
            req.getSession().setAttribute("name", name);
        }

        return "redirect:/eventBooking";
    }

}
