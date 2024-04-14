package net.i_no_am.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashTextResourceSupplier {

    @Unique
    @Final @Mutable private List<String> splashes = new ArrayList<>(); // Initialize the splashes list

    @Unique
    private static final String[] customSplashes = {
            "Try ClickCrystals!",
            "Made by I-No-oNe/i_no_am",
            "We will never know if it's working ig"
    };

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("RETURN"))
    public void reloadMixin(List<String> list, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        Random random = new Random();
        String name = "Player";
        int randomIndex = random.nextInt(customSplashes.length);
        String splash = customSplashes[randomIndex].replace("%name", name);
        this.setSplashes(splash);
    }

    @Unique
    private void setSplashes(String splash) {
        this.splashes.clear();
        this.splashes.add(splash);
    }
}