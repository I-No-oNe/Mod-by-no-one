package net.i_no_am.modules;

import net.i_no_am.client.Global;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.NoOneMod.PREFIX;

public class FreeCamera extends ToggledModule implements Global {
    private static double cameraSpeed = 2.0;
    private boolean wasEnabled = false;
    private boolean warningSent = false;
    private Vec3d savedPosition;
    private boolean resetVelocity = false;
    private OtherClientPlayerEntity fakePlayer = null;

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
            createFakePlayer(client, savedPosition, player.getYaw(), player.getPitch());
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
            if (wasEnabled && !warningSent) {
                player.sendMessage(Text.of(PREFIX + Formatting.RED + "You have disabled the Free camera module."), false);
                warningSent = true;
            }

            if (wasEnabled && savedPosition != null) {
                player.updatePosition(savedPosition.x, savedPosition.y, savedPosition.z);
            }

            resetVelocity = true;
        }

        wasEnabled = isEnabled;
    }

    private void createFakePlayer(MinecraftClient client, Vec3d position, float yaw, float pitch) {
        fakePlayer = new OtherClientPlayerEntity(Objects.requireNonNull(client.world), client.getGameProfile());
        fakePlayer.updatePosition(position.x, position.y, position.z);
        fakePlayer.setYaw(yaw);
        fakePlayer.setPitch(pitch);
        client.world.addEntity(fakePlayer);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (resetVelocity) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                player.setVelocity(Vec3d.ZERO);
            }
            resetVelocity = false;
        }

        if (fakePlayer != null) {
            fakePlayer.remove(Entity.RemovalReason.DISCARDED);
            fakePlayer = null;
        }
    }

    public static void setSpeed(double speed) {
        cameraSpeed = speed;
    }
}
