package mk.finki.ukim.mk.lab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventBooking {
    private String eventName;
    private String attendeeName;
    private String atendeeAddress;
    private Long numberOfTickets;
}
