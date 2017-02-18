package com.github.ac31007_group_8.quiz;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Application configuration.
 *
 * @author Robert T.
 */
@ParametersAreNonnullByDefault
public class Configuration {

    private Configuration() {} // Static

    public static String DATABASE_HOST = "localhost";
    public static int DATABASE_PORT = 3306;
    public static String DATABASE_SCHEMA = "quizsystem";
    public static String DATABASE_USER;
    public static String DATABASE_PASSWORD;

    private static Properties props;

    static void load() {
        props = new Properties();
        File propsFile = new File("config.properties");
        if (propsFile.exists() && propsFile.isFile() && propsFile.canRead()) {
            try {
                FileInputStream inputStream = new FileInputStream(propsFile);
                props.load(inputStream);
                inputStream.close();
            } catch (Exception ex) {
                // This shouldn't ever happen.
                throw new RuntimeException(ex);
            }
        }

        String dbHost = getConfigKeyRaw("DATABASE_HOST");
        if (dbHost != null) DATABASE_HOST = dbHost;

        String dbSchema = getConfigKeyRaw("DATABASE_SCHEMA");
        if (dbSchema != null) DATABASE_SCHEMA = dbSchema;

        String dbPortRaw = getConfigKeyRaw("DATABASE_PORT");
        if (dbPortRaw != null) {
            try {
                int dbPort = Integer.parseInt(dbPortRaw);
                if (dbPort < 1 || dbPort > 65535) {
                    throw new RuntimeException("Invalid port in configuration env var or properties: " + dbPort);
                }
                DATABASE_PORT = dbPort;
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Unable to parse integer for jooq port. Check your env var or properties file!", ex);
            }
        }

        String dbUser = getConfigKeyRaw("DATABASE_USER");
        if (dbUser == null) {
            throw new RuntimeException("No DATABASE_USER provided in configuration.");
        }
        DATABASE_USER = dbUser;

        String dbPassword = getConfigKeyRaw("DATABASE_PASSWORD");
        if (dbPassword == null) {
            throw new RuntimeException("No DATABASE_PASSWORD provided in configuration.");
        }
        DATABASE_PASSWORD = dbPassword;
    }

    private static @Nullable String getConfigKeyRaw(String key) {
        // Check for key in the environment
        String envVar = System.getenv(key);
        if (envVar != null) return envVar;
        // Try for a config file
        return (String)props.get(key);
    }

}
