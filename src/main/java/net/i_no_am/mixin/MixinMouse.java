package net.i_no_am.mixin;

import net.i_no_am.modules.InventoryTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.i_no_am.client.ClientEntrypoint.INVENTORY_TWEAKS;

@Mixin(Mouse.class)
public class MixinMouse {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    public void onMouseButtonHook(long window, int button, int action, int mods, CallbackInfo ci) {
        if (INVENTORY_TWEAKS.enabled && window == MinecraftClient.getInstance().getWindow().getHandle()) {
            if (action == 0)
                InventoryTweaks.hold_mouse0 = false;
            if (action == 1)
                InventoryTweaks.hold_mouse0 = true;
        }
    }
}