package net.i_no_am.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.utils.InteractionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.village.VillagerProfession;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

import static net.i_no_am.client.ClientEntrypoint.AUTO_ATTACK;

public class AutoAttack extends ToggledModule {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random random = new Random();

    // Define a field to store the last attack time
    private long lastAttackTime = 0;

    public AutoAttack() {
        super("Auto Attack", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (AUTO_ATTACK.enabled) {
                autoAttack();
            }
        });
    }

    private void autoAttack() {
        ClientPlayerEntity player = client.player;

        // Additional conditions
        if (player == null || client.currentScreen != null) return;
        if (!isHoldingWeapon(player)) return;

        HitResult hitResult = client.crosshairTarget;
        if (!(hitResult instanceof EntityHitResult entityHitResult)) return;

        if (!entityHitResult.getType().equals(EntityHitResult.Type.ENTITY)) return;

        if (entityHitResult.getEntity() instanceof VillagerEntity villager &&
                villager.getVillagerData().getProfession() == VillagerProfession.NONE) {
            return;
        }

        int minDelayTicks = 3;
        int maxDelayTicks = 17;
        int randomDelayTicks = random.nextInt(maxDelayTicks - minDelayTicks + 1) + minDelayTicks;

        if (player.getAttackCooldownProgress(0.0F) < 1.0) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime < randomDelayTicks) return;

        InteractionUtils.inputAttack();

        // Update the last attack time
        lastAttackTime = currentTime;
    }

    private boolean isHoldingWeapon(ClientPlayerEntity player) {
        // Check if the player is holding a weapon
        if (player == null) return false;
        Item item = player.getMainHandStack().getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }
}

