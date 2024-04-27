package net.i_no_am.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyBoard {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (action == 0 && key == 320 && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.networkHandler != null && MinecraftClient.getInstance().world != null) {
            for (int i = 5; i <= 45; i++) {
                MinecraftClient.getInstance().interactionManager.clickSlot(MinecraftClient.getInstance().player.currentScreenHandler.syncId, i, 1, SlotActionType.THROW, MinecraftClient.getInstance().player);
            }
            MinecraftClient.getInstance().player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(MinecraftClient.getInstance().player.currentScreenHandler.syncId));
        }
    }
}