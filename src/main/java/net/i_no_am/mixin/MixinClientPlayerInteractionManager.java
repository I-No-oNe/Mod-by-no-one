package net.i_no_am.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.i_no_am.client.ClientEntrypoint.FAST_MINE;
import static net.i_no_am.client.ClientEntrypoint.INVENTORY_TWEAKS;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager{

    @Shadow
    private int blockBreakingCooldown;

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "attackBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I"))
    public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (client != null && FAST_MINE.enabled) {
            blockBreakingCooldown = 0;
        }
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", ordinal = 1))
    public void updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (client != null && FAST_MINE.enabled) {
            blockBreakingCooldown = 0;
        }
    }
    @Unique
    private boolean pauseListening = false;

    @Inject(method = "clickSlot", at = @At("HEAD"))
    public void clickSlotHook(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if ((INVENTORY_TWEAKS.enabled && isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))
                && (isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL))
                && actionType == SlotActionType.THROW
                && !pauseListening) {
            Item copy = MinecraftClient.getInstance().player.currentScreenHandler.slots.get(slotId).getStack().getItem();
            pauseListening = true;
            for (int i2 = 0; i2 < MinecraftClient.getInstance().player.currentScreenHandler.slots.size(); ++i2) {
                if (INVENTORY_TWEAKS.enabled && MinecraftClient.getInstance().player.currentScreenHandler.slots.get(i2).getStack().getItem() == copy)
                    MinecraftClient.getInstance().interactionManager.clickSlot(MinecraftClient.getInstance().player.currentScreenHandler.syncId, i2, 1, SlotActionType.THROW, MinecraftClient.getInstance().player);
            }
            pauseListening = false;
        }
    }

    @Unique
    public boolean isKeyPressed(int button) {
        if (INVENTORY_TWEAKS.enabled && button == -1)
            return false;
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), button);
    }
}

