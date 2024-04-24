//package net.i_no_am.mixin;
//
//import net.minecraft.client.render.Camera;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.BlockView;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import static net.i_no_am.client.ClientEntrypoint.SCAFFOLD;
//
//@Mixin(Camera.class)
//public abstract class MixinCamera {
//
//    @Shadow private BlockView area;
//    @Shadow private Entity focusedEntity;
//    @Shadow private boolean thirdPerson;
//    @Shadow private float lastTickDelta;
//    @Shadow private double lastCameraY;
//    @Shadow private double cameraY;
//
//    @Shadow
//    protected void setRotation(float yaw, float pitch) {
//    }
//
//    @Shadow protected abstract void setPos(double x, double y, double z);
//
//    @Shadow private boolean ready;
//
//    // Inject into update method to handle camera position for freecam mode
//    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
//    private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
//        this.ready = true;
//        this.area = area;
//        this.focusedEntity = focusedEntity;
//        this.thirdPerson = thirdPerson;
//        this.lastTickDelta = tickDelta;
//
//        if (SCAFFOLD.enabled) {
//            // Set camera to look at 0 0 0 when scaffold module is enabled
//            double xDiff = 0 - focusedEntity.getX();
//            double yDiff = 0 - focusedEntity.getY();
//            double zDiff = 0 - focusedEntity.getZ();
//            double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
//
//            // Calculate yaw and pitch
//            float newYaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90.0);
//            float newPitch = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(yDiff, distance)));
//
//            this.setRotation(newYaw, newPitch);
//        } else {
//            // Set the regular rotation if scaffold is not enabled
//            this.setRotation(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));
//        }
//
//        this.setPos(MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()), MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY()) + MathHelper.lerp(tickDelta, this.lastCameraY, this.cameraY), MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
//
//        ci.cancel();
//    }
//}
