package net.i_no_am.modules;

import org.lwjgl.glfw.GLFW;


public class AttackAssistance extends ToggledModule {

    public AttackAssistance() {
        super("AttackAssistance", GLFW.GLFW_KEY_L);
    }
}