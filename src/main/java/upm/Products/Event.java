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
    public LocalDate expiration;


    public Event(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDate expiration, int maxParticipants) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive.");
        }

        if (name == null || name.isBlank() || name.length() >= MAX_CHAR_NAME) {
            throw new IllegalArgumentException("Invalid name.");
        }

        if (pricePerson <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.id = id;
        this.name = name.replace("\"", ""); //Quitamos comillas para que en la comparacion por nombre alfabetico no de error
        this.price = pricePerson;
        this.creationDate = creationDate;
        this.expiration = expiration;
        this.maxParticipants=maxParticipants;
    }

    public Event(int id, String name, double pricePerson, LocalDate expiration, int maxParticipants) {
        this(id, name, pricePerson, LocalDateTime.now(), expiration, maxParticipants);
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
        StringBuilder sb= new StringBuilder();
        sb.append("  {class: BasicProduct");
        sb.append(",id: ").append(id);
        sb.append(",name:").append(name);
        sb.append(",creationDate: ").append(creationDate);
        sb.append(",plannedDate: ").append(expiration);
        sb.append(",price:").append(String.format("%.2f", price)).append("}");
        return sb.toString();
    }
}
