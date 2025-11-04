package upm.Products;

public interface IProduct {
    int MAX_CHAR_NAME=100;
    public static int getMaxCharName() {
        return MAX_CHAR_NAME;
    }
    void setName(String name);
    void setPrice(double price);
    double getPrice();
    String getName();
    int getId();
}
