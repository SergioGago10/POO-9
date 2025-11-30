package upm.Products;

import java.time.LocalDate;
import java.time.LocalDateTime;

//hay que poner minutos
public class Event implements IProduct {
    private int id;
    private String name;
    private double price;
    private int maxParticipants;
    private LocalDateTime creationDate;
    private LocalDate plannedDate;
    private TypeEvent typeEvent;


    public Event(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDate plannedDate,
                 int maxParticipants, TypeEvent typeEvent) {
        this.creationDate = creationDate;
        this.plannedDate = plannedDate;
        this.id = id;
        this.name = "'" + name.replace("\"", "").replace("'", "") + "'";
        //Quitamos comillas para que en la comparacion por nombre alfabetico no de error + poner simples para formato
        this.price = pricePerson;
        this.maxParticipants = maxParticipants;
        this.typeEvent=typeEvent;
    }

    public Event(int id, String name, double pricePerson, LocalDate plannedDate, int maxParticipants, TypeEvent typeEvent) {
        this(id, name, pricePerson, LocalDateTime.now(), plannedDate, maxParticipants,typeEvent);
    }


    public void setName(String name) {
        this.name = "'" + name.replace("\"", "").replace("'", "") + "'";;
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
        sb.append("  {class:").append(typeEvent);
        sb.append(", id: ").append(id);
        sb.append(", name:").append(name);
        sb.append(", price:").append(String.format("%.2f", price));
        sb.append(", date of Event: ").append(plannedDate);
        sb.append(", max people allowed:").append(maxParticipants).append("}");
        return sb.toString();
    }
}
