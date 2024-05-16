package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.i_no_am.NoOneMod.PREFIX;
import static net.i_no_am.modules.FlyHack.setJetpackMaxSpeed;

public class FlySpeedCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("fly-speed")
                .then(ClientCommandManager.argument("fly-speed", DoubleArgumentType.doubleArg())
                        .executes(context -> {
                            double speed = DoubleArgumentType.getDouble(context, "fly-speed");
                            setJetpackMaxSpeed(speed);
                            context.getSource().sendFeedback(Text.of(PREFIX + "Elytra speed set to " + speed));
                            return 1;
                        })));
    }
}

