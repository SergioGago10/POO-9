package upm.Products;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomizableProduct extends BasicProduct {
    private int maxCustomTexts;
    private List<String> customTexts;


    public CustomizableProduct(String id, String name, Category category, double price, int maxCustomTexts) {
        super(id, name, category, price);
        this.maxCustomTexts = maxCustomTexts;
        this.customTexts = new ArrayList<>();
    }

    public int getMaxCustomTexts() {
        return maxCustomTexts;
    }

    public List<String> getCustomTexts() {
        return Collections.unmodifiableList(customTexts);
    }

    public void setCustomTexts(List<String> texts) {
        customTexts.clear();
        if (texts != null) {
            customTexts.addAll(texts);
        }
    }

    public double calculateFinalPrice() {
        double extraPrice = 0;
        if (!customTexts.isEmpty()) {
            extraPrice = getPrice() * 0.10 * customTexts.size();
        }
        return getPrice() + extraPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  {class:ProductPersonalized");
        sb.append(", id: ").append(id);
        sb.append(", name:").append(name);
        sb.append(", category:").append(category);
        sb.append(", price:").append(String.format("%.2f", price));
        sb.append(", maxPersonal: ").append(maxCustomTexts);
        if (!customTexts.isEmpty()) {
            sb.append(", personalizationList:[").append(customTexts.get(0));
            int i=1;
            while(i<customTexts.size()) {
                sb.append(", ").append(customTexts.get(i));
                i++;
            }
        }
        sb.append("]}");
        return sb.toString();
    }

}
