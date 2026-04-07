package top.dontplay.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class SealDiceConfig {
    private static final int DEFAULT_WS_PORT = 8887;
    private static final String KEY_WS_PORT = "websocket.port";
    private static final Path CONFIG_PATH = Paths.get("config", "sealdice2mc.properties");

    private SealDiceConfig() {
    }

    public static synchronized int loadWebSocketPort() {
        Properties properties = new Properties();

        if (Files.exists(CONFIG_PATH)) {
            try (InputStream in = Files.newInputStream(CONFIG_PATH)) {
                properties.load(in);
            } catch (IOException e) {
                System.err.println("Failed to read config file, using default port: " + e.getMessage());
            }
        }

        int port = parsePort(properties.getProperty(KEY_WS_PORT));
        properties.setProperty(KEY_WS_PORT, String.valueOf(port));

        try {
            writeProperties(properties);
        } catch (IOException e) {
            System.err.println("Failed to write config file: " + e.getMessage());
        }

        return port;
    }

    public static synchronized void saveWebSocketPort(int port) throws IOException {
        if (!isValidPort(port)) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }

        Properties properties = new Properties();
        if (Files.exists(CONFIG_PATH)) {
            try (InputStream in = Files.newInputStream(CONFIG_PATH)) {
                properties.load(in);
            }
        }

        properties.setProperty(KEY_WS_PORT, String.valueOf(port));
        writeProperties(properties);
    }

    private static int parsePort(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_WS_PORT;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            return isValidPort(parsed) ? parsed : DEFAULT_WS_PORT;
        } catch (NumberFormatException ignored) {
            return DEFAULT_WS_PORT;
        }
    }

    private static boolean isValidPort(int port) {
        return port >= 1 && port <= 65535;
    }

    private static void writeProperties(Properties properties) throws IOException {
        Path parent = CONFIG_PATH.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
            properties.store(out, "SealDice2MC config");
        }
    }
}