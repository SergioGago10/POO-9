package upm.products;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import upm.tickets.core.Ticket;
import upm.tickets.itemsaddition.ItemAdditionVisitor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties({"type"})

public class ProductService implements Item {
    private String id;
    private ServiceCategory category;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime maxDate;

    public ProductService(){}
    public ProductService(String id, ServiceCategory category, LocalDateTime maxDate){
        this.id=id;
        this.category=category;
        this.maxDate=maxDate;
    }

    public void setCategory(ServiceCategory category){
        this.category=category;
    }

    public LocalDateTime getMaxDate() {return maxDate;}

    public String getId(){
        return id;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        ZonedDateTime zdt = maxDate.atZone(ZoneId.of("CET")); // Zona horaria CET
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        String formattedDate = zdt.format(formatter);
        sb.append("  {class:").append("ProductService");
        sb.append(", id: ").append(id);
        sb.append(", category:").append(category);
        sb.append(", expiration:").append(formattedDate);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void setCategoryFromCLI(String value) {
        setCategory(ServiceCategory.valueOf(value));
    }

    @Override
    public boolean addTo(Ticket<?> ticket) {
        // Al pasar 'this', el ticket recibe un objeto de tipo ProductService
        return ticket.addSpecificService(this);
    }

    @Override
    public boolean accept(ItemAdditionVisitor visitor, String[] args) {
        return visitor.add(this, args);
    }

    public ServiceCategory getCategory() {
        return category;
    }

}
