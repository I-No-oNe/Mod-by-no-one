package net.i_no_am.modules;

import org.lwjgl.glfw.GLFW;


public class SpectatorSight extends ToggledModule {

    public SpectatorSight() {
        super("SpectatorSight", GLFW.GLFW_KEY_COMMA);
    }
}