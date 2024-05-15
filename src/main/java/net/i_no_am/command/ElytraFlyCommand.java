package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.i_no_am.modules.ElytraFly.*;
import static net.i_no_am.NoOneMod.PREFIX;

public class ElytraFlyCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("elytra-speed")
                .then(ClientCommandManager.argument("speed", FloatArgumentType.floatArg())
                        .executes(context -> {
                            float speed = FloatArgumentType.getFloat(context, "speed");
                            setSpeed(speed);
                            context.getSource().sendFeedback(Text.of(PREFIX + "Elytra speed set to " + speed));
                            return 1;
                        })));
    }
}