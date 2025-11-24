package upm.Products;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MeetingProduct extends Event{
    private static final int hoursPlanned=12;

    public MeetingProduct(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDate expiration,
                          int maxParticipants){
        super(id,name,pricePerson,creationDate,expiration,maxParticipants);
        if ((creationDate.plusHours(12).isAfter(expiration.atStartOfDay()))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
    }

    public MeetingProduct( String name, double pricePerson, LocalDateTime creationDate, LocalDate expiration,
                           int maxParticipants){
        this(Catalog.generateNewProductId(),name,pricePerson,creationDate,expiration,maxParticipants);
    }

    public MeetingProduct(int id, String name, double pricePerson, LocalDate expiration, int maxParticipants){
        super(id,name,pricePerson,expiration,maxParticipants);
        if ((this.creationDate.plusDays(3).isAfter(expiration.atStartOfDay()))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
    }
}
