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
        this.dayToSends = dayToSends;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    private int coefficient;
    private ArrayList<DayToSend> dayToSends;

    public ArrayList<DayToSend> getDayToSends() {
        return dayToSends;
    }

    public void setDayToSends(ArrayList<DayToSend> dayToSends) {
        this.dayToSends = dayToSends;
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
        dayToSends = new ArrayList<>();
    }
}
