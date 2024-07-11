import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SQL {

    private static String shopName = "WB";

    public static void createBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                System.out.println("Connection to Store DB succesfull!");
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("opt/java/familyshop.properties"))) {
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, username,password);
    }

    public static void checkId(Long chatId, String userName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                boolean check = false;
                int chatIdFromTokenShop = 0;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tokenshop");
                while (resultSet.next()) {
                    chatIdFromTokenShop = resultSet.getInt("ChatId");
                    if (chatId == chatIdFromTokenShop) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    statement.executeUpdate("INSERT tokenshop(UserName, ChatId) VALUES ('" + userName + "', '" + chatId + "')");
                }

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static String getToken(Long chatId, int choiceShop) {
        String token = null;
        getShopName(choiceShop);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                int chatIdFromTokenShop = 0;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tokenshop");
                while (resultSet.next()) {
                    chatIdFromTokenShop = resultSet.getInt("ChatId");
                    if (chatId == chatIdFromTokenShop) {
                        token = resultSet.getString(shopName);
                        break;
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return token;
    }

    public static void setToken(Long chatId, int choiceShop, String token) {
        getShopName(choiceShop);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("UPDATE tokenshop SET " + shopName + " = \"" + token + "\" WHERE ChatId = " + chatId);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static String getShopName(int shopNumber){
        int shop = shopNumber;
        switch (shop) {
            case (1):
                shopName = "WB";
                break;
            case (2):
                shopName = "WBstats";
                break;
            case (3):
                shopName = "Ozon";
                break;
            case (4):
                shopName = "OzonClient";
                break;
            case (5):
                shopName = "YM";
                break;
            default:
                break;
        }
        return shopName;
    }


    public static ArrayList<Person> getListUsers() {
        ArrayList<Person> usersArrayList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tokenshop");
                while (resultSet.next()) {
                    usersArrayList.add(new Person(resultSet.getString("UserName"), resultSet.getInt("ChatId")));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return usersArrayList;
    }
}


