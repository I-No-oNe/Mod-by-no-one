package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import static net.i_no_am.NoOneMod.PREFIX;
import static net.i_no_am.client.ClientEntrypoint.FLY_HACK;

public class FlyHack extends ToggledModule {
    private boolean warningSent = false;
    private boolean flyingEnabled = false;

    public FlyHack() {
        super("Fly Hack", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        final ClientPlayerEntity player = client.player;
        if (player == null) return;

        if (FLY_HACK.enabled && !flyingEnabled) {
            player.getAbilities().flying = true;
            flyingEnabled = true;

            if (!warningSent) {
                player.sendMessage(Text.of(PREFIX + Formatting.RED + "Be aware that you might get banned, this is a really basic and simple fly hack"), false);
                warningSent = true;
            }
        } else if (!FLY_HACK.enabled && flyingEnabled) {

            player.getAbilities().flying = false;
            flyingEnabled = false;
            warningSent = false;
        }
    }
}
