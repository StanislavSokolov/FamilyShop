public class Item {
    private int id;
    private String cdate;
    private String ctime;
    private String sdate;
    private String stime;
    private int finishedPrice;
    private int forPay;
    private String odid;
    private int product_id;
    private int countSales;

    public int getCountSales() {
        return countSales;
    }

    public void setCountSales(int countSales) {
        this.countSales = countSales;
    }

    public int getCountOrders() {
        return countOrders;
    }

    public void setCountOrders(int countOrders) {
        this.countOrders = countOrders;
    }

    private int countOrders;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String subject;
    private String supplierArticle;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSupplierArticle() {
        return supplierArticle;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public int getFinishedPrice() {
        return finishedPrice;
    }

    public void setFinishedPrice(int finishedPrice) {
        this.finishedPrice = finishedPrice;
    }

    public int getForPay() {
        return forPay;
    }

    public void setForPay(int forPay) {
        this.forPay = forPay;
    }

    public String getOdid() {
        return odid;
    }

    public void setOdid(String odid) {
        this.odid = odid;
    }

    public String getOblastOkrugName() {
        return oblastOkrugName;
    }

    public void setOblastOkrugName(String oblastOkrugName) {
        this.oblastOkrugName = oblastOkrugName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Item(int product_id, String status) {
        this.product_id = product_id;
        this.status = status;
    }

    public Item(int id, int product_id, String cdate, String ctime, String sdate, String stime, int finishedPrice, int forPay, String odid, String oblastOkrugName, String warehouseName, String status) {
        this.id = id;
        this.product_id = product_id;
        this.cdate = cdate;
        this.ctime = ctime;
        this.sdate = sdate;
        this.stime = stime;
        this.finishedPrice = finishedPrice;
        this.forPay = forPay;
        this.odid = odid;
        this.oblastOkrugName = oblastOkrugName;
        this.warehouseName = warehouseName;
        this.status = status;
    }

    private String oblastOkrugName;
    private String warehouseName;
    private String status;
}