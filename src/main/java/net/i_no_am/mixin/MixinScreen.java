package net.i_no_am.mixin;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;


@Mixin(Screen.class)
public abstract class MixinScreen {
    @Inject(method = "applyBlur", at = @At("HEAD"), cancellable = true)
    private void applyBlur(CallbackInfo info) {
        if (RENDER_TWEAKS.enabled) {
            info.cancel();
        }
    }

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void renderBackground(CallbackInfo info) {
        if (RENDER_TWEAKS.enabled) {
            info.cancel();
        }
    }

    @Inject(method = "renderInGameBackground", at = @At("HEAD"), cancellable = true)
    private void renderInGameBackground(CallbackInfo info) {
        if (RENDER_TWEAKS.enabled) {
            info.cancel();
        }
    }
}
