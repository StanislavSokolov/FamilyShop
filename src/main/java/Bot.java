import com.sun.scenario.Settings;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    /**
     * Настройки файла для разных пользователей. Ключ - уникальный id чата
     */

    private static Map<Long, String> userSettings;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        userSettings = new HashMap<>();
        ArrayList<Person> personArrayList = SQL.getListUsers();
        for (Person p: personArrayList) {
            userSettings.put((long) p.getChatId(), "");
        }
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
            Message msg = update.getMessage();
            String text = msg.getText();
            String chatId = msg.getChatId().toString();
            String userName = getUserName(msg);
            if (!checkChatId(msg.getChatId(), userName)) {
                userSettings.put(msg.getChatId(), "");
            }
            if (text.equals("/warehouses")) {
                warehouses(chatId);
            }
            if (text.equals("/coefficients")) {
                coefficients(chatId);
            }
            if (text.equals("/start")) {
                setAnswer(msg.getChatId(), userName, "Добро пожаловать в сервис поиска бесплатных окон для поставок на склады Wildberries! Ты можешь сам настроить список складов и коэффициенты приемки, а я буду присылать тебе уведомления, если найду там подходящие окна. Чтобы посмотреть и настроить список складов  и коэффициенты приемки нажми на кнопку \"Menu\" и выбери соответствующий раздел.");
            }
        }
        else if (update.hasCallbackQuery()) {
            String text = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            if (text.equals("add")) {
                add(chatId);
            }
            if (text.equals("remove")) {
                remove(chatId);
            }
            if (text.equals("back")) {
                warehouses(chatId);
            }
            for (Warehouse wh: WarehouseSearch.getWarehouseArrayList()) {
                if (text.equals(wh.getColumn())) {
                    updateListWarehouses(chatId, wh.getColumn());
                }
            }
            if (text.equals("free") || text.equals("x1") || text.equals("x2") || text.equals("x3") || text.equals("x5") || text.equals("x10")) {
                updateCoefficient(chatId, text);
            }
        }
    }

    private void coefficients(String chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButtonFree = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonX1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonX2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonX3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonX5 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonX10 = new InlineKeyboardButton();
        inlineKeyboardButtonFree.setText("Бесплатно");
        inlineKeyboardButtonFree.setCallbackData("free");
        inlineKeyboardButtonX1.setText("x1");
        inlineKeyboardButtonX1.setCallbackData("x1");
        inlineKeyboardButtonX2.setText("x2");
        inlineKeyboardButtonX2.setCallbackData("x2");
        inlineKeyboardButtonX3.setText("x3");
        inlineKeyboardButtonX3.setCallbackData("x3");
        inlineKeyboardButtonX5.setText("x5");
        inlineKeyboardButtonX5.setCallbackData("x5");
        inlineKeyboardButtonX10.setText("x10");
        inlineKeyboardButtonX10.setCallbackData("x10");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButtonFree);
        keyboardButtonsRow.add(inlineKeyboardButtonX1);
        keyboardButtonsRow.add(inlineKeyboardButtonX2);
        keyboardButtonsRow.add(inlineKeyboardButtonX3);
        keyboardButtonsRow.add(inlineKeyboardButtonX5);
        keyboardButtonsRow.add(inlineKeyboardButtonX10);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        String text = "Выберите коэффициент, до которого необходимо вести поиск окон для поставки:";
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.enableHtml(true);
        setAnswer(sendMessage);
    }

    private void updateListWarehouses(String chatId, String column) {
        SQL.update(chatId, column, SQL.getWarehouseValue(chatId, column));
        warehouses(chatId);
    }

    private void updateCoefficient(String chatId, String coefficient) {
        int coef = 0;
        if (!coefficient.equals("free") & !coefficient.equals("x10")) coef = Integer.parseInt(coefficient.substring(1, 2));
        if (coefficient.equals("x10")) coef = Integer.parseInt(coefficient.substring(1, 3));
        for (Warehouse wh: SQL.getListWarehousesToRemove(chatId)) {
            SQL.update(chatId, wh.getColumn(), coef);
        }
        if (coef == 0) setAnswer(Integer.parseInt(chatId), "Только бесплатные приемки");
        else setAnswer(Integer.parseInt(chatId), "Установлен коэффициент " + coefficient);
        ArrayList<Person> p = SQL.getUserName(chatId);
        System.out.println(p.get(0).getUserName() + " " + "установил(а) коэффициент" + " " + "x" + coef);
    }

    // Шаг "Выбор действия"
    private void warehouses(String chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButtonAdd = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButtonRemove = new InlineKeyboardButton();
        inlineKeyboardButtonAdd.setText("Добавить");
//        inlineKeyboardButtonAdd.setUrl("https://core.telegram.org/bots/api#answerwebappquery");
        inlineKeyboardButtonAdd.setCallbackData("add");
        inlineKeyboardButtonRemove.setText("Убрать");
        inlineKeyboardButtonRemove.setCallbackData("remove");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButtonAdd);
        keyboardButtonsRow.add(inlineKeyboardButtonRemove);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        ArrayList<String> stringArrayList = SQL.getListWarehouses(chatId);
        String text = "В вашем списке нет складов для отслеживания. Добавьте их." + "\n" + "\n" + "Выберите действие:";
        if (!stringArrayList.isEmpty()) {
            text = "Список складов для отслеживания:" + "\n";
            for (String s: stringArrayList) text = text + "\n" + s;
            text = text + "\n" + "\n" + "Выберите действие:";
        }
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.enableHtml(true);
        setAnswer(sendMessage);
    }

    private void add(String chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        ArrayList<Warehouse> warehousesArrayList = SQL.getListWarehousesToAdd(chatId);
        String text = "";
        if (!warehousesArrayList.isEmpty()) {
            for (Warehouse wh: warehousesArrayList) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(wh.getName());
                inlineKeyboardButton.setCallbackData(wh.getColumn());
                List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                keyboardButtonsRow.add(inlineKeyboardButton);
                rowList.add(keyboardButtonsRow);
            }
            text = "Выберите склад для добавления";
        } else {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Назад");
            inlineKeyboardButton.setCallbackData("back");
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
            text = "Все необходимые склады уже добавлены для отслеживания";
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.enableHtml(true);
        setAnswer(sendMessage);
    }

    private void remove(String chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        ArrayList<Warehouse> warehousesArrayList = SQL.getListWarehousesToRemove(chatId);
        String text = "";
        if (!warehousesArrayList.isEmpty()) {
            for (Warehouse wh: warehousesArrayList) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(wh.getName());
                inlineKeyboardButton.setCallbackData(wh.getColumn());
                List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                keyboardButtonsRow.add(inlineKeyboardButton);
                rowList.add(keyboardButtonsRow);
            }
            text = "Выберите склад для удаления";
        } else {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Назад");
            inlineKeyboardButton.setCallbackData("back");
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
            text = "Все склады удалены для отслеживания";
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.enableHtml(true);
        setAnswer(sendMessage);
    }


    // Проверяем идентификатор чата в базе данных
    // Если пользователь новый, то добавляем запись в базе данных
    private boolean checkChatId(Long chatId, String userName){
        return SQL.checkId(chatId, userName);
    }

    // Проверяем идентификатор чата в базе данных
    // Если пользователь новый, то добавляем запись в базе данных
    private void getUser(Long chatId, String userName){
        SQL.getUser(chatId, userName);
    }

    // Получаем список пользователей из базы данных
    private ArrayList<Person> getListUsers() {
        return SQL.getListUsers();
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

    public void setAnswer(String result) {
        ArrayList<Person> personArrayList = getListUsers();
        for (Person p: personArrayList)
            setAnswer((long) p.getChatId(), p.getUserName(), result);
    }

    public void setAnswer(int chatId, String result) {
        setAnswer((long) chatId, "", result);
    }

    public void setAnswer() {
        ArrayList<Person> personArrayList = getListUsers();
        String hello = "Появилась возможность установить новые коэффициенты x5 и x10, до которых включительно будет осуществляться поиск соответствующих окон приемки. Зайдите в \"Меню\" и настройте коэффициенты.";
        for (Person p: personArrayList) {
//            setAnswer((long) p.getChatId(), "xx", hello);
        }
    }

    public void setAnswer(ArrayList<Warehouse> warehouseArrayList) {
        ArrayList<Person> personArrayList = getListUsers();
//        for (Warehouse wh: warehouseArrayList) {
//            for (Date dt: wh.getDates())
//                System.out.println(wh.getName() + dt.getDate() + " " + dt.getCoefficient());
//        }
        for (Person p: personArrayList) {
            ArrayList<Warehouse> warehouses = SQL.getListWarehousesToRemove(String.valueOf(p.getChatId()));
            if (!warehouses.isEmpty()) {
                String s = "";
                for (Warehouse st: warehouses) {
                    for (Warehouse wh: warehouseArrayList) {
                        if (st.getName().equals(wh.getName())) {
                            if (!wh.getDayToSends().isEmpty()) {
                                boolean check = false;
                                for (DayToSend day: wh.getDayToSends()) {
                                    if (day.getCoefficient() <= st.getCoefficient()) {
                                        if (!check) s = s + "\n" + "\n" + wh.getName();
                                        check = true;
                                        if (day.getCoefficient() == 0) s = s + "\n" + day.getDate() + " (Бесплатно)";
                                        else s = s + "\n" + day.getDate() + " (x" + day.getCoefficient() + ")";
                                    }
                                }
                            }
                        }
                    }
                }
                if (!s.equals("")) {
                    if (!userSettings.get((long) p.getChatId()).equals(s)) {
                        System.out.println("\n" + p.getUserName() + s);
                        userSettings.put((long) p.getChatId(), s);
                        setAnswer((long) p.getChatId(), "xx", s);
                    }
                }
            }
        }
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены, используются настройки по умолчанию
     */
//    public static Settings getUserSettings(Long chatId) {
//        Settings settings = userSettings.get(chatId);
//        if (settings == null) {
//            return defaultSettings;
//        }
//        return settings;
//    }

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