package upm.Products;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BasicProduct.class, name = "Products.BasicProduct"),
        @JsonSubTypes.Type(value = CustomizableProduct.class, name = "Products.CustomizableProduct"),
        @JsonSubTypes.Type(value = Event.class, name = "Products.Event")
})
public abstract class Product implements Item {
    protected String id;
    protected String name;
    protected double price;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
