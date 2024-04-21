package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.client.ClientEntrypoint.NO_SLOW;

public class NoSlow extends ToggledModule {

    public NoSlow() {
        super("No Slow", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);

        if (NO_SLOW.enabled && client.player != null) {
            if (client.player.isOnGround()) { // Check if player is on the ground
                Vec3d playerVelocity = client.player.getVelocity();
                if (shouldRemoveSlowingEffects(client)) {
                    client.player.clearStatusEffects();
                }
                playerVelocity = adjustPlayerVelocity(playerVelocity, client);
                client.player.setVelocity(playerVelocity);
            }
        }
    }

    private boolean shouldRemoveSlowingEffects(MinecraftClient client) {
        return (Objects.requireNonNull(client.player).hasStatusEffect(StatusEffects.SLOWNESS) && isEnabled()) &&
                !(client.player.isUsingItem() || client.player.isSneaking() || isOnBlock(client));
    }

    private Vec3d adjustPlayerVelocity(Vec3d velocity, MinecraftClient client) {
        Vec3d adjustedVelocity = velocity;

        if (Objects.requireNonNull(client.player).isInFluid() || isOnBlock(client)) {
            if (!client.player.isOnGround()) {
                adjustedVelocity = adjustedVelocity.multiply(1.6, 1, 1.6);
            }
        }

        if (NO_SLOW.enabled && (client.player.isUsingItem() || client.player.isSneaking())) {
            adjustedVelocity = adjustedVelocity.multiply(1.6, 1, 1.6);
        }
        return adjustedVelocity;
    }

    private boolean isOnBlock(MinecraftClient client) {
        Vec3d playerPos = Objects.requireNonNull(client.player).getPos();
        BlockPos blockPos = new BlockPos((int) playerPos.x, (int) playerPos.y, (int) playerPos.z);
        Block block = Objects.requireNonNull(client.world).getBlockState(blockPos.down()).getBlock();
        return block == Blocks.SOUL_SAND || block == Blocks.COBWEB;
    }
}