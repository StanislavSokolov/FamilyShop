import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class FamilyShop {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Найдите в Telegram и протестируйте @FalconFamilyShopBot
            botsApi.registerBot(new Bot("@FalconFamilyShopBot", "token"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        SQL.createBD();
    }
}
