package upm.products;

import upm.tickets.itemsaddition.ItemAdditionVisitor;

import java.time.LocalDateTime;

//hay que poner minutos
public class Event extends Product {
    private int maxParticipants;
    private int actualParticipants;
    private LocalDateTime creationDate;
    private LocalDateTime plannedDate;
    private TypeEvent typeEvent;

    public Event() {
        // Jackson
    }
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

    public Event(String id, String name, double pricePerson, LocalDateTime creationDate, LocalDateTime plannedDate,
                 int maxParticipants, TypeEvent typeEvent, int actualParticipants){
        this(id,name,pricePerson,creationDate,plannedDate,maxParticipants,typeEvent);
        this.actualParticipants = actualParticipants;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  {class:").append(typeEvent);
        sb.append(", id: ").append(id);
        sb.append(", name:").append(name);
        sb.append(", price:").append(String.format("%.2f", price));
        sb.append(", date of Event: ").append(plannedDate.toLocalDate());
        sb.append(", max people allowed:").append(maxParticipants).append("}");
        return sb.toString();
    }

    @Override
    public boolean accept(ItemAdditionVisitor visitor, String[] args) {
        return visitor.add(this, args);
    }


    public String toTicketString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  {class:").append(typeEvent);
        sb.append(", id: ").append(id);
        sb.append(", name:").append(name);
        sb.append(", price:").append(String.format("%.2f", price));
        sb.append(", date of Event: ").append(plannedDate.toLocalDate());
        sb.append(", max people allowed:").append(maxParticipants);
        sb.append(", actual people in event:").append(actualParticipants).append("}");
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
    public LocalDateTime getCreationDate() {return creationDate;}
    public int getActualParticipants() {return actualParticipants;}


    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
    public void setActualParticipants(int actualParticipants) { this.actualParticipants = actualParticipants; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public void setPlannedDate(LocalDateTime plannedDate) { this.plannedDate = plannedDate; }
    public void setTypeEvent(TypeEvent typeEvent) { this.typeEvent = typeEvent; }
    public void setId(String id) { this.id = id; }
    public void setPrice(double price) { this.price = price; } // ya tienes setPrice en Product
    public void setName(String name) { this.name = name; }     // ya tienes setName en Product
}
