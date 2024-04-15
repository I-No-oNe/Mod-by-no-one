package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.NoOneMod.PREFIX;
import static net.i_no_am.client.ClientEntrypoint.BOAT_FLY;

public class BoatFly extends ToggledModule {

    private static double upwardSpeed = 0.15;
    private static double forwardSpeed;
    private static boolean changeForwardSpeed;

    private boolean warningSent = false; // Flag to track whether the warning message has been sent

    public BoatFly() {
        super("Boat Fly", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (!BOAT_FLY.enabled) {
            warningSent = false; // Reset the flag if the module is disabled
            return;
        }
        PlayerEntity player = client.player;
        if (player == null || !player.hasVehicle()) {
            warningSent = false; // Reset the flag if there's no player or no vehicle
            return;
        }

        Entity vehicle = player.getVehicle();
        Vec3d velocity = Objects.requireNonNull(vehicle).getVelocity();

        double motionX = velocity.x;
        double motionY = 0;
        double motionZ = velocity.z;

        if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_SPACE)) {
            motionY = upwardSpeed;
        } else if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            motionY = velocity.y;
        }

        if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_W) && changeForwardSpeed) {
            float yawRad = vehicle.getYaw() * MathHelper.RADIANS_PER_DEGREE;
            motionX = -MathHelper.sin(yawRad) * forwardSpeed;
            motionZ = MathHelper.cos(yawRad) * forwardSpeed;
        }

        vehicle.setVelocity(motionX, motionY, motionZ);

        // Send warning message only once
        if (!warningSent) {
            player.sendMessage(Text.literal(PREFIX + Formatting.RED + "Be aware that you might get banned"), false);
            warningSent = true; // Update the flag to indicate that the message has been sent
        }
    }

    public void setUpwardSpeed(double upwardSpeed) {
        BoatFly.upwardSpeed = upwardSpeed;
    }

    public void setForwardSpeed(double forwardSpeed) {
        BoatFly.forwardSpeed = forwardSpeed;
    }

    public void setChangeForwardSpeed(boolean changeForwardSpeed) {
        BoatFly.changeForwardSpeed = changeForwardSpeed;
    }
}
