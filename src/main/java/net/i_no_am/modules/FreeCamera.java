package net.i_no_am.modules;

import net.i_no_am.client.Global;
import net.i_no_am.utils.FakePlayerEntityUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static net.i_no_am.client.ClientEntrypoint.FREE_CAMERA;

public class FreeCamera extends ToggledModule implements Global {
    public FakePlayerEntityUtils fakePlayer;
    private boolean disableActionsExecuted = false;

    public FreeCamera() {
        super("Free Camera", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        final ClientPlayerEntity player = client.player;
        if (player == null) return;
        final ClientWorld world = player.clientWorld;
        if (fakePlayer == null && FREE_CAMERA.enabled) {
            fakePlayer = new FakePlayerEntityUtils(player, world);
        }
        if (FREE_CAMERA.enabled) {
            player.getAbilities().flying = true;
            disableActionsExecuted = false;
        }
        if (!FREE_CAMERA.enabled && !disableActionsExecuted) {
            assert client.player != null;
            client.player.setVelocity(Vec3d.ZERO);
            client.player.getAbilities().flying = false;
            if (fakePlayer != null) {
                fakePlayer.resetPlayerPosition();
                fakePlayer.despawn();
            }
            fakePlayer=null;
            disableActionsExecuted = true;
        }
    }
}
