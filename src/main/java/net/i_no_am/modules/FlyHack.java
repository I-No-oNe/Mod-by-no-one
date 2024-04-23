package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.NoOneMod.PREFIX;
import static net.i_no_am.client.ClientEntrypoint.FLY_HACK;

public class FlyHack extends ToggledModule {

    private boolean warningSent = false;

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

        ClientPlayerEntity player = client.player;
        if (player != null) {
            Vec3d viewVector = Objects.requireNonNull(client.getCameraEntity()).getRotationVec(1.0F);
            if (GLFW.glfwGetKey(client.getWindow().getHandle(), SPACE_KEY) == GLFW.GLFW_PRESS) {
                Vec3d motion = new Vec3d(viewVector.x * 1.5, 0.25, viewVector.z * 1.5);
                if (motion.lengthSquared() > JETPACK_MAX_SPEED * JETPACK_MAX_SPEED) {
                    motion = motion.normalize().multiply(JETPACK_MAX_SPEED);
                }
                player.setVelocity(motion);
            }

            if (player.fallDistance > 2 && !isFallingFastEnoughToCauseDamage(player)) {
                player.fallDistance = 0;
            }

            if (!warningSent) {
                player.sendMessage(Text.of(PREFIX + Formatting.RED + "Be aware that you might get banned"), false);
                warningSent = true;
            }
        }
    }

    private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }
}
