package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class ClientFakerCommand {
    private static boolean isEnabled = false;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(buildCommand());
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> buildCommand() {
        return LiteralArgumentBuilder
                .<FabricClientCommandSource>literal("vanillaclient")
                .executes(ClientFakerCommand::toggle)
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("enable")
                        .executes(ClientFakerCommand::enable)
                )
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("disable")
                        .executes(ClientFakerCommand::disable)
                );
    }

    private static int toggle(CommandContext<FabricClientCommandSource> context) {
        isEnabled = !isEnabled;
        context.getSource().getPlayer().sendMessage(Text.literal("Vanilla Client is now " + (isEnabled ? "enabled" : "disabled")), false);
        return 1;
    }

    private static int enable(CommandContext<FabricClientCommandSource> context) {
        isEnabled = true;
        context.getSource().getPlayer().sendMessage(Text.literal("Vanilla Client is now enabled"), false);
        return 1;
    }

    private static int disable(CommandContext<FabricClientCommandSource> context) {
        isEnabled = false;
        return 1;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }
}
