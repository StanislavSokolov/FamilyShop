public class Stock {
    private String warehouseName;
    private int quantity;
    private int quantityFull;
    private int inWayFromClient;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityFull() {
        return quantityFull;
    }

    public void setQuantityFull(int quantityFull) {
        this.quantityFull = quantityFull;
    }

    public int getInWayFromClient() {
        return inWayFromClient;
    }

    public void setInWayFromClient(int inWayFromClient) {
        this.inWayFromClient = inWayFromClient;
    }

    public Stock(String warehouseName, int quantity, int quantityFull, int inWayFromClient) {
        this.warehouseName = warehouseName;
        this.quantity = quantity;
        this.quantityFull = quantityFull;
        this.inWayFromClient = inWayFromClient;
    }
}
