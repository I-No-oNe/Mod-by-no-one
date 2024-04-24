package net.i_no_am.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.resource.DataPackSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ALL")
@Mixin(DataPackSettings.class)
public class MixinDataPackSettings {

    @Inject(at = @At("RETURN"), method = "getDisabled", cancellable =true)
    public void getDisabled(CallbackInfoReturnable r){
        r.setReturnValue(ImmutableList.of());
    }
}
