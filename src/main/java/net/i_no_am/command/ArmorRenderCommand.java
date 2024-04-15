package net.i_no_am.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.i_no_am.modules.NoArmorRender;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.i_no_am.NoOneMod.PREFIX;

public class ArmorRenderCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            registerSlotCommand(dispatcher, slot);
        }
    }

    private static void registerSlotCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, EquipmentSlot slot) {
        dispatcher.register(literal(slot.getName())
                .executes(context -> toggleRender(context, slot))
                .then(literal("enable")
                        .executes(context -> {
                            setRenderStatus(context, slot, true);
                            return 1;
                        })
                )
                .then(literal("disable")
                        .executes(context -> {
                            setRenderStatus(context, slot, false);
                            return 1;
                        })
                )
        );
    }

    private static int toggleRender(CommandContext<FabricClientCommandSource> context, EquipmentSlot slot) {
        boolean currentStatus = NoArmorRender.getRenderStatus(slot);
        boolean newStatus = !currentStatus;
        setRenderStatus(context, slot, newStatus);
        String statusMessage = newStatus ? "enabled" : "disabled";
        sendFeedback(context, PREFIX + slot.getName() + " render is now " + statusMessage);
        return 1;
    }

    private static void setRenderStatus(CommandContext<FabricClientCommandSource> ignorecontext, EquipmentSlot slot, boolean render) {
        NoArmorRender.setRenderStatus(slot, render);
    }

    private static void sendFeedback(CommandContext<FabricClientCommandSource> context, String message) {
        context.getSource().sendFeedback(Text.literal(message));
    }
}
