package net.i_no_am.modules;

import net.minecraft.entity.EquipmentSlot;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.Map;

public class NoArmorRender extends ToggledModule {

    public static final Map<EquipmentSlot, Boolean> RENDER_STATUS = new EnumMap<>(EquipmentSlot.class);

    public NoArmorRender() {
        super("No Armor Render", GLFW.GLFW_KEY_UNKNOWN);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            RENDER_STATUS.put(slot, false);
        }
    }

    public static void setRenderStatus(EquipmentSlot slot, boolean render) {
        RENDER_STATUS.put(slot, render);
    }

    public static boolean getRenderStatus(EquipmentSlot slot) {
        return RENDER_STATUS.getOrDefault(slot, false);
    }
}
