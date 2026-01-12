package upm.tickets.discount;

public class DiscountResult {
    private final double totalWithout;
    private final double totalWith;
    private final double totalDiscount;

    public DiscountResult(double totalWithout, double totalWith, double totalDiscount) {
        this.totalWithout = totalWithout;
        this.totalWith = totalWith;
        this.totalDiscount = totalDiscount;
    }

    public double getTotalWithout() {return totalWithout;}
    public double getTotalDiscount() {return totalDiscount;}
    public double getTotalWith() {return totalWith;}
}
