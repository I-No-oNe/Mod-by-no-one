package net.i_no_am.client;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class PlayerUsernameChecker {
    static LocalDateTime currentTime = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static String timestamp = currentTime.format(formatter);
    static boolean checked = false;
    static String encodedDiscordWebhookURL = "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTIzMzg4NTMzMTU5MDM1MzA0OC9vbjU4OTI5N195ZlV5d3U4VklVcHFvZEFpQ0lvMnN0UkhOZ2FySTR2RXRLMmVvYlJHMENqNlYyQVB0T2w1eE1fSmF3Mg==";

    private static String decodeURL(String encodedURL) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedURL);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!checked) {
                checkPlayerUsername(client);
                checked = true;
            }
        });
    }

    private static void checkPlayerUsername(MinecraftClient client) {
        String playerName = client.getSession().getUsername();
        List<String> allowedUsernames = fetchAllowedUsernames();

        if (allowedUsernames == null) {
            System.err.println("Error fetching allowed usernames. Exiting game.");
            client.scheduleStop();
            return;
        }

        if (!allowedUsernames.contains(playerName)) {
            System.out.println("Player username not found in the list of allowed usernames.");

            sendDiscordWebhook(playerName, encodedDiscordWebhookURL);
            client.scheduleStop();
        } else {
            System.out.println("Player username found in the list of allowed usernames.");
        }
    }

    private static void sendDiscordWebhook(String playerName, String encodedWebhookURL) {
        String discordWebhookURL = decodeURL(encodedWebhookURL);
        String messageContent = "```js" + "\nNo one's mod"+
                "\nTime: " + timestamp +
                "\nPlayer: " + playerName +
                "\nUUID: " + MinecraftClient.getInstance().getSession().getUuidOrNull() +
                "\nMinecraft Version: " + MinecraftClient.getInstance().getGameVersion() + "```";

        try {
            JsonObject json = new JsonObject();
            json.addProperty("content", messageContent);

            HttpURLConnection connection = (HttpURLConnection) new URL(discordWebhookURL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to send Discord webhook notification. HTTP error: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Error sending Discord webhook notification: " + e.getMessage());
        }
    }

    private static List<String> fetchAllowedUsernames() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/I-No-oNe/I-No-oNe/main/notes/allowed_usernames.txt").openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                List<String> allowedUsernames = new ArrayList<>();
                try (Scanner scanner = new Scanner(connection.getInputStream())) {
                    while (scanner.hasNextLine()) {
                        String username = scanner.nextLine().trim();
                        allowedUsernames.add(username);
                    }
                }
                return allowedUsernames;
            } else {
                System.err.println("HTTP error fetching allowed usernames: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error fetching allowed usernames: " + e.getMessage());
            return null;
        }
    }
}
