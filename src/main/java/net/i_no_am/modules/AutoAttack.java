package net.i_no_am.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.client.Global;
import net.i_no_am.utils.FakePlayerEntityUtils;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static net.i_no_am.client.ClientEntrypoint.AUTO_ATTACK;

public class AutoAttack extends ToggledModule implements Global {
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(AutoAttack.class.getName());

    private long lastAttackTime = 0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public AutoAttack() {
        super("Auto Attack", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
    }

    @Override
    public void onDisable() {
        scheduler.shutdown();
    }

    private void autoAttack() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null || client.currentScreen != null) return;
        if (!isHoldingWeapon(player)) return;

        HitResult hitResult = client.crosshairTarget;
        if (!(hitResult instanceof EntityHitResult entityHitResult)) return;

        if (!entityHitResult.getType().equals(HitResult.Type.ENTITY)) return;

        if (entityHitResult.getEntity() instanceof VillagerEntity villager) {
            if (villager.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
                return;
            }
        }

        if (entityHitResult.getEntity() instanceof FakePlayerEntityUtils) return;

        if (player.getAttackCooldownProgress(0.0F) < 1.0) return;

        long currentTime = System.currentTimeMillis();
        int shortDelay = 3;
        int longDelay = 15;
        int randomDelayMillis = (random.nextInt(longDelay - shortDelay + 1) + shortDelay) * 50;

        if (currentTime - lastAttackTime < randomDelayMillis) return;

        scheduleAttack(randomDelayMillis);
    }

    private void scheduleAttack(int delayMillis) {
        scheduler.schedule(() -> {
            try {
                InteractionUtils.inputAttack();
                lastAttackTime = System.currentTimeMillis();
            } catch (Exception e) {
                logger.severe("Error during scheduled attack: " + e.getMessage());
                e.printStackTrace();
            }
        }, delayMillis, TimeUnit.MILLISECONDS);
    }

    private boolean isHoldingWeapon(ClientPlayerEntity player) {
        if (player == null) return false;
        Item item = player.getMainHandStack().getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }

    private void onStartTick(MinecraftClient client) {
        if (AUTO_ATTACK.enabled) {
            try {
                autoAttack();
            } catch (Exception e) {
                logger.severe("Error during autoAttack: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
