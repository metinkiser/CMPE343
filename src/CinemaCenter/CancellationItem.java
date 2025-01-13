package cinemaCenter;

/**
 * A simple data model representing either a Ticket or Product purchase
 * that can be canceled. The "type" indicates "Ticket" or "Product".
 */
public class CancellationItem {

    private String type;     // "Ticket" or "Product"
    private int itemId;      // TicketID or SaleID
    private String name;     // e.g. "Ticket #12" or "Popcorn"
    private int quantity;    // For products: purchased quantity; for ticket: "1"
    private double totalPrice; // The total price

    /**
     * Constructs a CancellationItem object.
     * @param type The type (Ticket/Product)
     * @param itemId The item ID (TicketID or SaleID)
     * @param name Display name
     * @param quantity Purchased quantity
     * @param totalPrice Total cost
     */
    public CancellationItem(String type, int itemId, String name, int quantity, double totalPrice) {
        this.type = type;
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getType() {
        return type;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
