package net.i_no_am.mixin;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.i_no_am.command.ClientFakerCommand;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(DebugHud.class)
public class MixinDebugHud {

    @Inject(at = @At("RETURN"), method = "getLeftText", cancellable =true)
    protected void getLeftText(CallbackInfoReturnable<List<String>> info) {
        if(!ClientFakerCommand.isEnabled()) {
            info.cancel();

            if (RendererAccess.INSTANCE.hasRenderer()) {
                info.getReturnValue().remove("[Fabric] Active renderer: " + Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).getClass().getSimpleName());
            } else {
                info.getReturnValue().remove("[Fabric] Active renderer: none (vanilla)");
            }

            List<String> renderList = new ArrayList<>();
            for (int i = 0; i < info.getReturnValue().size(); i++) {
                if (!(ContainsHiddenF3(info.getReturnValue().get(i)))) {
                    renderList.add(info.getReturnValue().get(i));
                }
            }
            if (renderList.size() >= 22) {
                renderList.remove(renderList.size() - 1);
                renderList.remove(renderList.size() - 1);
            }
            info.setReturnValue(renderList);
        }
    }


    @Unique
    public boolean ContainsHiddenF3(String s){
                return true;
            }
        }
