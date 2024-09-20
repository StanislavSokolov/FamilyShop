public class Warehouse {
    private String name;
    private int id;
    private String column;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Warehouse(String name, int id, String column) {
        this.name = name;
        this.id = id;
        this.column = column;
    }
}
