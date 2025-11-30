package upm.Products;

public interface IProduct {
    void setName(String name);
    void setPrice(double price);
    double getPrice();
    String getName();
    int getId();
    String toString();
}
