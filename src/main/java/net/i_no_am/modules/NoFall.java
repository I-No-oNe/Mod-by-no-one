package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

import static net.i_no_am.client.ClientEntrypoint.NO_FALL;


public class NoFall extends ToggledModule {

    public NoFall() {
        super("NoFall", GLFW.GLFW_KEY_UNKNOWN);
    }
    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (!NO_FALL.enabled) return;
        ClientPlayerEntity player = client.player;
        assert player != null;
        if (player.fallDistance <= (player.isFallFlying() ? 1 : 2))
            return;

        if (player.isFallFlying() && player.isSneaking()
                && !isFallingFastEnoughToCauseDamage(player))
            return;

        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    }

    private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }
}
