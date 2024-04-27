package net.i_no_am.modules;

import org.lwjgl.glfw.GLFW;

public class InventoryTweaks extends ToggledModule {

    public static boolean hold_mouse0;

    public InventoryTweaks() {
        super("Inventory Tweaks", GLFW.GLFW_KEY_UNKNOWN);
    }
}
