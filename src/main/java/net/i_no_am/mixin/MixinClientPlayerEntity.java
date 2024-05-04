package net.i_no_am.mixin;


import net.i_no_am.utils.InteractionUtils;
import net.i_no_am.utils.SwitchUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.i_no_am.client.ClientEntrypoint.ELYTRA_SWITCH;
import static net.minecraft.item.Items.ELYTRA;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE))
    public void swapToElytra(CallbackInfo callbackInfo) {
        if (ELYTRA_SWITCH.enabled) {
            var target = ((ClientPlayerEntity) (Object) this);
            if (!target.isOnGround() && !target.isFallFlying() && !target.isTouchingWater() && !target.hasStatusEffect(StatusEffects.LEVITATION)) {
                SwitchUtils.search(ELYTRA);
                InteractionUtils.inputUse();
            }
        }
    }
}