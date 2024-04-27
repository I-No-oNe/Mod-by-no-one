package net.i_no_am.mixin;


import net.i_no_am.modules.InventoryTweaks;
import net.i_no_am.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static net.i_no_am.client.ClientEntrypoint.INVENTORY_TWEAKS;


@Mixin(value = {HandledScreen.class})
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {

    @Unique
    private final TimerUtils delayTimer = new TimerUtils();

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slotIn, double mouseX, double mouseY);

    @Shadow
    protected abstract void onMouseClick(Slot slotIn, int slotId, int mouseButton, SlotActionType type);

    @Inject(method = "render", at = @At("HEAD"))
    private void drawScreenHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (int i1 = 0; i1 < Objects.requireNonNull(MinecraftClient.getInstance().player).currentScreenHandler.slots.size(); ++i1) {
            Slot slot = MinecraftClient.getInstance().player.currentScreenHandler.slots.get(i1);
            if  (INVENTORY_TWEAKS.enabled && isPointOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                if(INVENTORY_TWEAKS.enabled && iWantToSleep()) {
                    if (attack() && delayTimer.passedMs(90)) {
                        this.onMouseClick(slot, slot.id, 0, SlotActionType.QUICK_MOVE);
                        delayTimer.reset();
                    }
                }
            }
        }
    }

    @Unique
    private boolean iWantToSleep() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344);
    }

@Unique
    private boolean attack() {
        return InventoryTweaks.hold_mouse0;
    }
}