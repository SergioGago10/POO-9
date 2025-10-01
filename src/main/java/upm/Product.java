package upm;

enum Category {
    MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONIC
}

public class Product {
    private final static int MAX_CHAR_NAME = 100;
    private int id;
    private String name;
    private Category category;
    private int price;//debatir si int o double

    public Product(int id, String name, String category, int price) {
        this.id = id;
        this.name = name;
        this.category = Category.valueOf(category);
        this.price=price;
    }


    public static int getMaxCharName(){
        return MAX_CHAR_NAME;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = Category.valueOf(category);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
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

