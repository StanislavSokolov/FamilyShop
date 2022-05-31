public class ItemShop {
    private String supplierArticle = "";
    private int quantity = 0;
    private int quantityFull = 0;
    private int inWayToClient = 0;
    private int inWayFromClient = 0;
    private int nmId = 0;
    private String subject = "";
    private int price = 0;
    private int discount = 0;
    private int promoCode = 0;

    public ItemShop(String supplierArticle, int quantity, int quantityFull, int inWayToClient, int inWayFromClient, int nmId, String subject) {
        this.supplierArticle = supplierArticle;
        this.quantity = quantity;
        this.quantityFull = quantityFull;
        this.inWayToClient = inWayToClient;
        this.inWayFromClient = inWayFromClient;
        this.nmId = nmId;
        this.subject = subject;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setQuantityFull(int quantityFull) {
        this.quantityFull = quantityFull;
    }

    public void setInWayToClient(int inWayToClient) {
        this.inWayToClient = inWayToClient;
    }

    public void setInWayFromClient(int inWayFromClient) {
        this.inWayFromClient = inWayFromClient;
    }

    public void setPromoCode(int promoCode) {
        this.promoCode = promoCode;
    }

    public int getPromoCode() {
        return promoCode;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public void setNmId(int nmId) {
        this.nmId = nmId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSupplierArticle() {
        return supplierArticle;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityFull() {
        return quantityFull;
    }

    public int getInWayToClient() {
        return inWayToClient;
    }

    public int getInWayFromClient() {
        return inWayFromClient;
    }

    public int getNmId() {
        return nmId;
    }

    public String getSubject() {
        return subject;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }
}

