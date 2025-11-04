package upm.Products;

import java.time.LocalDateTime;

//hay que poner minutos
public abstract class Event implements IProduct {
    public int id;
    public String name;
    public double price;
    private int maxParticipants;
    private LocalDateTime creationDate;
    private LocalDateTime expiration;


    public Event(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDateTime expiration) {
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
    }

    public Event(int id, String name, double pricePerson, LocalDateTime expiration) {
        this(id, name, pricePerson, LocalDateTime.now(), expiration);
        if ((creationDate.plusHours(12).isAfter(expiration))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
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


}
