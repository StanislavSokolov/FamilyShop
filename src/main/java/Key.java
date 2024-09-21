public class Key {
    String warehouse;
    int value;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Key(String warehouse, int value) {
        this.warehouse = warehouse;
        this.value = value;
    }
}
