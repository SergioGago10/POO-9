package upm.Products;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FoodProduct extends Event{
    private static final int daysPlanned=12;

    public FoodProduct(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDate expiration,int maxParticipants){
        super(id,name,pricePerson,creationDate,expiration,maxParticipants);
        if ((creationDate.plusDays(3).isAfter(expiration.atStartOfDay()))) {
            throw new IllegalArgumentException("The meeting should be planned at least 3 days before");
        }
    }

    public FoodProduct( String name, double pricePerson, LocalDateTime creationDate, LocalDate expiration,int maxParticipants){
        this(Catalog.generateNewProductId(),name,pricePerson,creationDate,expiration,maxParticipants);
    }

    public FoodProduct(int id, String name, double pricePerson, LocalDate expiration, int maxParticipants){
        super(id,name,pricePerson,expiration,maxParticipants);
        if ((this.creationDate.plusDays(3).isAfter(expiration.atStartOfDay()))) {
            throw new IllegalArgumentException("The meeting should be planned at least 3 days before");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  {class: Food");
        sb.append(super.toString());
        return sb.toString();
    }
}
