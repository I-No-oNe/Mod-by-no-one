package net.i_no_am.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.utils.InteractionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.Random;

import static net.i_no_am.client.ClientEntrypoint.AUTO_ATTACK;

public class AutoAttack extends ToggledModule {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random random = new Random();

    public AutoAttack() {
        super("AutoAttack", GLFW.GLFW_KEY_M);
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
        if (!player.isHolding(Items.WOODEN_SWORD) && !player.isHolding(Items.STONE_SWORD) && !player.isHolding(Items.IRON_SWORD) &&
                !player.isHolding(Items.GOLDEN_SWORD) && !player.isHolding(Items.DIAMOND_SWORD) && !player.isHolding(Items.NETHERITE_SWORD) &&
                !player.isHolding(Items.WOODEN_AXE) && !player.isHolding(Items.STONE_AXE) && !player.isHolding(Items.IRON_AXE) &&
                !player.isHolding(Items.GOLDEN_AXE) && !player.isHolding(Items.DIAMOND_AXE) && !player.isHolding(Items.NETHERITE_AXE)) {
            return; // Player is not holding a weapon
        }
        // Getting the targeted entity from the hit result
        HitResult hitResult = client.crosshairTarget;
        if (!(hitResult instanceof EntityHitResult entityHitResult)) return;

        if (!entityHitResult.getType().equals(EntityHitResult.Type.ENTITY)) return;

        // Attacking the targeted entity after a random delay
        int minDelayTicks = 3; // 1 second in ticks
        int maxDelayTicks = 15; // 2 seconds in ticks (increased for more randomness)
        int randomDelayTicks = random.nextInt(maxDelayTicks - minDelayTicks + 1) + minDelayTicks;

        // Check if enough ticks have passed since the last attack
        if (player.getAttackCooldownProgress(0.0F) < 1.0) return; // Not ready to attack yet

        // Check if enough ticks have passed to trigger the attack
        if (Objects.requireNonNull(client.world).getTime() % randomDelayTicks != 0) return;

        // Perform the attack
        InteractionUtils.inputAttack();
    }
}
