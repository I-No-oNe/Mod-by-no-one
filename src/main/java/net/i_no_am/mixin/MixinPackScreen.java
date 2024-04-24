package net.i_no_am.mixin;

import net.i_no_am.command.ClientFakerCommand;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

    @Mixin(PackScreen.class)
    abstract class MixinPackScreen {
        @Shadow
        private long refreshTimeout;

        @Unique
        private boolean currentShowResourcePack = ClientFakerCommand.isEnabled();

        @Inject(method = "tick", at = @At("HEAD"))
        private void tick(CallbackInfo ci) {

            boolean previousShowResourcePack = currentShowResourcePack;
            currentShowResourcePack = !ClientFakerCommand.isEnabled();
            // Refresh gui the next second like in vanilla minecraft:
            if (previousShowResourcePack != currentShowResourcePack) {
                this.refreshTimeout = SharedConstants.TICKS_PER_SECOND;
            }
        }

        @Redirect(method = "updatePackLists", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/pack/ResourcePackOrganizer;getEnabledPacks()Ljava/util/stream/Stream;"))
        public Stream<ResourcePackOrganizer.Pack> updateSelectedPackList(ResourcePackOrganizer organizer) {
            Stream<ResourcePackOrganizer.Pack> packs = organizer.getEnabledPacks();
            if (ClientFakerCommand.isEnabled()) {
                packs = packs.filter(pack -> !pack.getDisplayName().getString().contains("Fabric Mod"));
            }
            return packs;
        }
    }