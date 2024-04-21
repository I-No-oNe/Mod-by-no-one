package net.i_no_am.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.utils.InteractionUtils;
import net.i_no_am.utils.SwitchUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

import static net.i_no_am.client.ClientEntrypoint.ELYTRA_SWITCH;
import static net.minecraft.item.Items.ELYTRA;

public class ElytraSwitch extends ToggledModule {

    private final Map<ArmorItem, EquipmentSlot> chestplateMap = new HashMap<>();

    private int tickCounter = 0;
    private boolean usingElytra = false;

    public ElytraSwitch() {
        super("Elytra Switch", GLFW.GLFW_KEY_UNKNOWN);
        initializeChestplateMap();
    }

    private void initializeChestplateMap() {
        chestplateMap.put((ArmorItem) Items.LEATHER_CHESTPLATE, EquipmentSlot.CHEST);
        chestplateMap.put((ArmorItem) Items.IRON_CHESTPLATE, EquipmentSlot.CHEST);
        chestplateMap.put((ArmorItem) Items.GOLDEN_CHESTPLATE, EquipmentSlot.CHEST);
        chestplateMap.put((ArmorItem) Items.DIAMOND_CHESTPLATE, EquipmentSlot.CHEST);
        chestplateMap.put((ArmorItem) Items.NETHERITE_CHESTPLATE, EquipmentSlot.CHEST);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (ELYTRA_SWITCH.enabled) {
            ClientTickEvents.END_CLIENT_TICK.register(clientTick -> {
                if (ELYTRA_SWITCH.enabled && usingElytra && wait(20)) { // Wait 20 ticks (1 second)
                    usingElytra = false;
                } else {
                    if (isPlayerJumping()&& ELYTRA_SWITCH.enabled) {
                        switchToElytra();
                        usingElytra = true;
                    } else {
                        switchToChestplate();
                        usingElytra = false;
                    }
                }
            });
        }
    }


    private boolean isPlayerJumping() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        PlayerEntity player = minecraft.player;

        if (player != null && !player.isOnGround()) {
            // Check if the player is in the air
            if (!player.isOnGround()) {
                // Wait for 5 ticks to ensure the player has been in the air for a short duration
                return wait(5);
            }
        }
        return false;
    }
    private void switchToElytra() {
        if (SwitchUtils.search(ELYTRA)) {
           wait(15); InteractionUtils.inputUse();
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
        if (player == null) return;
        if (player.isOnGround()) {
            for (Map.Entry<ArmorItem, EquipmentSlot> entry : chestplateMap.entrySet()) {
                if (SwitchUtils.search(entry.getKey())) {
                    InteractionUtils.inputUse();
                    return;
                }
            }
        }
    }
}
///TODO-make this thing work normally
