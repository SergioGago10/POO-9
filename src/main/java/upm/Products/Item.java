package upm.Products;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import upm.tickets.core.Ticket;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        // Productos
        @JsonSubTypes.Type(value = BasicProduct.class, name = "BasicProduct"),
        @JsonSubTypes.Type(value = CustomizableProduct.class, name = "CustomizableProduct"),
        @JsonSubTypes.Type(value = Event.class, name = "Event"),

        // Servicios
        @JsonSubTypes.Type(value = ProductService.class, name = "service")
})
public interface Item {
    String getId();
    boolean addTo(Ticket<?> ticket);

}
