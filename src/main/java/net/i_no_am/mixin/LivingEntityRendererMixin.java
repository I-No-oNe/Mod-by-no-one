package net.i_no_am.mixin;

import net.i_no_am.utils.TargetUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.i_no_am.client.ClientEntrypoint.RENDER_TWEAKS;

@Mixin(net.minecraft.client.render.entity.LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    public <T extends LivingEntity> void render(T entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (TargetUtils.isTarget(entity)) {
            TargetUtils.highlight(matrices);
        }
    }

    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
    public void hasLabel(LivingEntity ent, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (RENDER_TWEAKS.enabled && mc.getCameraEntity() == ent) {
            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }
    }
}
