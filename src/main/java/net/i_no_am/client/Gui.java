package net.i_no_am.client;

import net.i_no_am.command.GuiCommand;
import net.i_no_am.modules.ToggledModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

import static net.i_no_am.client.ClientEntrypoint.*;

public class Gui {
    public static void render(DrawContext context, float ignoredTickDelta) {
        if (!GuiCommand.isGuiEnabled()) return; // Check if GUI rendering is enabled

        // Show all modules in the bottom right corner
        for (int i = 0; i < TOGGLED_MODULES.length; i++) {
            ToggledModule module = TOGGLED_MODULES[TOGGLED_MODULES.length - i - 1];
            String text = String.format("§8[§7%s§8] §r%s§7:§r %s",
                    module.keybind.getBoundKeyLocalizedText().getString(),
                    module.name,
                    module.isEnabled() ? "§aON" : "§cOFF"
            );
            renderTextShadow(context, text, i);
        }
        // Show player name as title
        String playerName = getPlayerName(client.player);
        if (playerName != null) {
            renderTextShadow(context, "§l§3" + playerName + " Modules:" + "§r", TOGGLED_MODULES.length);
        }
    }

    private static String getPlayerName(ClientPlayerEntity player) {
        if (player != null) {
            return player.getName().getString();
        } else {
            return null;
        }
    }

    private static void renderTextShadow(DrawContext context, String text, float index) {
        int x = client.getWindow().getScaledWidth() - client.textRenderer.getWidth(text) - 2;
        int y = client.getWindow().getScaledHeight() - 12 - (int) (index * 11);

        context.drawTextWithShadow(client.textRenderer, text, x, y, 0xFFFFFF);
    }
}
