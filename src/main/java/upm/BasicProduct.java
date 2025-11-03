package upm;

enum Category {
    MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONIC
}

public class BasicProduct extends ProductAbstract{
    private Category category;

    public BasicProduct(int id, String name, Category category, double price) {
        super(id,name,price);
        this.category = category;
    }

    public BasicProduct(String name, Category category, double price) {
        super(Catalog.generateNewProductId(),name,price);
        this.category=category;
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
}

