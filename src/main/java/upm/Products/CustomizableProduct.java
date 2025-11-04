package upm.Products;

public class CustomizableProduct extends BasicProduct {
    private int maxPersonalized;

    public CustomizableProduct(int id, String name, Category category, double price, int maxPersonalized) {
        super(id, name, category, price);
        this.maxPersonalized = maxPersonalized;
    }

    public CustomizableProduct(String name, Category category, double price, int maxPersonalized) {
        super(name, category, price);
        this.maxPersonalized = maxPersonalized;
    }


    public int getMaxPersonalized() {
        return maxPersonalized;
    }

}
