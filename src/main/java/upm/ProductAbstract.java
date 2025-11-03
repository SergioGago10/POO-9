package upm;

public abstract class ProductAbstract {
    public final static int MAX_CHAR_NAME = 100;
    public int id;
    public String name;
    public double price;

    public ProductAbstract(int id, String name, double price) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive.");
        }

        if (name == null || name.isBlank() || name.length() >= MAX_CHAR_NAME) {
            throw new IllegalArgumentException("Invalid name.");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.id = id;
        this.name = name.replace("\"", ""); //Quitamos comillas para que en la comparacion por nombre alfabetico no de error
        this.price = price;
    }

    public static int getMaxCharName(){
        return MAX_CHAR_NAME;
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
