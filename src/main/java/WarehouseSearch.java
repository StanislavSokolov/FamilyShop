import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class WarehouseSearch extends Thread {

    static final int ELECTROSTAL = 120762;
    static final String ELECTROSTAL_DESCRIPTION = "ID: " + ELECTROSTAL + ", name: Электросталь, address: Московская область, Электросталь, посёлок Случайный, территория Массив 3, 5";
    static final String ELECTROSTAL_DESCRIPTION_1 = "Электросталь";

    static final int TULA = 206348;
    static final String TULA_DESCRIPTION = "ID: " + TULA + ", name: Тула, address: Тульская область, муниципальное образование Алексин, 1";
    static final String TULA_DESCRIPTION_1 = "Тула (Алексин)";

    static final int NEVINOMISK = 208277;
    static final String NEVINOMISK_DESCRIPTION = "ID: " + NEVINOMISK + ", name: Невинномысск, address: ул. Тимирязева 16";
    static final String NEVINOMISK_DESCRIPTION_1 = "Невинномысск";

    static final int KRASNODAR = 130744;
    static final String KRASNODAR_DESCRIPTION = "ID: " + KRASNODAR + ", name: Краснодар (Тихорецкая), address: ул. Тихорецкая, 40с1";
    static final String KRASNODAR_DESCRIPTION_1 = "Краснодар (Тихорецкая)";

    static final int KOLEDINO = 507;
    static final String KOLEDINO_DESCRIPTION = "ID: " + KOLEDINO + ", name: Коледино, address: дер. Коледино, ул. Троицкая, 20";
    static final String KOLEDINO_DESCRIPTION_1 = "Коледино";

    static final int KAZAN = 117986;
    static final String KAZAN_DESCRIPTION = "ID :" + KAZAN + "name: Казань, address: Республика Татарстан, Зеленодольск, промышленный парк Зеленодольск, 20";
    static final String KAZAN_DESCRIPTION_1 = "Казань";

    String prevAnswer = "Бесплатные окна: ";

    ArrayList<Warehouse> warehouseArrayList;

    Bot bot;

    public WarehouseSearch(Bot bot) {
        warehouseArrayList = new ArrayList<>();

        warehouseArrayList.add(new Warehouse(ELECTROSTAL_DESCRIPTION_1, ELECTROSTAL));
        warehouseArrayList.add(new Warehouse(TULA_DESCRIPTION_1, TULA));
        //warehouseArrayList.add(new Warehouse(NEVINOMISK_DESCRIPTION_1, NEVINOMISK));
        warehouseArrayList.add(new Warehouse(KRASNODAR_DESCRIPTION_1, KRASNODAR));
        warehouseArrayList.add(new Warehouse(KOLEDINO_DESCRIPTION_1, KOLEDINO));
        warehouseArrayList.add(new Warehouse(KAZAN_DESCRIPTION_1, KAZAN));

        this.bot = bot;
    }

    @Override
    public void run() {

        super.run();
        while (true) {
            try {
                search();
                sleep(65000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void warehouses() {
        URL generetedURL = null;
        String response = null;
        generetedURL = URLRequestResponse.generateURL("wb","warehouses", "", "");
        try {
            response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken(""));
//            response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken("SOKOL0VE"));
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private void search() {
        URL generetedURL = null;
        String response = null;

        String answer = "Бесплатные окна: " + "\n";

        for (Warehouse w: warehouseArrayList) {
            generetedURL = URLRequestResponse.generateURL("wb","coefficients", "", String.valueOf(w.getId()));
            String s = "\n" + w.getName();
            boolean coincidence = false;
            try {
                response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken("SOKOL0VE"));
//                System.out.println(response);
                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                JSONObject jsonObject = new JSONObject("{\"data\":" + response + "}");
                for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                    if (Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(i).get("coefficient").toString()) == 0 & (jsonObject.getJSONArray("data").getJSONObject(i).get("boxTypeName").equals("Короба") || jsonObject.getJSONArray("data").getJSONObject(i).get("boxTypeName").equals("Монопаллеты"))) {
                        s = s + "\n"
                                + String.valueOf(jsonObject.getJSONArray("data").getJSONObject(i).get("date")).substring(0, 10)
                                + ": "
                                + String.valueOf(jsonObject.getJSONArray("data").getJSONObject(i).get("boxTypeName"));

                        coincidence = true;
                    }
                }
                if (coincidence) {
                    answer = answer + s;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                e.getMessage();
            }
        }

        if (!prevAnswer.equals(answer)) {
            if (!answer.equals("Бесплатные окна: " + "\n")) {
                prevAnswer = answer;
                bot.setAnswer(prevAnswer);
            }
        }
    }
}
