package upm.Products;

public class BasicProduct extends Product {
    protected Category category;

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

    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        sb.append("  {class:Product");
        sb.append(",id: ").append(id);
        sb.append(",name:").append(name);
        sb.append(",Category:").append(category);
        sb.append(",price:").append(String.format("%.2f", price)).append("}");
        return sb.toString();
    }
}

