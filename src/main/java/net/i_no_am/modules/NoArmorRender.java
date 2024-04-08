package net.i_no_am.modules;

import org.lwjgl.glfw.GLFW;


public class NoArmorRender extends ToggledModule {

    public NoArmorRender() {
        super("NoArmorRender", GLFW.GLFW_KEY_M);
    }
}