import com.sun.scenario.Settings;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
            Long chatId = msg.getChatId();
            if (text.equals("/setting")) {

            }
        }
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            String text = msg.getText();
            Long chatId = msg.getChatId();
            if (text.equals("/setting")) {

            }
        }
        if (update.hasCallbackQuery()) {

        }
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