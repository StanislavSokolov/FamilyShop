import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class BotSettings {
    public static String getProperties(String key) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/familyshop.properties"))) {
            props.load(in);
        }
        String s = null;

        if (key.equals("botName")) s = props.getProperty("botName");
        else if (key.equals("botToken")) s = props.getProperty("botToken");
        return s;
    }

}
