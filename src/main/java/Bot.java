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

    // Получая данные с нажатой кнопки пишем их в data
    private String data;

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
            Message msg = update.getMessage();
            String text = msg.getText();
            String chatId = msg.getChatId().toString();
            if (text.equals("/setting")) {
                setting(chatId);
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
            System.out.println(text);
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
        System.out.println("Update " + column);
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
        ArrayList<Warehouse> warehousesArrayList = SQL.getListWarehousesToAdd(chatId);
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        String text = "";
        if (!warehousesArrayList.isEmpty()) {
            for (Warehouse wh: warehousesArrayList) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(wh.getName());
                inlineKeyboardButton.setCallbackData(wh.getColumn());
                keyboardButtonsRow.add(inlineKeyboardButton);
            }
            text = "Выберите склад для добавления";
        } else {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Назад");
            inlineKeyboardButton.setCallbackData("back");
            keyboardButtonsRow.add(inlineKeyboardButton);
            text = "Все необходимые склады уже добавлены для отслеживания";
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
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
        ArrayList<Warehouse> warehousesArrayList = SQL.getListWarehousesToRemove(chatId);
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        String text = "";
        if (!warehousesArrayList.isEmpty()) {
            for (Warehouse wh: warehousesArrayList) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(wh.getName());
                inlineKeyboardButton.setCallbackData(wh.getColumn());
                keyboardButtonsRow.add(inlineKeyboardButton);
            }
            text = "Выберите склад для удаления";
        } else {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Назад");
            inlineKeyboardButton.setCallbackData("back");
            keyboardButtonsRow.add(inlineKeyboardButton);
            text = "Все склады удалены для отслеживания";
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.enableHtml(true);
        setAnswer(sendMessage);
    }


    // Проверяем идентификатор чата в базе данных
    // Если пользователь новый, то добавляем запись в базе данных
    private void checkChatId(Long chatId, String userName){
        SQL.checkId(chatId, userName);
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
        setAnswer((long) 419946022, "xx", result);
    }

    public void setAnswer(ArrayList<Warehouse> warehouseArrayList) {
        ArrayList<String> stringArrayList = SQL.getListWarehouses("419946022");
        if (!stringArrayList.isEmpty()) {
            String s = "Бесплатные окна: " + "\n";
            for (String st: stringArrayList) {
                for (Warehouse wh: warehouseArrayList) {
                    if (st.equals(wh.getName())) {
                        if (!wh.getDates().isEmpty()) {
                            s = s + "\n" + wh.getName();
                            for (String day: wh.getDates()) {
                                s = s + "\n" + day;
                            }
                        }
                    }
                }
            }
            if (!s.equals("Бесплатные окна: " + "\n")) setAnswer((long) 419946022, "xx", s);
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