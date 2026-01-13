    package upm.tickets.discount;

    import upm.products.BasicProduct;
    import upm.products.Item;
    import upm.products.Product;
    import upm.products.Category;
    import upm.tickets.core.Ticket;

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
        public DiscountResult calculateTotals(Ticket<? extends Item> ticket) {
            Map<Product, Double> discounts = discountPerProduct(ticket);
            double totalWithout = 0.0;
            double totalWith = 0.0;
            for (Item product : ticket.getItemsList()) {
                if (product instanceof Product productAdder) {
                    double price = productAdder.getPrice();
                    totalWithout += price;
                    totalWith += price * discounts.get(productAdder);
                }
            }
            double totalDiscount = totalWithout - totalWith;
            return new DiscountResult(totalWithout,totalWith,totalDiscount);
        }

        /**
         * Esta funcion es específica para el descuento por productos, ya que en esta estrategia
         * el descuento aplicado es por tipos de producto, donde se aplica un descuento u otro dependiendo de como sea
         * el tipo de producto
         */
        public Map<Product, Double> discountPerProduct(Ticket<? extends Item> ticket) {
            List<? extends Item> itemList = ticket.getItemsList();
            Map<Category, Integer> categoryCounter = countProductsByCategory(itemList);
            Map<Product, Double> discountMap = new HashMap<>();
            for (Item item : itemList) {
                if (item instanceof Product product) {
                    double factor = 1.0; // Valor por defecto
                    if (product instanceof BasicProduct bp) {
                        Category category = bp.getCategory();
                        if (categoryCounter.get(category) >= 2) {
                            factor = whatDiscountToApply(category);
                        }
                    }
                    // Para productos que no sean BasicProduct, factor seguirá siendo 1.0
                    discountMap.put(product, factor);
                }
            }
            return discountMap;
        }

        private Map<Category, Integer> countProductsByCategory(List<? extends Item> products) {
            Map<Category, Integer> counter = new EnumMap<>(Category.class);
            for (Category category : Category.values()) {
                counter.put(category, 0);
            }
            for (Item product : products) {
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
