public class DayToSend {
    String date = "";
    int coefficient = 0;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public DayToSend(String date, int coefficient) {
        this.date = date;
        this.coefficient = coefficient;
    }
}
