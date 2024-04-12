package net.i_no_am.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class MixinDeathScreen {

    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ClientPlayerEntity player = client.player;
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            String message = String.format("§aNo one's mod:§r You died at X: %.2f, Y: %.2f, Z: %.2f", x, y, z);
            client.player.sendMessage(Text.literal(message), false);
        }
    }
}
