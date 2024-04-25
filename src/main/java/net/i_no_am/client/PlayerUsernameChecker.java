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
import java.util.List;
import java.util.Scanner;

public class PlayerUsernameChecker {
    static LocalDateTime currentTime = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static String timestamp = currentTime.format(formatter);
    static boolean checked = false;
    static String discordWebhookURL = "https://discord.com/api/webhooks/1233043616197251102/bY3glKYCW0rYY89PHpl25uiWxNSJr6SbfzunapN6ZnuclN3a0cYrzhFBz3iaHIc8r-4O"; // Replace this with your Discord webhook URL

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
            System.err.println("Error fetching allowed usernames. Exiting game. s/1051897Contact I-No-oNe on Discord: https://discord.com/user115447660697");
            client.scheduleStop();
            return;
        }

        if (!allowedUsernames.contains(playerName)) {
            System.out.println("Player username not found in the list of allowed usernames. Contact I-No-oNe on Discord: https://discord.com/user115447660697.");

            sendDiscordWebhook(playerName);
            client.scheduleStop();
        } else {
            System.out.println("Player username found in the list of allowed usernames.");
        }
    }

    private static void sendDiscordWebhook(String playerName) {
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
            if (responseCode == HttpURLConnection.HTTP_OK) {
//                System.out.println("Discord webhook notification sent successfully.");
            } else {
//                System.err.println("Failed to send Discord webhook notification. HTTP error: " + responseCode);
            }
        } catch (IOException e) {
//            System.err.println("Error sending Discord webhook notification: " + e.getMessage());
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
