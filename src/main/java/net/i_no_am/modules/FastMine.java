package net.i_no_am.modules;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import static net.i_no_am.client.ClientEntrypoint.client;
import static net.i_no_am.client.ClientEntrypoint.networkHandler;

public class FastMine extends ToggledModule {
    BlockPos targetPos;
    int timer;

    public FastMine() {
        super("Fast Miner", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tickEnabled() {
        if (client.player == null || client.interactionManager == null) return;

        if (++timer % 2 != 0) return;  // Only run every few ticks

        // Check if the crosshair target is a block
        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
            BlockPos targetPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();

            // Send packet to start and stop destroying the targeted block
            networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, targetPos, direction));
            networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, targetPos, direction));
        }

    }

    @Override
    public void onEnable() {
        if (client.player == null) return;

        timer = 0;

        for (Direction direction : Direction.values()) {
            targetPos = client.player.getBlockPos().offset(direction);
        }
    }
}
