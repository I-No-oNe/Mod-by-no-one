package net.i_no_am.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static net.i_no_am.client.ClientEntrypoint.NO_SLOW;
import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;
import static net.minecraft.block.Blocks.COBWEB;
import static net.minecraft.block.Blocks.SOUL_SAND;

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
            if (NO_SLOW.enabled) {
                activeStatusEffects.remove(StatusEffects.SLOWNESS);
            }
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
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovement(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        World world = livingEntity.getWorld();
        BlockPos pos = livingEntity.getBlockPos();
        if (world.getBlockState(pos).getBlock() == SOUL_SAND) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 3));
        } else if (world.getBlockState(pos).getBlock() == COBWEB) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 1));
        }
    }
}

