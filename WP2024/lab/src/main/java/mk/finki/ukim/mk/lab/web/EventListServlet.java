package mk.finki.ukim.mk.lab.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.service.EventService;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


import java.io.IOException;
import java.util.List;

@WebServlet(name = "EventListServlet", urlPatterns = "/events")
public class EventListServlet extends HttpServlet {
    private final EventService service;
    private final SpringTemplateEngine springTemplateEngine;

    public EventListServlet(EventService service, SpringTemplateEngine springTemplateEngine) {
        this.service = service;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req,resp);

        WebContext context = new WebContext(webExchange);

        String searchQuery = req.getParameter("search");
        List<Event> events;

        if (searchQuery != null){
            events = service.searchEvents(searchQuery);
        }
        else {
            events = service.listAll();
        }

        context.setVariable("events", events);

        this.springTemplateEngine.process("listEvents.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eventName = req.getParameter("event");
        String numTickets = req.getParameter("numTickets");
        String clientName = req.getParameter("name");

        resp.sendRedirect("/eventBooking?event=" + eventName + "&numTickets=" + numTickets + "&clientName=" + clientName);
    }
}
