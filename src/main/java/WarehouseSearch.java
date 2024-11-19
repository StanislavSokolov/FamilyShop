import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class WarehouseSearch extends Thread {

    static final int ELECTROSTAL = 120762;
    static final String ELECTROSTAL_DESCRIPTION = "ID: " + ELECTROSTAL + ", name: Электросталь, address: Московская область, Электросталь, посёлок Случайный, территория Массив 3, 5";
    static final String ELECTROSTAL_DESCRIPTION_1 = "Электросталь";
    static final String ELECTROSTAL_DESCRIPTION_2 = "ELECTROSTAL";

    static final int TULA = 206348;
    static final String TULA_DESCRIPTION = "ID: " + TULA + ", name: Тула, address: Тульская область, муниципальное образование Алексин, 1";
    static final String TULA_DESCRIPTION_1 = "Тула";
    static final String TULA_DESCRIPTION_2 = "TULA";

    static final int NEVINOMISK = 208277;
    static final String NEVINOMISK_DESCRIPTION = "ID: " + NEVINOMISK + ", name: Невинномысск, address: ул. Тимирязева 16";
    static final String NEVINOMISK_DESCRIPTION_1 = "Невинномысск";
    static final String NEVINOMISK_DESCRIPTION_2 = "NEVINOMISK";

    static final int KRASNODAR = 130744;
    static final String KRASNODAR_DESCRIPTION = "ID: " + KRASNODAR + ", name: Краснодар (Тихорецкая), address: ул. Тихорецкая, 40с1";
    static final String KRASNODAR_DESCRIPTION_1 = "Краснодар";
    static final String KRASNODAR_DESCRIPTION_2 = "KRASNODAR";

    static final int KOLEDINO = 507;
    static final String KOLEDINO_DESCRIPTION = "ID: " + KOLEDINO + ", name: Коледино, address: дер. Коледино, ул. Троицкая, 20";
    static final String KOLEDINO_DESCRIPTION_1 = "Коледино";
    static final String KOLEDINO_DESCRIPTION_2 = "KOLEDINO";

    static final int KAZAN = 117986;
    static final String KAZAN_DESCRIPTION = "ID :" + KAZAN + "name: Казань, address: Республика Татарстан, Зеленодольск, промышленный парк Зеленодольск, 20";
    static final String KAZAN_DESCRIPTION_1 = "Казань";
    static final String KAZAN_DESCRIPTION_2 = "KAZAN";
  
    private static ArrayList<Warehouse> warehouseArrayList;

    private boolean startMessage = false;

    String prevAnswer = "Бесплатные окна: ";

//    ArrayList<Warehouse> warehouseArrayList;

    public static ArrayList<Warehouse> getWarehouseArrayList() {
        return warehouseArrayList;
    }

    Bot bot;

    public WarehouseSearch(Bot bot) {
        warehouseArrayList = new ArrayList<>();

        warehouseArrayList.add(new Warehouse(NEVINOMISK_DESCRIPTION_1, NEVINOMISK, NEVINOMISK_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(KRASNODAR_DESCRIPTION_1, KRASNODAR, KRASNODAR_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(KAZAN_DESCRIPTION_1, KAZAN, KAZAN_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(KOLEDINO_DESCRIPTION_1, KOLEDINO, KOLEDINO_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(TULA_DESCRIPTION_1, TULA, TULA_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(ELECTROSTAL_DESCRIPTION_1, ELECTROSTAL, ELECTROSTAL_DESCRIPTION_2));





//        warehouseArrayList.add(new Warehouse(ELECTROSTAL_DESCRIPTION_1, ELECTROSTAL, ELECTROSTAL_DESCRIPTION_2));
//        warehouseArrayList.add(new Warehouse(TULA_DESCRIPTION_1, TULA, TULA_DESCRIPTION_2));
//        warehouseArrayList.add(new Warehouse(NEVINOMISK_DESCRIPTION_1, NEVINOMISK, NEVINOMISK_DESCRIPTION_2));
//        warehouseArrayList.add(new Warehouse(KRASNODAR_DESCRIPTION_1, KRASNODAR, KRASNODAR_DESCRIPTION_2));
//        warehouseArrayList.add(new Warehouse(KOLEDINO_DESCRIPTION_1, KOLEDINO, KOLEDINO_DESCRIPTION_2));
//        warehouseArrayList.add(new Warehouse(KAZAN_DESCRIPTION_1, KAZAN, KAZAN_DESCRIPTION_2));

        this.bot = bot;
    }

    @Override
    public void run() {

        super.run();
        while (true) {
            prepare();
            search();
//            try {
//                search();
//                sleep(650000);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void prepare() {
        if (!startMessage) bot.setAnswer();
        startMessage = true;
    }

    private void warehouses() {
        URL generetedURL = null;
        String response = null;
        generetedURL = URLRequestResponse.generateURL("wb","warehouses", "", "");
        try {
//            response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken(""));
            response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken("SOKOL0VE"));
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private void search() {
        URL generetedURL = null;
        String response = null;

        for (Warehouse wh: warehouseArrayList) {
            wh.setDayToSends(new ArrayList<>());
            generetedURL = URLRequestResponse.generateURL("wb","coefficients", "", String.valueOf(wh.getId()));
//            boolean coincidence = false;
            try {
                response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken("SOKOL0VE"));
//                System.out.println(response);
                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                    JSONObject jsonObject = new JSONObject("{\"data\":" + response + "}");
                    for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                        if (((Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(i).get("coefficient").toString()) >= 0) & (Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(i).get("coefficient").toString()) <= 10)) & (jsonObject.getJSONArray("data").getJSONObject(i).get("boxTypeName").equals("Короба") || jsonObject.getJSONArray("data").getJSONObject(i).get("boxTypeName").equals("Монопаллеты"))) {
                            String coefficient = "Бесплатно";
                            if (Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(i).get("coefficient").toString()) != 0) coefficient = "x" + jsonObject.getJSONArray("data").getJSONObject(i).get("coefficient").toString();
//                            System.out.println("HERE");
                            wh.getDayToSends().add(new DayToSend(String.valueOf(jsonObject.getJSONArray("data").getJSONObject(i).get("date")).substring(0, 10)
                                    + ": "
                                    + String.valueOf(jsonObject.getJSONArray("data").getJSONObject(i).get("boxTypeName")), Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(i).get("coefficient").toString())));
//                            coincidence = true;
                        }
                    }
//                    if (coincidence) {
//                        answer = answer + s;
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                e.getMessage();
            }
            try {
                sleep(12000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bot.setAnswer(warehouseArrayList);
    }
}
