package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.i_no_am.NoOneMod.PREFIX;
import net.i_no_am.modules.FreeCamera;

public class CameraSpeedCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("free_camera_speed")
                .then(literal("set")
                        .then(argument("speed", doubleArg(0.1, 10.0))
                                .executes(context -> setSpeed(context, getDouble(context, "speed"))))
                )
        );
    }

    private static int setSpeed(CommandContext<FabricClientCommandSource> context, double speed) {
        FreeCamera.setSpeed(speed);
        context.getSource().sendFeedback(Text.of(PREFIX  + "Free Camera speed set to " + speed));
        return 1;
    }
}
