public class Person {
    private String userName;
    private int chatId;
    private int ELECTROSTAL;
    private int TULA;
    private int NEVINOMISK;
    private int KRASNODAR;
    private int KOLEDINO;
    private int KAZAN;

    public int getELECTROSTAL() {
        return ELECTROSTAL;
    }

    public void setELECTROSTAL(int ELECTROSTAL) {
        this.ELECTROSTAL = ELECTROSTAL;
    }

    public int getTULA() {
        return TULA;
    }

    public void setTULA(int TULA) {
        this.TULA = TULA;
    }

    public int getNEVINOMISK() {
        return NEVINOMISK;
    }

    public void setNEVINOMISK(int NEVINOMISK) {
        this.NEVINOMISK = NEVINOMISK;
    }

    public int getKRASNODAR() {
        return KRASNODAR;
    }

    public void setKRASNODAR(int KRASNODAR) {
        this.KRASNODAR = KRASNODAR;
    }

    public int getKOLEDINO() {
        return KOLEDINO;
    }

    public void setKOLEDINO(int KOLEDINO) {
        this.KOLEDINO = KOLEDINO;
    }

    public int getKAZAN() {
        return KAZAN;
    }

    public void setKAZAN(int KAZAN) {
        this.KAZAN = KAZAN;
    }

    public Person(String userName, int chatId, int ELECTROSTAL, int TULA, int NEVINOMISK, int KRASNODAR, int KOLEDINO, int KAZAN) {
        this.userName = userName;
        this.chatId = chatId;
        this.ELECTROSTAL = ELECTROSTAL;
        this.TULA = TULA;
        this.NEVINOMISK = NEVINOMISK;
        this.KRASNODAR = KRASNODAR;
        this.KOLEDINO = KOLEDINO;
        this.KAZAN = KAZAN;
    }

    public Person(String userName, int chatId) {
        this.userName = userName;
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}

