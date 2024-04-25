package net.i_no_am.utils;

import net.i_no_am.client.Global;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;

public class SwitchUtils implements Global {

    public static boolean search(Item item) {

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
