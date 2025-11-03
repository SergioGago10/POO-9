package upm;

import java.time.LocalDateTime;

//hay que poner minutos
public class Event extends ProductAbstract {
    private int maxParticipants;
    private LocalDateTime creationDate;
    private LocalDateTime expiration;


    public Event(int id, String name, double pricePerson, LocalDateTime creationDate, LocalDateTime expiration) {
        super(id,name,pricePerson);
        if ((creationDate.plusHours(12).isAfter(expiration))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
        this.creationDate = creationDate;
        this.expiration = expiration;
    }

    public Event(int id, String name, double pricePerson, LocalDateTime expiration) {
        super(id,name,pricePerson);
        this.creationDate = LocalDateTime.now();
        this.expiration = expiration;
        if ((creationDate.plusHours(12).isAfter(expiration))) {
            throw new IllegalArgumentException("The meeting should be planned at least 12 hours before");
        }
    }



}
