import com.sun.scenario.Settings;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
            if (text.equals("/setting")) {
                setting(chatId);
            }
            if (text.equals("/start")) {
                setAnswer(msg.getChatId(), userName, "Добро пожаловать в сервис поиска бесплатных окон для поставок на склады Wildberries! Ты можешь сам настроить список складов, а я буду присылать тебе уведомления, если найду там свободные окна. Чтобы посмотреть и настроить список складов нажми на кнопку \"Menu\" и выбери раздел \"Настройки\".");
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
                setting(chatId);
            }
            for (Warehouse wh: WarehouseSearch.getWarehouseArrayList()) {
                if (text.equals(wh.getColumn())) {
                    update(chatId, wh.getColumn());
                }
            }
        }
    }

    private void update(String chatId, String column) {
        SQL.update(chatId, column, SQL.getWarehouseValue(chatId, column));
        setting(chatId);
    }

    // Шаг "Выбор действия"
    private void setting(String chatId) {
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

    public void setAnswer(ArrayList<Warehouse> warehouseArrayList) {
        ArrayList<Person> personArrayList = getListUsers();
        for (Person p: personArrayList) {
            ArrayList<String> stringArrayList = SQL.getListWarehouses(String.valueOf(p.getChatId()));
            if (!stringArrayList.isEmpty()) {
                String s = "";
                for (String st: stringArrayList) {
                    for (Warehouse wh: warehouseArrayList) {
                        if (st.equals(wh.getName())) {
                            if (!wh.getDates().isEmpty()) {
                                s = s + "\n" + "\n" + wh.getName();
                                for (String day: wh.getDates()) {
                                    s = s + "\n" + day;
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