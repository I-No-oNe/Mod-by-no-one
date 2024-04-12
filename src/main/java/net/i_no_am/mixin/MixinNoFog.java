//package net.i_no_am.mixin;

//import net.minecraft.client.option.GameOptions;
//import net.minecraft.client.render.BackgroundRenderer;
//import net.minecraft.client.world.ClientWorld;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//
//@Mixin(BackgroundRenderer.class)
//public class MixinNoFog {
//
//    @Unique
//    private static float method_23792(ClientWorld world, float f) {
//        return 0.0F; // Set fog density to 0 to remove fog
//    }
//
//    @Unique
//    private static float getFogDistance(GameOptions options, float viewDistance) {
//        return 0.0F; // Set fog distance to 0 to remove fog
//    }
//
//    @Unique
//    private static boolean hasHorizonFog(GameOptions options) {
//        return false; // Disable horizon fog
//    }
//}
//