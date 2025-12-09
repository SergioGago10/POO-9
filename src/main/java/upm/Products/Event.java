package upm.Products;

import java.time.LocalDateTime;

//hay que poner minutos
public class Event extends Product {
    private int maxParticipants;
    private LocalDateTime creationDate;
    private LocalDateTime plannedDate;
    private TypeEvent typeEvent;


    public Event(String id, String name, double pricePerson, LocalDateTime creationDate, LocalDateTime plannedDate,
                 int maxParticipants, TypeEvent typeEvent) {
        this.creationDate = creationDate;
        this.plannedDate = plannedDate;
        this.id = id;
        this.name = name.replace("\"", ""); //Quitamos comillas para que en la comparacion por nombre alfabetico no de error
        this.price = pricePerson;
        this.maxParticipants = maxParticipants;
        this.typeEvent=typeEvent;
    }

    public Event(String id, String name, double pricePerson, LocalDateTime plannedDate, int maxParticipants, TypeEvent typeEvent) {
        this(id, name, pricePerson, LocalDateTime.now(), plannedDate, maxParticipants,typeEvent);
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

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public LocalDateTime getPlannedDate() {
        return plannedDate;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }
}
