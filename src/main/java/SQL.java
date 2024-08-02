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
        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/familyshop.properties"))) {
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

    public static int getCountOrders() {
        int i = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM item WHERE cdate = '" + URLRequestResponse.getDateCurrent() + "' and status = 'ordered'");
                while (resultSet.next()) {
                     i++;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return i;
    }

    public static int getCountSales() {
        int i = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM item WHERE sdate = '" + URLRequestResponse.getDateCurrent() + "' and status = 'sold'");
                while (resultSet.next()) {
                    i++;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return i;
    }

    public static int getCountCancels() {
        int i = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM item WHERE cdate = '" + URLRequestResponse.getDateCurrent() + "' and status = 'cancelled'");
                while (resultSet.next()) {
                    i++;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return i;
    }

    public static String getMoreInformstionString() {
        ArrayList<Item> itemsArrayList = getItem(getItemsArrayList());
        String item = "";
        if (!itemsArrayList.isEmpty()) {
            itemsArrayList.sort((o1, o2) -> o2.getCount() - o1.getCount());
            for (Item i: itemsArrayList) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = getConnection()) {
                        Statement statement = conn.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE id = " + i.getProduct_id());
                        while (resultSet.next()) {
                            i.setSubject(resultSet.getString("subject"));
                            i.setSupplierArticle(resultSet.getString("supplierArticle"));
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                item = item + i.getSubject() + " (" + i.getSupplierArticle() + "): "
                        + "\n"
                        + "\n"
                        + "Закали: "
                        + i.getCountOrders()
                        + " шт."
                        + "\n"
                        + "Купили: "
                        + i.getCountSales()
                        + " шт."
                        + "\n"
                        + "\n";
            }
        }

        return item;
    }

    public static String getItemOfTheDayString() {
        ArrayList<Item> itemsArrayList = getItem(getItemsArrayList());
        String item = "Не определен";
        if (!itemsArrayList.isEmpty()) {
            itemsArrayList.sort((o1, o2) -> o2.getCount() - o1.getCount());
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                try (Connection conn = getConnection()) {
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE id = " + itemsArrayList.get(0).getProduct_id());
                    while (resultSet.next()) {
                        itemsArrayList.get(0).setSubject(resultSet.getString("subject"));
                        itemsArrayList.get(0).setSupplierArticle(resultSet.getString("supplierArticle"));
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
            item = itemsArrayList.get(0).getSubject() + " (" + itemsArrayList.get(0).getSupplierArticle() + "): "
                    + "\n"
                    + "\n"
                    + "Закали: "
                    + itemsArrayList.get(0).getCountOrders()
                    + " шт."
                    + "\n"
                    + "Купили: "
                    + itemsArrayList.get(0).getCountSales()
                    + " шт.";
        }

        return item;
    }

    public static ArrayList<Item> getItemsArrayList() {
        ArrayList<Item> itemsArrayList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM item WHERE sdate = '" + URLRequestResponse.getDateCurrent() + "' and status = 'sold'");
                while (resultSet.next()) {
                    itemsArrayList.add(new Item(resultSet.getInt("product_id"), resultSet.getString("status")));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM item WHERE cdate = '" + URLRequestResponse.getDateCurrent() + "' and status = 'ordered'");
                while (resultSet.next()) {
                    itemsArrayList.add(new Item(resultSet.getInt("product_id"), resultSet.getString("status")));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return itemsArrayList;
    }

    public static ArrayList<Item> getItem(ArrayList<Item> itemsArrayList) {
        System.out.println(itemsArrayList.size());
        for (Item i: itemsArrayList) {
            System.out.println(i.getProduct_id() + " " + i.getStatus());
        }
        ArrayList<Item> items = new ArrayList<>();
        for (Item i: itemsArrayList) {
            if (items.isEmpty()) {
                i.setCount(1);
                if (i.getStatus().equals("sold")) i.setCountSales(1);
                else if (i.getStatus().equals("ordered")) i.setCountOrders(1);
                items.add(i);
            }
            else {
                boolean coincidence = false;
                for (Item j: items) {
                    if (i.getProduct_id() == j.getProduct_id()) {
                        j.setCount(j.getCount() + 1);
                        if (i.getStatus().equals("sold")) j.setCountSales(j.getCountSales() + 1);
                        else if (i.getStatus().equals("ordered")) j.setCountOrders(j.getCountOrders() + 1);
                        coincidence = true;
                    }
                }
                if (!coincidence) {
                    i.setCount(1);
                    if (i.getStatus().equals("sold")) i.setCountSales(1);
                    else if (i.getStatus().equals("ordered")) i.setCountOrders(1);
                    items.add(i);
                }
            }
        }
        for (Item i: items) {
            System.out.println(i.getCountSales() + " " + i.getCountOrders() + " " + i.getCount());
        }
        return items;
    }
}


