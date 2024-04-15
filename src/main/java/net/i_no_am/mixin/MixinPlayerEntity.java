package net.i_no_am.mixin;

import net.i_no_am.utils.TargetUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.i_no_am.client.ClientEntrypoint.AUTO_ATTACK;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Inject(method = "attack", at = @At("HEAD"))
    public void attack(Entity target, CallbackInfo ci) {
        if (target instanceof LivingEntity living && AUTO_ATTACK.enabled) {
            TargetUtils.setTarget(living);
        }
    }
}