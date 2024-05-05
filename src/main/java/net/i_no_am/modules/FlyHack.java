package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static net.i_no_am.client.ClientEntrypoint.FLY_HACK;

public class FlyHack extends ToggledModule {
    int toggle = 0;
    double FALL_SPEED = -0.04;


    public FlyHack() {
        super("Fly Hack", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        final ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (FLY_HACK.enabled) {
            boolean inFlyMode = player.isCreative() || player.isSpectator();
            PlayerAbilities abilities = player.getAbilities();

            abilities.flying = inFlyMode && !player.isOnGround() && !FLY_HACK.enabled;
            abilities.allowFlying = inFlyMode;
        }
        boolean inFlyMode = player.isCreative() || player.isSpectator();

        player.getAbilities().allowFlying = true;
        if (player.hasVehicle()) return;
        if (inFlyMode) return;

        final Vec3d velocity = player.getVelocity();
        if (FLY_HACK.enabled && toggle == 0) {
            player.setVelocity(new Vec3d(
                    velocity.x, FALL_SPEED - velocity.y, velocity.z
            ));
        }
        if (toggle == 0 || velocity.y < FALL_SPEED)
            toggle = 20;
        toggle--;
    }
}