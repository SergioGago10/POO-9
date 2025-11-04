package upm.Products;

import upm.Catalog;

import java.time.LocalDateTime;

public class MeetingProduct extends Event{
    private static final int hoursPlanned=12;

    public MeetingProduct(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDateTime expiration){
        super(id,name,pricePerson,creationDate,expiration);
        if ((creationDate.plusHours(12).isAfter(expiration))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
    }

    public MeetingProduct( String name, double pricePerson, LocalDateTime creationDate, LocalDateTime expiration){
        this(Catalog.generateNewProductId(),name,pricePerson,creationDate,expiration);
    }
}
