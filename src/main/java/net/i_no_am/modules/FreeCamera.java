package net.i_no_am.modules;

import net.i_no_am.client.Global;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static net.i_no_am.NoOneMod.PREFIX;

public class FreeCamera extends ToggledModule implements Global {
    private static double cameraSpeed = 2.0;
    private boolean wasEnabled = false;
    private boolean warningSent = false;
    private Vec3d savedPosition;
    private boolean resetVelocity = false;

    public FreeCamera() {
        super("Free Camera", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);

        ClientPlayerEntity player = client.player;
        if (player == null) return;

        boolean isEnabled = isEnabled();
        if (isEnabled && !wasEnabled) {
            savedPosition = player.getPos();
        }

        if (isEnabled) {
            Vec3d motion = Vec3d.ZERO;
            Vec3d lookVector = player.getRotationVector();
            double speed = cameraSpeed;

            if (client.options.backKey.isPressed()) motion = motion.add(lookVector.multiply(-speed, 0, -speed));
            if (client.options.forwardKey.isPressed()) motion = motion.add(lookVector.multiply(speed, 0, speed));
            if (client.options.leftKey.isPressed()) motion = motion.add(lookVector.rotateY(90).multiply(speed, 0, speed));
            if (client.options.rightKey.isPressed()) motion = motion.add(lookVector.rotateY(-90).multiply(speed, 0, speed));
            if (client.options.jumpKey.isPressed()) motion = motion.add(0, speed, 0);
            if (client.options.sneakKey.isPressed()) motion = motion.add(0, -speed, 0);

            player.setVelocity(motion);
        } else {
            if (resetVelocity) {
                player.setVelocity(Vec3d.ZERO);
                resetVelocity = false;
            }

            if (wasEnabled && !warningSent) {
                player.sendMessage(Text.of(PREFIX + Formatting.RED + "You have disabled the Free camera module."), false);
                warningSent = true;
            }

            if (!isEnabled() && wasEnabled) {
                if (savedPosition != null) {
                    player.updatePosition(savedPosition.x, savedPosition.y, savedPosition.z);
                    savedPosition = null;
                }
            }
        }

        wasEnabled = isEnabled;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        resetVelocity = true;
    }

    public static void setSpeed(double speed) {
        cameraSpeed = speed;
    }
}
