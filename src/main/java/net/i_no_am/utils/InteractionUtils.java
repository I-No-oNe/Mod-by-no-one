package net.i_no_am.utils;

import net.i_no_am.interfaces.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;

public final class InteractionUtils {
    // Declare and initialize mc variable
    private static final MinecraftClientAccessor mc = (MinecraftClientAccessor) MinecraftClient.getInstance();

    public static void inputAttack() {
        mc.inputAttack();
    }

    public static void inputUse() {
        mc.inputUse();
    }
}
