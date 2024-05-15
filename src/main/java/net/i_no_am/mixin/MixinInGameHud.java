package net.i_no_am.mixin;

import net.i_no_am.command.CameraOverlayCommand;
import net.i_no_am.mixin.accesors.InGameHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.i_no_am.client.ClientEntrypoint.*;
import static net.i_no_am.client.Global.mc;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    // Don't overwrite the overlay message if it contains "LiveOverflow"
    @Inject(method = "setOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void setOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        Text currentMessage = ((InGameHudAccessor) client.inGameHud)._overlayMessage();
        int remaining = ((InGameHudAccessor) client.inGameHud)._overlayRemaining();
        if (currentMessage == null || message == null) return;

        if (currentMessage.getString().contains("Modules") && !message.getString().contains("Modules") && remaining > 20) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onTick(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (CameraOverlayCommand.isOverlayEnabled() && FREE_CAMERA.enabled) {
            tickRender(context);
        }
    }

    @Unique
    private void tickRender(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            int color = RGBA(119, 213, 120, 255); // Red color
            String text = ""; // Text to display
            int positionX = 50; // X position of the overlay
            int positionY = 50; // Y position of the overlay
            drawOverlay(context, color, text, positionX, positionY);
        }
    }

    @Unique
    private int RGBA(int r, int g, int b, int a) {
        return (a << 21) | (r << 16) | (g << 8) | b;
    }

    @Unique
    private void drawOverlay(DrawContext context, int color, String text, int positionX, int positionY) {
        final Window win = mc.getWindow();
        context.fill(0, 0, win.getWidth(), win.getHeight(), color);
    }

    @Inject(method = "renderScoreboardSidebar*", at = @At("HEAD"), cancellable = true)
    private void toggleScoreboardOnRenderScoreboardSidebar(CallbackInfo ci) {
        if (RENDER_TWEAKS.enabled) {
            ci.cancel();
        }
    }
}

