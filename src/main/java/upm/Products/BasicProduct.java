package upm.Products;

public class BasicProduct implements IProduct {
    public int id;
    public String name;
    public double price;
    public Category category;

    public BasicProduct(int id, String name, Category category, double price) {
        this.id = id;
        this.name = name.replace("\"", ""); //Quitamos comillas para que en la comparacion por nombre alfabetico no de error
        this.price = price;
        this.category = category;
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

