package net.i_no_am.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.utils.InteractionUtils;
import net.i_no_am.utils.SwitchUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.client.ClientEntrypoint.ELYTRA_SWITCH;

public class ElytraSwitch extends ToggledModule {

    public ElytraSwitch() {
        super("ElytraSwitch", GLFW.GLFW_KEY_R);
    }

    @Override
    public void onEnable() {
        if (ELYTRA_SWITCH.enabled) { // Check if the module is enabled
            // Register the event listener only if ELYTRA_SWITCH is enabled
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (isPlayerJumping()) {
                    switchToElytra();
                    // Perform action when player is jumping
                }
            });
        }
    }

    public static boolean isPlayerJumping() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        PlayerEntity player = minecraft.player;
        return !Objects.requireNonNull(player).isOnGround(); // Return true if the player is not on the ground
    }

    private void switchToElytra() {
        if (SwitchUtils.search(Items.ELYTRA)) {
            InteractionUtils.inputUse();
        }
    }
}
//TODO: make @ElytraSwitch so it will switch to chestplate when "isOnGround" is true, and , make the isPlayerJumping method to detect playerJumping+spacebar pressing.