package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.i_no_am.NoOneMod.PREFIX;

public class DisablerGuiCommand {
    private static boolean guiEnabled = true;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("gui")
                .executes(DisablerGuiCommand::toggleGui)
                .then(literal("enable")
                        .executes(context -> {
                            setGuiEnabled(true);
                            sendFeedback(context, PREFIX +"GUI is enabled");
                            return 1;
                        })
                )
                .then(literal("disable")
                        .executes(context -> {
                            setGuiEnabled(false);
                            sendFeedback(context, PREFIX + "GUI is disabled");
                            return 1;
                        })
                )
        );
    }

    private static int toggleGui(CommandContext<FabricClientCommandSource> context) {
        guiEnabled = !guiEnabled; // Toggle GUI rendering status
        String status = guiEnabled ? "enabled" : "disabled";
        sendFeedback(context, "GUI is now " + status);
        return 1;
    }

    private static void setGuiEnabled(boolean enabled) {
        guiEnabled = enabled; // Set GUI rendering status
    }

    public static boolean isGuiEnabled() {
        return guiEnabled; // Getter method for GUI rendering status
    }

    private static void sendFeedback(CommandContext<FabricClientCommandSource> context, String message) {
        context.getSource().sendFeedback(Text.literal(message));
    }
}
