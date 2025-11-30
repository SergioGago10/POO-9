package upm.Products;

public interface IProduct {
    void setName(String name);
    void setPrice(double price);
    double getPrice();
    String getName();
    String getId();
    String toString();
}
