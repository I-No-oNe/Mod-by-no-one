package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class FreeCamera extends ToggledModule {
    private boolean wasEnabled = false;
    private Vec3d savedPosition;

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
            // Save the player's position when enabling the module
            savedPosition = player.getPos();
            // Set player to swimming state
//            player.setSwimming(true);
        }

        if (isEnabled) {
            double speed = 0.6;
            Vec3d motion = Vec3d.ZERO;
            if (client.options.backKey.isPressed()) motion = motion.add(0, 0, -speed);
            if (client.options.forwardKey.isPressed()) motion = motion.add(0, 0, speed);
            if (client.options.rightKey.isPressed()) motion = motion.add(-speed, 0, 0);
            if (client.options.leftKey.isPressed()) motion = motion.add(speed, 0, 0);
            if (client.options.jumpKey.isPressed()) motion = motion.add(0, speed, 0);
            if (client.options.sneakKey.isPressed()) motion = motion.add(0, -speed, 0);

            // Set player velocity for smooth movement
            player.setVelocity(motion);
        } else {
            // Restore the player's position when disabling the module
            if (wasEnabled) {
                player.updatePosition(savedPosition.x, savedPosition.y, savedPosition.z);
            }
        }

        wasEnabled = isEnabled;
    }
}
