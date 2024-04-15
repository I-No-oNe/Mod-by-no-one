package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.NoOneMod.PREFIX;
import static net.i_no_am.client.ClientEntrypoint.FLY_HACK;

public class FlyHack extends ToggledModule {

    private boolean warningSent = false; // Flag to track whether the warning message has been sent

    public FlyHack() {
        super("Fly Hack", GLFW.GLFW_KEY_UNKNOWN);
    }

    private static final int SPACE_KEY = GLFW.GLFW_KEY_SPACE;
    private static final double JETPACK_MAX_SPEED = 0.6;

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (!FLY_HACK.enabled) {
            warningSent = false; // Reset the flag if the module is disabled
            return;
        }

        if (client.world == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player != null) {
            // Fly logic
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), SPACE_KEY)) {
                Vec3d motion = player.getVelocity();
                Vec3d vel = new Vec3d(motion.x * 1.5, 0.25, motion.z * 1.5);
                if (vel.lengthSquared() > JETPACK_MAX_SPEED * JETPACK_MAX_SPEED) {
                    vel = vel.normalize().multiply(JETPACK_MAX_SPEED);
                }
                player.setVelocity(vel);
            }

            // No fall logic
            if (player.fallDistance > (player.isFallFlying() ? 1 : 2)) {
                if (player.isFallFlying() && player.isSneaking() && !isFallingFastEnoughToCauseDamage(player)) {
                    return;
                }
                player.networkHandler.sendPacket(new OnGroundOnly(true));
            }

            // Send warning message only once
            if (!warningSent) {
                Objects.requireNonNull(player).sendMessage(Text.literal(PREFIX + Formatting.RED + "Be aware that you might get banned"), false);
                warningSent = true; // Update the flag to indicate that the message has been sent
            }
        }
    }

    private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }
}
