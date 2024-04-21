package net.i_no_am.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerUsernameChecker {

    static boolean checked = false;

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
            System.err.println("Error fetching allowed usernames. Exiting game. Contact I-No-oNe on Discord: https://discord.com/users/1051897115447660697");
            client.scheduleStop();
            return;
        }

        if (!allowedUsernames.contains(playerName)) {
            System.out.println("Player username not found in the list of allowed usernames. Exiting game. Contact I-No-oNe on Discord: https://discord.com/users/1051897115447660697");
            client.scheduleStop();
        } else {
            System.out.println("Player username found in the list of allowed usernames.");
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
