package net.i_no_am.mixin;

import net.i_no_am.client.Global;
import net.i_no_am.utils.TargetUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.i_no_am.client.ClientEntrypoint.AUTO_ATTACK;
import static net.i_no_am.client.ClientEntrypoint.FREE_CAMERA;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends Entity implements Global {

    protected MixinPlayerEntity(World world) {
        super(null, world);
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void attack(Entity target, CallbackInfo ci) {
        if (target instanceof LivingEntity living && AUTO_ATTACK.enabled) {
            TargetUtils.setTarget(living);
        }
    }

    @Inject(method = "isPushedByFluids", at = @At("HEAD"), cancellable = true)
    private void isPushedByFluids(CallbackInfoReturnable<Boolean> cir) {

        if (FREE_CAMERA.enabled) {
            cir.setReturnValue(false);
        }
    }
}

