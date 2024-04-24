package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.i_no_am.client.ClientEntrypoint.SCAFFOLD;

public class Scaffold extends ToggledModule {

    public Scaffold() {
        super("Scaffold", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            return;
        }

        // Check if the Scaffold module is enabled
        if (!SCAFFOLD.enabled) {
            return;
        }

        // Check if the player has blocks in the inventory
        ItemStack blockStack = findBlockInInventory(player);
        if (blockStack.isEmpty()) {
            // No blocks available, return
            return;
        }

        BlockPos pos = player.getBlockPos().add(0, -1, 0);
        if (!client.world.getBlockState(pos).isAir()) {
            return;
        }

        // Calculate block center position
        Vec3d blockCenter = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        // Calculate lookAt position
        Vec3d lookAt = player.getRotationVec(1.0F).multiply(4.0).add(new Vec3d(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ()));

        // Calculate pitch and yaw
        double pitch = Math.atan2(lookAt.y - blockCenter.y, Math.sqrt((lookAt.x - blockCenter.x) * (lookAt.x - blockCenter.x) + (lookAt.z - blockCenter.z) * (lookAt.z - blockCenter.z)));
        double yaw = Math.atan2(lookAt.z - blockCenter.z, lookAt.x - blockCenter.x);

        // Set player pitch and yaw
        player.setYaw((float) Math.toDegrees(yaw));
        player.setPitch((float) Math.toDegrees(pitch));

        // Interact with the block
        Objects.requireNonNull(client.interactionManager).interactBlock(player, Hand.MAIN_HAND, new BlockHitResult(blockCenter, Direction.UP, pos, true));
        player.swingHand(Hand.MAIN_HAND);
    }

    private ItemStack findBlockInInventory(ClientPlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
