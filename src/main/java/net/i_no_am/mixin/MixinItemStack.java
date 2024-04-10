package net.i_no_am.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Inject(method = "setBobbingAnimationTime", at = @At("HEAD"), cancellable = true)
    public void setBobbingAnimationTime(int bobbingAnimationTime, CallbackInfo ci) {
        if (RENDER_TWEAKS.enabled) {
            ci.cancel();
        }
    }

    @Inject(method = "getBobbingAnimationTime", at = @At("RETURN"), cancellable = true)
    public void getBobbingAnimationTime(CallbackInfoReturnable<Integer> cir) {
        if (RENDER_TWEAKS.enabled) {
            cir.setReturnValue(0);
        }
    }
}