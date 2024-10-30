package mk.finki.ukim.mk.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.finki.ukim.mk.lab.service.EventBookingService;
import mk.finki.ukim.mk.lab.service.EventService;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name="EventBookingServlet", urlPatterns = "/eventBooking")
public class EventBookingServlet extends HttpServlet {
    private final EventBookingService service;
    private final SpringTemplateEngine springTemplateEngine;

    public EventBookingServlet(EventBookingService service, SpringTemplateEngine springTemplateEngine) {
        this.service = service;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req,resp);

        WebContext context = new WebContext(webExchange);

        String eventName = req.getParameter("event");
        String numTickets = req.getParameter("numTickets");
        String clientIp = req.getRemoteAddr();
        String clientName = req.getParameter("clientName");

        context.setVariable("eventName", eventName);
        context.setVariable("numTickets", numTickets);
        context.setVariable("clientIp", clientIp);
        context.setVariable("clientName", clientName);

        this.springTemplateEngine.process("bookingConfirmation.html", context, resp.getWriter());
    }
}
