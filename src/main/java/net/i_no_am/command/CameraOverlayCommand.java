package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.i_no_am.NoOneMod.PREFIX;

public class CameraOverlayCommand {
    private static boolean overlayEnabled = false; // Default to false

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("camera_overlay")
                .executes(CameraOverlayCommand::toggleOverlay)
                .then(literal("enable")
                        .executes(context -> {
                            setOverlayEnabled(true);
                            sendFeedback(context, PREFIX + "Overlay is enabled");
                            return 1;
                        })
                )
                .then(literal("disable")
                        .executes(context -> {
                            setOverlayEnabled(false);
                            sendFeedback(context, PREFIX + "Overlay is disabled");
                            return 1;
                        })
                )
        );
    }

    private static int toggleOverlay(CommandContext<FabricClientCommandSource> context) {
        overlayEnabled = !overlayEnabled; // Toggle overlay rendering status
        String status = overlayEnabled ? "enabled" : "disabled";
        sendFeedback(context, PREFIX + "Overlay is now " + status);
        return 1;
    }

    private static void setOverlayEnabled(boolean enabled) {
        overlayEnabled = enabled; // Set overlay rendering status
    }

    public static boolean isOverlayEnabled() {
        return overlayEnabled; // Getter method for overlay rendering status
    }

    private static void sendFeedback(CommandContext<FabricClientCommandSource> context, String message) {
        context.getSource().sendFeedback(Text.of(message));
    }
}
