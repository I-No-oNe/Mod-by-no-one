package net.i_no_am.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;

@Mixin(LightmapTextureManager.class)
public abstract class MixinLightmapTextureManager {

    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void getBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        if (RENDER_TWEAKS.enabled) {
            cir.setReturnValue(15.0F);
        }
    }
}
