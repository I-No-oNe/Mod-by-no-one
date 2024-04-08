package net.i_no_am.modules;

import org.lwjgl.glfw.GLFW;


public class NoGuiBackground extends ToggledModule {

    public NoGuiBackground() {
        super("NoGuiBackground", GLFW.GLFW_KEY_GRAVE_ACCENT);
    }
}