package upm.Products;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public ServiceCategory getCategory() {
        return category;
    }

}
