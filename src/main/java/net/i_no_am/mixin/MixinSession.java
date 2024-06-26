package net.i_no_am.mixin;

import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Mixin(Session.class)
public class MixinSession {
    @Unique
    private final HashMap<String, HashSet<String>> names = new HashMap<>() {{
        put("frfrf", new HashSet<>(List.of("Notch", "i_no_am", "Someone", "ImproperIssues", "github", "im tired", "Sleep", "sleeP", "What is it?")));
        // Put your own fun name in here :)
    }};

    @Inject(method = "getUsername", at = @At("RETURN"), cancellable = true)
    private void getUsername(CallbackInfoReturnable<String> cir) {
        String name = cir.getReturnValue();
        if (name != null && names.containsKey(name)) {
            String random = (String) names.get(name).toArray()[(int) (Math.random() * names.get(name).size())];
            cir.setReturnValue(random);
        }
    }
}
