package net.i_no_am.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;



    @Inject(method = "getStatusEffects", at = @At("HEAD"), cancellable = true)
    private void getStatusEffects(CallbackInfoReturnable<Collection<StatusEffectInstance>> cir) {
        if (RENDER_TWEAKS.enabled) {
            activeStatusEffects.remove(StatusEffects.BLINDNESS);
            activeStatusEffects.remove(StatusEffects.DARKNESS);
            cir.setReturnValue(activeStatusEffects.values());
        }
    }
    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("HEAD"), cancellable = true)
    private void addStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (RENDER_TWEAKS.enabled) {
            if (effect.getEffectType() == StatusEffects.BLINDNESS || effect.getEffectType() == StatusEffects.DARKNESS) {
                cir.setReturnValue(false);
            }
        }
    }
}
