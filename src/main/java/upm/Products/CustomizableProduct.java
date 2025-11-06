package upm.Products;

public class CustomizableProduct extends BasicProduct {
    private int maxCustomTexts;

    public CustomizableProduct(int id, String name, Category category, double price, int maxCustomTexts) {
        super(id, name, category, price);
        this.maxCustomTexts = maxCustomTexts;
    }


    public int getMaxCustomTexts() {
        return maxCustomTexts;
    }

    @Override
    public String toString(){
        StringBuilder resul=new StringBuilder();
        resul.append(super.toString());
        resul.deleteCharAt(resul.length()-1); //para quitarle el } del final
        resul.append(", maxCustomTexts:").append(maxCustomTexts).append("}");
        return resul.toString();
    }

}
