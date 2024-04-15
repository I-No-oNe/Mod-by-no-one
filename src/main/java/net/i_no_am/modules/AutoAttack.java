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
import java.util.Timer;
import java.util.TimerTask;

import static net.i_no_am.client.ClientEntrypoint.AUTO_ATTACK;

public class AutoAttack extends ToggledModule {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random random = new Random();

    private long lastAttackTime = 0;
    private int shortDelay = 2; // Default short delay
    private int longDelay = 50; // Default long delay

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

        if (player.getAttackCooldownProgress(0.0F) < 1.0) return;

        long currentTime = System.currentTimeMillis();
        int randomDelay = random.nextInt(longDelay - shortDelay + 1) + shortDelay;

        if (currentTime - lastAttackTime < randomDelay) return;

        scheduleAttack(randomDelay);
    }

    private void scheduleAttack(int delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InteractionUtils.inputAttack();
                lastAttackTime = System.currentTimeMillis();
                timer.cancel(); // Cancel the timer after executing the task
            }
        }, delay);
    }

    private boolean isHoldingWeapon(ClientPlayerEntity player) {
        // Check if the player is holding a weapon
        if (player == null) return false;
        Item item = player.getMainHandStack().getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }
}
