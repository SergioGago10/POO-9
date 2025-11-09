    package upm.Products;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;

    public class CustomizableProduct extends BasicProduct {
        private int maxCustomTexts;
        private List<String> customTexts;


        public CustomizableProduct(int id, String name, Category category, double price, int maxCustomTexts) {
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
        public String toString(){
            StringBuilder resul=new StringBuilder();
            resul.append(super.toString());
            resul.deleteCharAt(resul.length()-1); //para quitarle el } del final
            resul.append(", maxCustomTexts:").append(maxCustomTexts).append("}");
            return resul.toString();
        }

    }
