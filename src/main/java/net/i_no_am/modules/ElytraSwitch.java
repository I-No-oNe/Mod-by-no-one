package net.i_no_am.modules;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.utils.InteractionUtils;
import net.i_no_am.utils.SwitchUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.Set;

import static net.i_no_am.client.ClientEntrypoint.ELYTRA_SWITCH;
import static net.minecraft.item.Items.ELYTRA;

public class ElytraSwitch extends ToggledModule {

    private static final Set<ArmorItem> CHESTPLATES = ImmutableSet.of(
            (ArmorItem) Items.LEATHER_CHESTPLATE,
            (ArmorItem) Items.IRON_CHESTPLATE,
            (ArmorItem) Items.GOLDEN_CHESTPLATE,
            (ArmorItem) Items.DIAMOND_CHESTPLATE,
            (ArmorItem) Items.NETHERITE_CHESTPLATE);

    private final int switchDelayTicks = 15;
    private int tickCounter = 0;
    private boolean usingElytra = false;

    public ElytraSwitch() {
        super("Elytra Switch", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (ELYTRA_SWITCH.enabled) {
            ClientTickEvents.END_CLIENT_TICK.register(clientTick -> {
                if (ELYTRA_SWITCH.enabled) {
                    if (usingElytra && wait(switchDelayTicks)) {
                        usingElytra = false;
                    } else {
                        if (!client.player.isOnGround()) {
                            switchToElytra();
                            usingElytra = true;
                        } else {
                            switchToChestplate();
                            usingElytra = false;
                        }
                    }
                }
            });
        }
    }
    private void switchToElytra() {
        if (SwitchUtils.search(ELYTRA)) {
            wait(switchDelayTicks);
            InteractionUtils.inputUse();
        }
    }

    private boolean wait(int ticks) {
        tickCounter++;
        if (tickCounter >= ticks) {
            tickCounter = 0;
            return true;
        }
        return false;
    }

    private void switchToChestplate() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        PlayerEntity player = minecraft.player;
        if (player == null || !player.isOnGround()) return;
        for (ArmorItem chestplate : CHESTPLATES) {
            if (SwitchUtils.search(chestplate)) {
                InteractionUtils.inputUse();
                return;
            }
        }
    }
}
