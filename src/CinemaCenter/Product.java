package cinemaCenter;

/**
 * Represents a Product from the Products table.
 */
public class Product {
    private int productID;
    private String name;
    private double price;
    private int stockQuantity;
    private String category;

    /**
     * Constructs a Product object.
     * @param productID The product ID
     * @param name The product name
     * @param price The product price
     * @param stockQuantity The current stock
     * @param category The product category (Beverage, Snack, Toy, etc.)
     */
    public Product(int productID, String name, double price, int stockQuantity, String category) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }

    public int getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return name + " (" + price + ")";
    }
}
