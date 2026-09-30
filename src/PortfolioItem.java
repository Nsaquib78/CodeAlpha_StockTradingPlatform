public class PortfolioItem {
    private String symbol;
    private int quantity;
    private double averagePurchasePrice;

    public PortfolioItem(String symbol, int quantity, double averagePurchasePrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.averagePurchasePrice = averagePurchasePrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAveragePurchasePrice() {
        return averagePurchasePrice;
    }

    public void setAveragePurchasePrice(double averagePurchasePrice) {
        this.averagePurchasePrice = averagePurchasePrice;
    }
}
