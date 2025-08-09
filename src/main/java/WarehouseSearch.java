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

    static final int RYAZAN = 301760;
    static final String RYAZAN_DESCRIPTION = "ID :" + RYAZAN + "name: Рязань, address: Индустриальный промышленный парк Рязанский, Тюшевское сельское поселение, Рязанский район";
    static final String RYAZAN_DESCRIPTION_1 = "Рязань";
    static final String RYAZAN_DESCRIPTION_2 = "RYAZAN";

    static final int UTKINAZAVOD = 2737;
    static final String UTKINAZAVOD_DESCRIPTION = "ID :" + UTKINAZAVOD + "name: Уткина Заводь, address: Всеволожский р-н, г.п. Свердловское, дер. Новосаратовка, участок № 1 (промзона Уткина Заводь, комплекс МЛП, корпус 4, парадная 4)";
    static final String UTKINAZAVOD_DESCRIPTION_1 = "Уткина Заводь";
    static final String UTKINAZAVOD_DESCRIPTION_2 = "UTKINAZAVOD";

    static final int PODOLSK = 117501;
    static final String PODOLSK_DESCRIPTION = "ID :" + PODOLSK + "name: Подольск, address: ул. Поливановская, 9с5";
    static final String PODOLSK_DESCRIPTION_1 = "Подольск";
    static final String PODOLSK_DESCRIPTION_2 = "PODOLSK";

    static final int EKATERINBURG14G = 1733;
    static final String EKATERINBURG14G_DESCRIPTION = "ID :" + EKATERINBURG14G + "name: Екатеринбург, address: ул. Испытателей, 14Г";
    static final String EKATERINBURG14G_DESCRIPTION_1 = "Екатеринбург - Испытателей 14г";
    static final String EKATERINBURG14G_DESCRIPTION_2 = "EKATERINBURG14G";


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

        warehouseArrayList.add(new Warehouse(UTKINAZAVOD_DESCRIPTION_1, UTKINAZAVOD, UTKINAZAVOD_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(NEVINOMISK_DESCRIPTION_1, NEVINOMISK, NEVINOMISK_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(KRASNODAR_DESCRIPTION_1, KRASNODAR, KRASNODAR_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(KAZAN_DESCRIPTION_1, KAZAN, KAZAN_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(EKATERINBURG14G_DESCRIPTION_1, EKATERINBURG14G, EKATERINBURG14G_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(KOLEDINO_DESCRIPTION_1, KOLEDINO, KOLEDINO_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(PODOLSK_DESCRIPTION_1, PODOLSK, PODOLSK_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(TULA_DESCRIPTION_1, TULA, TULA_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(ELECTROSTAL_DESCRIPTION_1, ELECTROSTAL, ELECTROSTAL_DESCRIPTION_2));
        warehouseArrayList.add(new Warehouse(RYAZAN_DESCRIPTION_1, RYAZAN, RYAZAN_DESCRIPTION_2));

        this.bot = bot;
    }

    @Override
    public void run() {

        super.run();
//        warehouses();
        while (true) {
            prepare();
            search();
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
                sleep(9000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bot.setAnswer(warehouseArrayList);
    }
}
