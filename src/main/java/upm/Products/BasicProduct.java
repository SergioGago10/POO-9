package upm.Products;

import upm.Catalog;

public class BasicProduct implements IProduct {
    public final static int MAX_CHAR_NAME = 100;
    public int id;
    public String name;
    public double price;
    private Category category;

    public BasicProduct(int id, String name, Category category, double price) {
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
        this.category = category;
    }


    public static int getMaxCharName(){
        return MAX_CHAR_NAME;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
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

    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        sb.append("  {class: BasicProduct");
        sb.append(",id: ").append(id);
        sb.append(",name:").append(name);
        sb.append(",Category:").append(category);
        sb.append(",price:").append(String.format("%.2f", price)).append("}");
        return sb.toString();
    }
}

