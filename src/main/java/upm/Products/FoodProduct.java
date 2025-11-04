package upm.Products;

import upm.Catalog;

import java.time.LocalDateTime;

public class FoodProduct extends Event{
    private static final int daysPlanned=12;

    public FoodProduct(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDateTime expiration){
        super(id,name,pricePerson,creationDate,expiration);
        if ((creationDate.plusDays(3).isAfter(expiration))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
    }

    public FoodProduct( String name, double pricePerson, LocalDateTime creationDate, LocalDateTime expiration){
        this(Catalog.generateNewProductId(),name,pricePerson,creationDate,expiration);
    }
}
