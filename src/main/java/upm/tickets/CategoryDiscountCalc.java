    package upm.tickets;

    import upm.Products.BasicProduct;
    import upm.Products.Product;
    import upm.Products.Category;

    import java.util.EnumMap;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class CategoryDiscountCalc implements ITicketDiscountCalc {
        private static final Map<Category, Double> CATEGORY_DISCOUNTS = new EnumMap<>(Category.class);
        //https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
        static {
            CATEGORY_DISCOUNTS.put(Category.STATIONERY, 0.95);
            CATEGORY_DISCOUNTS.put(Category.CLOTHES, 0.93);
            CATEGORY_DISCOUNTS.put(Category.BOOK, 0.90);
            CATEGORY_DISCOUNTS.put(Category.ELECTRONIC, 0.97);
        }

        @Override
        public Map<Product, Double> discountPerProduct(Ticket ticket) {
            List<Product> products = ticket.getProductsList();
            Map<Category, Integer> categoryCounter = countProductsByCategory(products);
            Map<Product, Double> discountMap = new HashMap<>();
            for (Product product : products) {
                double factor = 1.0; // Valor por defecto
                if (product instanceof BasicProduct) {
                    BasicProduct bp = (BasicProduct) product;
                    Category category = bp.getCategory();
                    if (categoryCounter.get(category) >= 2) {
                        factor = whatDiscountToApply(category);
                    }
                }
                // Para productos que no sean BasicProduct, factor seguirá siendo 1.0
                discountMap.put(product, factor);
            }

            return discountMap;
        }

        @Override
        public double[] calculateTotals(Ticket ticket) {
            Map<Product, Double> discounts = discountPerProduct(ticket);
            double totalWithout = 0.0;
            double totalWith = 0.0;
            for (Product product : ticket.getProductsList()) {
                double price = product.getPrice();
                totalWithout += price;
                totalWith += price * discounts.get(product);
            }
            double totalDiscount = totalWithout - totalWith;
            return new double[]{totalWithout, totalWith, totalDiscount};
        }

        private Map<Category, Integer> countProductsByCategory(List<Product> products) {
            Map<Category, Integer> counter = new EnumMap<>(Category.class);
            for (Category category : Category.values()) {
                counter.put(category, 0);
            }
            for (Product product : products) {
                if (product instanceof BasicProduct) {
                    Category category = ((BasicProduct) product).getCategory();
                    counter.put(category, counter.get(category) + 1);
                }
            }
            return counter;
        }

        private double whatDiscountToApply(Category category) {
            return CATEGORY_DISCOUNTS.getOrDefault(category, 1.0);
        }
    }
