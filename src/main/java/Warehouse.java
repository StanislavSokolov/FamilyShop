import java.util.ArrayList;

public class Warehouse {
    private String name;
    private int id;
    private String column;

    public Warehouse(String name, int id, String column, int coefficient) {
        this.name = name;
        this.id = id;
        this.column = column;
        this.coefficient = coefficient;
        this.dates = dates;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    private int coefficient;
    private ArrayList<Date> dates;

    public ArrayList<Date> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Date> dates) {
        this.dates = dates;
    }

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
        dates = new ArrayList<>();
    }
}
