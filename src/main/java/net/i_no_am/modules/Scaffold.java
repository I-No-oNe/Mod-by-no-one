    package net.i_no_am.modules;

    import net.i_no_am.client.Global;
    import net.minecraft.block.BlockState;
    import net.minecraft.client.MinecraftClient;
    import net.minecraft.client.network.ClientPlayerEntity;
    import net.minecraft.command.argument.EntityAnchorArgumentType;
    import net.minecraft.util.Hand;
    import net.minecraft.util.hit.BlockHitResult;
    import net.minecraft.util.math.BlockPos;
    import net.minecraft.util.math.Direction;
    import net.minecraft.util.math.Vec3d;
    import org.lwjgl.glfw.GLFW;

    import static net.i_no_am.client.ClientEntrypoint.SCAFFOLD;

    public class Scaffold extends ToggledModule implements Global {

        public Scaffold() {
            super("Scaffold", GLFW.GLFW_KEY_UNKNOWN);
        }

        @Override
        public void tick(MinecraftClient client) {
            super.tick(client);
            ClientPlayerEntity player = client.player;
            if (player == null || client.world == null || !SCAFFOLD.enabled) {
                return;
            }
            BlockPos pos = mc.player.getBlockPos().add(0, -1, 0);
            BlockState state = mc.world.getBlockState(pos);
            if (!state.isAir()) {
                return;
            }

            client.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, pos.toCenterPos());
            client.player.setYaw(180);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP.getOpposite(), pos, true));
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }


