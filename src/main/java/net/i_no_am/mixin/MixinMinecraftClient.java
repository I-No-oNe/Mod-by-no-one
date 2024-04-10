package net.i_no_am.mixin;


import net.i_no_am.interfaces.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements MinecraftClientAccessor {

    @Shadow
    protected abstract boolean doAttack();
    @Shadow protected abstract void doItemUse();
@Override
public void inputAttack() {
    this.doAttack();
}

@Override
public void inputUse() {
    this.doItemUse();
}
}