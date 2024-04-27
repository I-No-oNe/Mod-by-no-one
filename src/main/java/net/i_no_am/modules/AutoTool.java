//package net.i_no_am.modules;
//
//import net.i_no_am.client.Global;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.ClientPlayerEntity;
//import net.minecraft.block.BlockState;
//import net.minecraft.enchantment.Enchantments;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.BlockPos;
//import org.lwjgl.glfw.GLFW;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Map;
//
//public class AutoTool extends ToggledModule implements Global {
//
//    public AutoTool() {
//        super("Auto Tool", GLFW.GLFW_KEY_UNKNOWN);
//    }
//
//    @Override
//    public void tick(MinecraftClient client) {
//        super.tick(client);
//        ClientPlayerEntity player = client.player;
//        if (player == null || !isEnabled()) return;
//
//        BlockPos playerPos = player.getBlockPos();
//        BlockState state = player.getWorld().getBlockState(playerPos);
//        if (state.isAir()) return;
//
//        final Map<Integer, Float> entries = new HashMap<>();
//        player.getInventory().main.forEach((slot, itemStack) -> {
//            if (!itemStack.isEmpty()) {
//                entries.put(slot, calcWantedLvl(itemStack, state));
//            }
//        });
//
//        int selectedSlot = entries.entrySet().stream()
//                .max(Comparator.comparing(Map.Entry::getValue))
//                .map(Map.Entry::getKey)
//                .orElse(-1);
//
//        if (selectedSlot != -1) player.getInventory().selectedSlot = selectedSlot;
//    }
//
//    private float calcWantedLvl(ItemStack item, BlockState state) {
//        float lvl = 0;
//        lvl += item.getMiningSpeedMultiplier(state);
//        lvl += item.encha(Enchantments.EFFICIENCY);
//        return lvl;
//    }
//}
