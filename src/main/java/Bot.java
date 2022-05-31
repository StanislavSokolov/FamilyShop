import com.sun.scenario.Settings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public final class Bot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    //Настройки по умолчанию
    static Settings defaultSettings;
    //
    private URL generetedURL;
    String response;

    /**
     * Настройки файла для разных пользователей. Ключ - уникальный id чата
     */

    private static Map<Long, Settings> userSettings;
    private Long chatId;
    private String userName;
    private String text;

    // Цена установлена setPrice = 1
    private int setPrice = 0;
    // Скидка установлена setDiscount = 1
    private int setDiscount = 0;
    // Промокод установлен setPromocode = 1
    private int setPromocode = 0;

    // Получая значения при изменении цен, скидок и промокодов пишем их в newPrice, newDiscount, newPromocode
    private int newPrice = 0;
    private int newDiscount = 0;
    private int newPromocode = 0;

    // Получая данные с нажатой кнопки пишем их в data
    private String data;

    // Выбор магазина: 0 - магазин не выбран; 1 - Wildberries; 2 - Ozon; 3 - Яндекс.Маркет
    private int choiceShop = 0;

    // Текущий токен для обращения к API
    private String token;

    // Флаг ввода токена
    private int setToken = 0;

    boolean coincidence = false;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        userSettings = new HashMap<>();
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId();
            userName = getUserName(message);
            text = message.getText();
            System.out.print("chatId: " + chatId + "; ");
            System.out.print("userName: " + userName + "; ");
            System.out.println("text: " + text + ";");
            if (text.equals("/start")) {
                setToken = 0;
                checkChatId(chatId, userName);
                choiceShop();
            } else if (text.equals("/change")) {
                setToken = 2;
                checkChatId(chatId, userName);
                choiceShop();
            } else if ((setPrice == 0) & (setDiscount == 0) & (setPromocode == 0) & (choiceShop == 0) & (setToken == 0)) {
                setToken = 0;
                checkChatId(chatId, userName);
                choiceShop();
            } else if ((setPrice == 0) & (setDiscount == 0) & (setPromocode == 0) & (choiceShop != 0) & (setToken == 1)) {
                setToken(chatId, choiceShop, text);
                checkToken(choiceShop);
            } else if ((setPrice == 0) & (setDiscount == 0) & (setPromocode == 0) & (choiceShop != 0) & (setToken == 2)) {
                setToken(chatId, choiceShop, text);
                choiceShop = choiceShop + 1;
                checkToken(choiceShop);
            } else if ((setPrice == 0) & (setDiscount == 0) & (setPromocode == 0) & (choiceShop != 0) & (setToken == 3)) {
                setToken(chatId, choiceShop, text);
                setToken = 0;
                choiceShop();
            } else if ((setPrice == 1) & (setDiscount == 0) & (setPromocode == 0)) {
                setAnswer(chatId, userName, "Выберите товар из Вашего каталога");
            } else if ((setPrice == 2) & (setDiscount == 0) & (setPromocode == 0)) {
                int newValue = parseInt(text);
                if (newValue == -1) {
                } else {
                    if ((choiceShop == 1) | (choiceShop == 2)) {
                        setAnswer(chatId, userName, "Установите скидку для " + data);
                        setDiscount = 1;
                        newPrice = newValue;
                    } else if ((choiceShop == 3) | (choiceShop == 4)) {
                        setPrice = 0;
                        setDiscount = 0;
                        setPromocode = 0;
                        newPrice = newValue;
                        int method = 4;
                        choiceShop = 3;
                        generetedURL = URLRequestResponse.generateURL(choiceShop, method, "0");
                        try {
                            response = URLRequestResponse.getResponseFromURL(generetedURL, getToken(chatId, 3), getToken(chatId, 4), method, data, String.valueOf(newPrice));
                            setAnswer(chatId, userName, "Цена установлена");
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }

                    }
                }
            } else if ((setPrice == 2) & (setDiscount == 1) & (setPromocode == 0)) {
                int newValue = parseInt(text);
                if (newValue == -1) {

                } else {
                    setAnswer(chatId, userName, "Установите промокод для " + data);
                    setPromocode = 1;
                    newDiscount = newValue;
                }

            } else if ((setPrice == 2) & (setDiscount == 1) & (setPromocode == 1)) {
                int newValue = parseInt(text);
                if (newValue == -1) {

                } else {
                    newPromocode = newValue;
                    setPrice = 0;
                    setDiscount = 0;
                    setPromocode = 0;
                    int method = 2;
                    generetedURL = URLRequestResponse.generateURL(2, method, getToken(chatId, choiceShop));
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, "nmId", data, "price", newPrice, getToken(chatId, choiceShop));
                        setAnswer(chatId, userName, "Цена установлена");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    method = 3;
                    generetedURL = URLRequestResponse.generateURL(2, method, getToken(chatId, choiceShop));
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, "nm", data, "discount", newDiscount, getToken(chatId, choiceShop));
                        setAnswer(chatId, userName, "Скидка установлена");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    method = 4;
                    generetedURL = URLRequestResponse.generateURL(2, method, getToken(chatId, choiceShop));
                }
            } else {

            }
        } else if (update.hasCallbackQuery()) {
            data = update.getCallbackQuery().getData();
            if (data.equals("Остатки")) {
                String answerString = "";
                if ((choiceShop == 1) | (choiceShop == 2)) {
                    choiceShop = 2;
                    int method = 5;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, getToken(chatId, choiceShop));
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                    ArrayList<ItemShop> itemShops = new ArrayList<ItemShop>();
                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                        if (itemShops.isEmpty()){
                            itemShops.add(new ItemShop(jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayToClient").toString()),
                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString()),
                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString()));
                        } else {
                            for (ItemShop itemShopCurrent : itemShops) {
                                if (itemShopCurrent.getNmId() == parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
                                    itemShopCurrent.setQuantity(itemShopCurrent.getQuantity() + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()));
                                    itemShopCurrent.setQuantityFull(itemShopCurrent.getQuantityFull() + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()));
                                    itemShopCurrent.setInWayToClient(itemShopCurrent.getInWayToClient() + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayToClient").toString()));
                                    itemShopCurrent.setInWayFromClient(itemShopCurrent.getInWayFromClient() + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString()));
                                    coincidence = true;
                                }
                            }
                            if (!coincidence) {
                                itemShops.add(new ItemShop(jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayToClient").toString()),
                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString()),
                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString()));
                            }
                            coincidence = false;
                        }
                    }

                    method = 1;
                    token = getToken(chatId, 1);
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, getToken(chatId, 1));
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, token);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    System.out.println(response);
                    jsonObject = new JSONObject("{\"price\":" + response + "}");
                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                        for (ItemShop itemShopCurrent : itemShops) {
                            if (itemShopCurrent.getNmId() == parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
                                itemShopCurrent.setPrice(parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("price").toString()));
                                itemShopCurrent.setDiscount(parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("discount").toString()));
                                itemShopCurrent.setPromoCode(parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("promoCode").toString()));
                            }
                        }
                    }

                    int total = 0;

                    for (ItemShop itemShopCurrent: itemShops) {
                        int money = itemShopCurrent.getQuantity()*itemShopCurrent.getPrice()*(100 - itemShopCurrent.getDiscount())*68/10000;
                        total = total + money;
                        answerString = answerString
                                + "Наименование: "
                                + itemShopCurrent.getSubject()
                                + "\n"
                                + "Артикул: "
                                + itemShopCurrent.getSupplierArticle()
                                + "\n"
                                + "Цена: "
                                + itemShopCurrent.getPrice()
                                + "\n"
                                + "Скидка: "
                                + itemShopCurrent.getDiscount()
                                + "\n"
                                + "Промокод: "
                                + itemShopCurrent.getPromoCode()
                                + "\n"
                                + "Остаток на складе: "
                                + itemShopCurrent.getQuantity()
                                + "\n"
                                + "Товары в пути: "
                                + (itemShopCurrent.getQuantityFull() - itemShopCurrent.getQuantity())
                                + " (к клиенту - "
                                + itemShopCurrent.getInWayToClient()
                                + "; от клиента - "
                                + itemShopCurrent.getInWayFromClient()
                                + ")"
                                + "\n"
                                + "Ожидаемая выручка: "
//                            + ((itemShopCurrent.getQuantity()*itemShopCurrent.getPrice()*itemShopCurrent.getDiscount()/100)*80/100)
                                + money
                                + "\n"
                                + "\n";
                    }
                    answerString = answerString
                            + "Товаров на складе на сумму: "
                            + total;
                } else if ((choiceShop == 3) | (choiceShop == 4)) {
                    int method = 1;
                    choiceShop = 3;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, "0");
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, getToken(chatId, 3), getToken(chatId, 4), method, "0", "0");
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject.get("result")));
                    method = 2;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, "0");
                    for (int i = 0; i < jsonObject1.getJSONArray("items").length(); i++) {
                        System.out.println(jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id"));
                        try {
                            response = URLRequestResponse.getResponseFromURL(generetedURL, getToken(chatId, 3), getToken(chatId, 4), method, jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id").toString(), "0");
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObject2 = new JSONObject(response);
                        JSONObject jsonObject3 = new JSONObject(String.valueOf(jsonObject2.get("result")));
                        JSONObject jsonObject4 = new JSONObject(String.valueOf(jsonObject3.get("stocks")));
                        answerString = answerString
                                + "Наименование: "
                                + jsonObject3.get("name").toString()
                                + "\n"
                                + "Артикул: "
                                + jsonObject3.get("id").toString()
                                + "\n"
                                + "Цена: "
                                + jsonObject3.get("old_price").toString()
                                + "\n"
                                + "С учетом скидки: "
                                + jsonObject3.get("price").toString()
                                + "\n"
                                + "Остаток на складе: "
                                + jsonObject4.get("present").toString()
                                + "\n"
                                + "Товары в пути: "
                                + jsonObject4.get("reserved").toString()
                                + "\n"
                                + "Ожидаемая выручка: "
                                + "\n"
                                + "\n";
                    }
                }
                setAnswer(chatId, userName, answerString);
                setPrice = 0;
                setDiscount = 0;
                setPromocode = 0;
                choiceAct();
            } else if (data.equals("Продажи")) {
                String answerString = "";
                if ((choiceShop == 1) | (choiceShop == 2)) {
                    int method = 6;
                    choiceShop = 2;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, getToken(chatId, choiceShop));
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, token);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                    answerString = "ПРОДАЖИ ЗА СЕГОДНЯ:" + "\n";
                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                        answerString = answerString
                                + "\n"
                                + "Наименование: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString()
                                + "\n"
                                + "Артикул: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString()
                                + "\n"
                                + "Количество: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()
                                + "\n"
                                + "Цена: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString()
                                + "\n"
                                + "Вознаграждение: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString()
                                + "\n"
                                + "Склад отгрузки: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString()
                                + "\n"
                                + "Регион доставки: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString()
                                + "\n"
                                + "Дата: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString()
                                + "\n";
                    }
                    method = 7;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, getToken(chatId, choiceShop));
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, token);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    jsonObject = new JSONObject("{\"price\":" + response + "}");
                    answerString = answerString + "\n" + "ЗАКАЗЫ ЗА СЕГОДНЯ:" + "\n";
                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                        answerString = answerString
                                + "\n"
                                + "Наименование: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString()
                                + "\n"
                                + "Артикул: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString()
                                + "\n"
                                + "Количество: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()
                                + "\n"
                                + "Цена: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString()
                                + "\n"
                                + "Склад отгрузки: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString()
                                + "\n"
                                + "Регион доставки: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("oblast").toString()
                                + "\n"
                                + "Дата: "
                                + jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString()
                                + "\n";
                    }
                } else if ((choiceShop == 3) | (choiceShop == 4)) {
                    int method = 3;
                    choiceShop = 3;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, "0");
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, getToken(chatId, 3), getToken(chatId, 4), method, "0", "0");
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject5 = new JSONObject(response);
                    answerString = "ПРОДАЖИ ЗА СЕГОДНЯ:" + "\n";
                    for (int i = 0; i < jsonObject5.getJSONArray("result").length(); i++) {
                        JSONArray jsonArray = (JSONArray) jsonObject5.getJSONArray("result").getJSONObject(i).get("products");
                        JSONObject jsonObject6 = new JSONObject(String.valueOf(jsonObject5.getJSONArray("result").getJSONObject(i).get("analytics_data")));
                        answerString = answerString
                                + "\n"
                                + "Наименование: "
                                + jsonArray.getJSONObject(0).get("name").toString()
                                + "\n"
                                + "Артикул: "
                                + jsonArray.getJSONObject(0).get("offer_id").toString()
                                + "\n"
                                + "Количество: "
                                + jsonArray.getJSONObject(0).get("quantity").toString()
                                + "\n"
                                + "Цена: "
                                + jsonArray.getJSONObject(0).get("price").toString()
                                + "\n"
                                + "Склад отгрузки: "
                                + jsonObject6.getString("warehouse_name")
                                + "\n"
                                + "Регион доставки: "
                                + jsonObject6.getString("region") + " (" + jsonObject6.getString("city") + ")"
                                + "\n"
                                + "Дата: "
                                + jsonObject5.getJSONArray("result").getJSONObject(i).get("created_at").toString()
                                + "\n";
                    }

                }


                setAnswer(chatId, userName, answerString);
                setPrice = 0;
                setDiscount = 0;
                setPromocode = 0;
                choiceAct();
            } else if (data.equals("Цена")) {
                SendMessage sendMessage = null;
                if ((choiceShop == 1) | (choiceShop == 2)) {
                    int method = 1;
                    choiceShop = 2;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, getToken(chatId, choiceShop));
                    choiceShop = 1;
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, getToken(chatId, choiceShop));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
//                System.out.println(response);
                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
//                System.out.println(jsonObject.toString());
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//                List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                    List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                    sendMessage = new SendMessage();
                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                        inlineKeyboardButton.setText(String.valueOf(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId")));
                        inlineKeyboardButton.setCallbackData(String.valueOf(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId")));
                        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                        keyboardButtonsRow.add(inlineKeyboardButton);
                        rowList.add(keyboardButtonsRow);
                        inlineKeyboardMarkup.setKeyboard(rowList);
                    }
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setText("Выберите артикул для изменения цены. Для отмены действия отправьте команду /start");
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                } else if ((choiceShop == 3) | (choiceShop == 4)) {
                    int method = 1;
                    choiceShop = 3;
                    generetedURL = URLRequestResponse.generateURL(choiceShop, method, "0");
                    try {
                        response = URLRequestResponse.getResponseFromURL(generetedURL, getToken(chatId, 3), getToken(chatId, 4), method, "0", "0");
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject.get("result")));

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                    sendMessage = new SendMessage();
                    for (int i = 0; i < jsonObject1.getJSONArray("items").length(); i++) {
                        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                        inlineKeyboardButton.setText(String.valueOf(jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id")));
                        inlineKeyboardButton.setCallbackData(String.valueOf(jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id")));
                        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                        keyboardButtonsRow.add(inlineKeyboardButton);
                        rowList.add(keyboardButtonsRow);
                        inlineKeyboardMarkup.setKeyboard(rowList);
                    }
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setText("Выберите артикул для изменения цены. Для отмены действия отправьте команду /start");
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                }

                setAnswer(sendMessage);
                setPrice = 1;
                setDiscount = 0;
                setPromocode = 0;
            } else if (data.equals("Вернуться к выбору магазина")){
                choiceShop();
            } else if (data.equals("Wildberries")){
                choiceShop = 1;
                checkToken(choiceShop);
            } else if (data.equals("Ozon")){
                choiceShop = 3;
                checkToken(choiceShop);
            } else if (data.equals("Яндекс.Маркет")){
                choiceShop = 4;
                checkToken(choiceShop);
            } else {
                if ((setPrice == 1) & (setDiscount == 0) & (setPromocode == 0)) {
                    setAnswer(chatId, userName, "Установите цену для " + data);
                    setPrice = 2;
                }
            }
        } else {
            System.out.println("X3");
        }
    }

    private void checkToken(int choiceShop) {
        if ((setToken == 0) | (setToken == 1)) {
            token = getToken(chatId, choiceShop);
            switch (choiceShop) {
                case (1):
                    if (token == null) {
                        setAnswer(chatId, userName, "Введите токен для вашего магазина на" + data + " из раздела \"Доступ к новому API\" в личном кабинете");
                        setToken = 1;
                    } else {
                        this.choiceShop = 2;
                        token = getToken(chatId, this.choiceShop);
                        if (token == null) {
                            setAnswer(chatId, userName, "Введите ключ для работы с API статистики x64 для вашего магазина на" + data + " из раздела \"Доступ к API\" в личном кабинете");
                            setToken = 1;
                        } else choiceAct();
                    }
                    break;
                case (2):
                    if (token == null) {
                        setAnswer(chatId, userName, "Введите ключ для работы с API статистики x64 для вашего магазина на" + data + " из раздела \"Доступ к API\" в личном кабинете");
                        setToken = 1;
                    } else choiceAct();
                    break;
                case (3):
                    if (token == null) {
                        setAnswer(chatId, userName, "Введите токен для вашего магазина на " + data + " из раздела \"Настройки\" в личном кабинете");
                        setToken = 1;
                    } else {
                        this.choiceShop = 4;
                        token = getToken(chatId, this.choiceShop);
                        if (token == null) {
                            setAnswer(chatId, userName, "Введите идентификатор клиента для вашего магазина на " + data + " из раздела \"Настройки\" в личном кабинете");
                            setToken = 1;
                        } else choiceAct();
                    }
                    break;
                case (4):
                    if (token == null) {
                        setAnswer(chatId, userName, "Введите идентификатор клиента " + data + " из раздела \"Настройки\" в личном кабинете");
                        setToken = 1;
                    } else choiceAct();
                    break;
                default:
                    break;
            }
        } else if ((setToken == 2) | (setToken == 3)) {
            switch (choiceShop) {
                case (1):
                    setAnswer(chatId, userName, "Введите токен для вашего магазина на " + data + " из раздела \"Доступ к новому API\" в личном кабинете");
                    break;
                case (2):
                    this.choiceShop = 2;
                    setAnswer(chatId, userName, "Введите ключ для работы с API статистики x64 для вашего магазина на " + data + " из раздела \"Доступ к API\" в личном кабинете");
                    setToken = 3;
                    break;
                case (3):
                    setAnswer(chatId, userName, "Введите токен для вашего магазина на " + data + " из раздела \"Настройки\" в личном кабинете");
                    break;
                case (4):
                    this.choiceShop = 4;
                    setAnswer(chatId, userName, "Введите идентификатор клиента " + data + " из раздела \"Настройки\" в личном кабинете");
                    setToken = 1;
                    break;
                default:
                    break;
            }

        }

    }

    // Шаг "Выбор магазина"
    private void choiceShop(){
        setPrice = 0;
        setDiscount = 0;
        setPromocode = 0;
        choiceShop = 0;
//        setToken = 0;
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Wildberries");
        inlineKeyboardButton.setCallbackData("Wildberries");
        inlineKeyboardButton1.setText("Ozon");
        inlineKeyboardButton1.setCallbackData("Ozon");
        inlineKeyboardButton2.setText("Яндекс.Маркет");
        inlineKeyboardButton2.setCallbackData("Яндекс.Маркет");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        keyboardButtonsRow.add(inlineKeyboardButton1);
        keyboardButtonsRow.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("Выберите магазин для управления");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        setAnswer(sendMessage);
    }
    // Шаг "Выбор действия"
    private void choiceAct(){
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Остатки");
        inlineKeyboardButton.setCallbackData("Остатки");
        inlineKeyboardButton1.setText("Продажи");
        inlineKeyboardButton1.setCallbackData("Продажи");
        inlineKeyboardButton2.setText("Цена");
        inlineKeyboardButton2.setCallbackData("Цена");
        inlineKeyboardButton3.setText("Вернуться к выбору магазина");
        inlineKeyboardButton3.setCallbackData("Вернуться к выбору магазина");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        keyboardButtonsRow.add(inlineKeyboardButton1);
        keyboardButtonsRow.add(inlineKeyboardButton2);
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("Выберите действие");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        setAnswer(sendMessage);
    }

    // Проверяем идентификатор чата в базе данных
    // Если пользователь новый, то добавляем запись в базе данных
    private void checkChatId(Long chatId, String userName){
        SQL.checkId(chatId, userName);
    }

    // Проверяем токен в базе данных
    // Если пользователь еще не ввел токен для выбранного магазина
    // Напрявляем его к методу setToken();
    private String getToken(Long chatId, int choiceShop){
        return SQL.getToken(chatId, choiceShop);
    }

    // Устанавливаем введенное пользователем значение токена
    private void setToken(Long chatId, int choiceShop, String token){
        SQL.setToken(chatId, choiceShop, token);
    }

    private void setAnswer(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены, используются настройки по умолчанию
     */
    public static Settings getUserSettings(Long chatId) {
        Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return defaultSettings;
        }
        return settings;
    }

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param userName имя пользователя
     * @param text текст ответа
     */
    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
        }
    }
}

