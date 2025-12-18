package upm.Products;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ProductService implements IProduct{
    private String id;
    private ServiceCategory category;
    private LocalDateTime maxDate;

    public ProductService(String id, ServiceCategory category, LocalDateTime maxDate){
        this.id=id;
        this.category=category;
        this.maxDate=maxDate;
    }

    public void setCategory(ServiceCategory category){
        this.category=category;
    }

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
        return sb.toString();
    }
}
