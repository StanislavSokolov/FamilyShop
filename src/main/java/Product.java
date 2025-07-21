public class Product {
    private int id;
    private int user_id;
    private String supplierArticle;
    private String nmId;
    private String subject;
    private String shopName;
    private int price;
    private int discount;
    private String description;
    private String rating;
    private int minOrder;
    private int maxOrder;
    private int enControlPrice;

    public Product(int id, int user_id, String supplierArticle, String nmId, String subject, String shopName, int price, int discount, String description, String rating, int minOrder, int maxOrder, int enControlPrice) {
        this.id = id;
        this.user_id = user_id;
        this.supplierArticle = supplierArticle;
        this.nmId = nmId;
        this.subject = subject;
        this.shopName = shopName;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.rating = rating;
        this.minOrder = minOrder;
        this.maxOrder = maxOrder;
        this.enControlPrice = enControlPrice;
    }

    public Product(int id, String supplierArticle, String nmId, String subject, int price, int discount) {
        this.id = id;
        this.supplierArticle = supplierArticle;
        this.nmId = nmId;
        this.subject = subject;
        this.price = price;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSupplierArticle() {
        return supplierArticle;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public String getNmId() {
        return nmId;
    }

    public void setNmId(String nmId) {
        this.nmId = nmId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public int getEnControlPrice() {
        return enControlPrice;
    }

    public void setEnControlPrice(int enControlPrice) {
        this.enControlPrice = enControlPrice;
    }
}
