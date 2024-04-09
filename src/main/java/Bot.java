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

    // Получая данные с нажатой кнопки пишем их в data
    private String data;

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
            if (text.equals("/send")) {
                ArrayList<Person> users = getListUsers();
                if (users != null) {
                    for (Person p: users) {
                        setAnswer((long) p.getChatId(), p.getUserName(), "Уважаемый дольщик, " + p.getUserName() + ", напоминаем, что до завершения строительства ЖК Дефанс и выдачи ключей осталось 28 месяцев. На текущий момент на ремонт у Вас накоплено 0 рублей 00 копеек.");
                    }
                }
            } else {
                setAnswer(chatId, userName, "Такой запрос не обрабатывается");
            }
//                int newValue = parseInt(text);
//                if (newValue == -1) {
        } else if (update.hasCallbackQuery()) {
            data = update.getCallbackQuery().getData();
            if (data.equals("Остатки")) {
                String answerString = "";
                setAnswer(chatId, userName, answerString);
            } else if (data.equals("Продажи")) {
                String answerString = "";
                setAnswer(chatId, userName, answerString);
            }
        }
    }

    // Шаг "Выбор магазина"
    private void choiceShop(){
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

