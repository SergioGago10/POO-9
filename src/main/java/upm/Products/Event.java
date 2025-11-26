package upm.Products;

import java.time.LocalDate;
import java.time.LocalDateTime;

//hay que poner minutos
public abstract class Event implements IProduct {
    public int id;
    public String name;
    public double price;
    public int maxParticipants;
    public LocalDateTime creationDate;
    public LocalDate plannedDate;
    public static final LocalDate expiration = LocalDate.of(2025, 12, 30);


    public Event(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDate plannedDate,
                 int maxParticipants) {
        if (expiration.isBefore(plannedDate))
            throw new IllegalArgumentException("Date must be before "+expiration);
        this.id = id;
        this.name = name.replace("\"", ""); //Quitamos comillas para que en la comparacion por nombre alfabetico no de error
        this.price = pricePerson;
        this.creationDate = creationDate;
        this.plannedDate = plannedDate;
        this.maxParticipants = maxParticipants;
    }

    public Event(int id, String name, double pricePerson, LocalDate plannedDate, int maxParticipants) {
        this(id, name, pricePerson, LocalDateTime.now(), plannedDate, maxParticipants);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(", id: ").append(id);
        sb.append(", name:").append(name);
        sb.append(", price:").append(String.format("%.2f", price));
        sb.append(", date of Event: ").append(plannedDate);
        sb.append(", max people allowed:").append(maxParticipants);
        return sb.toString();
    }
}
