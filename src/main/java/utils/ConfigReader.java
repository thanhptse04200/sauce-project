package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("Cannot find config.properties");
            }

            properties.load(input);

        } catch (IOException e) {

            throw new RuntimeException("Cannot load config.properties", e);

        }

    }

    private ConfigReader() {
    }

    public static String get(String key) {

        return properties.getProperty(key);

    }

    public static int getInt(String key) {

        return Integer.parseInt(properties.getProperty(key));

    }

    public static boolean getBoolean(String key) {

        return Boolean.parseBoolean(properties.getProperty(key));

    }

}