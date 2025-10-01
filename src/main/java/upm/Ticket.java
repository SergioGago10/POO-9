package upm;

public class Ticket {
    private final static int MAX_PRODUCTOS=100;
    private Product[] productsList;
    private int discount;
    private int amountProducts;
    private int totalCost;

    public Ticket(){
        productsList =new Product[MAX_PRODUCTOS];
        this.amountProducts=0;
        totalCost=0;
    }
    // cambiar usando amountProd
    public void addProduct(Product product){
        int i=0;
       while(this.productsList[i]!=null){
           i++;
       }
       this.productsList[i]=product;
    }

    public void sortProducts(){

    }

    public int getTotalCost(){// debatir si calcular el precio al pedirlo o que se vaya actualizando al añadir o quitar
        int stationeryCounter=0, clothesCounter=0, bookCounter=0, electronicCounter=0;
        for(int i=0;i<productsList.length;i++){
            totalCost+=productsList[i].getPrice();
            switch (productsList[i].getCategory()){
                case STATIONERY:
                    stationeryCounter++;
                    break;
                case CLOTHES:
                    clothesCounter++;
                    break;
                case BOOK:
                    bookCounter++;
                    break;
                case ELECTRONIC:
                    electronicCounter++;
                    break;
                default:
                    break;
            }
        }
        if(clothesCounter>1)
            totalCost= (int) (totalCost*0.93);
        if(bookCounter>1)
            totalCost= (int) (totalCost*0.9);
        if(stationeryCounter>1)
            totalCost= (int) (totalCost*0.95);
        if(electronicCounter>1)
            totalCost= (int) (totalCost*0.97);
        return this.totalCost;
    }



}
