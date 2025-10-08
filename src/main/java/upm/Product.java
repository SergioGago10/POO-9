package upm;

enum Category {
    MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONIC
}

public class Product {
    private final static int MAX_CHAR_NAME = 100;
    private int id;
    private String name;
    private Category category;
    private double price;
    //El precio debe ser double, ya que con los descuentos obtenemos decimales y además los precios pueden tener decimales al ser euros y centimos
    //no creo que caparlo a un int sea buena idea

    public Product(int id, String name, Category category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price=price;
    }


    public static int getMaxCharName(){
        return MAX_CHAR_NAME;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}

