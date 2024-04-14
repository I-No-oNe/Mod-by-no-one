package net.i_no_am.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;

public class SwitchUtils {

    public static boolean search(Item item) {
        MinecraftClient mc = MinecraftClient.getInstance(); // Initialize mc

        if (mc.player == null) {
            return false;
        }

        PlayerInventory inv = mc.player.getInventory();

        for (int i = 0; i <= 8; i++) {
            if (inv.getStack(i).isOf(item)) {
                inv.selectedSlot = i;
                return true;
            }
        }
        return false;
    }
}
