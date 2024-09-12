import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static java.lang.Integer.parseInt;

public class WarehouseSearch extends Thread {
    
    Bot bot;

    public WarehouseSearch(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {

        super.run();
        while (true) {
            try {
                search();
                sleep(15000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void search() {
        URL generetedURL = null;
        String response = null;
        generetedURL = URLRequestResponse.generateURL("wb","warehouses", "");
        try {
            response = URLRequestResponse.getResponseFromURL(generetedURL, SQL.getToken("SOKOL0VE"));
            System.out.println(response);
//            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                JSONObject jsonObject1 = new JSONObject(response);
//                JSONObject jsonObject = jsonObject1.getJSONObject("data");
//                for (int i = 0; i < jsonObject.getJSONArray("listGoods").length(); i++) {
//                    List<Product> products = user.getProducts();
//                    if (products.isEmpty()) {
//                        Product product = new Product("",
//                                jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString(),
//                                "",
//                                parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).getJSONArray("sizes").getJSONObject(0).get("price").toString()),
//                                parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("discount").toString()),
//                                "wb", "", "", user);
//                        session.save(product);
//                    } else {
//                        boolean coincidence = false;
//                        for (Product p : products) {
//                            if (p.getNmId().equals(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString())) {
//                                session.createQuery("update Product set price = "
//                                        + parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).getJSONArray("sizes").getJSONObject(0).get("price").toString())
//                                        + " WHERE nmId = '" + jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString() + "'").executeUpdate();
//                                session.createQuery("update Product set discount = "
//                                        + parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("discount").toString())
//                                        + " WHERE nmId = '" + jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString() + "'").executeUpdate();
//
//                                coincidence = true;
//                            }
//                        }
//                        if (!coincidence) {
//                            Product product = new Product("",
//                                    jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString(),
//                                    "",
//                                    parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).getJSONArray("sizes").getJSONObject(0).get("price").toString()),
//                                    parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("discount").toString()),
//                                    "wb", "", "", user);
//                            session.save(product);
//                        }
//                    }
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
}
