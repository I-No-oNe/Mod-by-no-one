package net.i_no_am.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;


@Mixin(Entity.class)
public abstract class MixinEntity {

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void overrideIsInvisibleToPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (RENDER_TWEAKS.enabled) {
            if ((Object)this instanceof PlayerEntity) {
                cir.setReturnValue(false);
            }
        }
    }
}
