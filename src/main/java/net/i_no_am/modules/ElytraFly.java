package net.i_no_am.modules;

import net.i_no_am.client.Global;
import net.i_no_am.utils.PlayerUtils;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.client.ClientEntrypoint.*;

public class ElytraFly extends ToggledModule implements Global {

    private static float speed = 1f;
    private static boolean autoFly = false;

    public ElytraFly() {
        super("Elytra Fly", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (client.player == null) {
            return;
        }
        if (!client.player.isFallFlying()) {
            return;
        }

        if (!NO_FALL.enabled && !FLY_HACK.enabled && ELYTRA_FLY.enabled) {
            double moveX = 0;
            double moveY = 0;
            double moveZ = 0;

            if (client.options.jumpKey.isPressed()) {
                moveY += speed;
            }
            if (client.options.sneakKey.isPressed()) {
                moveY -= speed;
            }

            client.player.setVelocity(0, 0, 0);
            client.player.updateVelocity(speed, PlayerUtils.getMovement());
            client.player.addVelocity(moveX, moveY, moveZ);

            if (!Objects.requireNonNull(mc.player).isFallFlying() && autoFly) {
                Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                client.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
        }
    }


    public static void setSpeed(float speed) {
        ElytraFly.speed = speed;
    }

    public static void setAutoFly(boolean autoFly) {
        ElytraFly.autoFly = autoFly;
    }
}
