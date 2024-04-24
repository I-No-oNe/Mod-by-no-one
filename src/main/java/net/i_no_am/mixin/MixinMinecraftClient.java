package net.i_no_am.mixin;

import net.i_no_am.command.ClientFakerCommand;
import net.i_no_am.interfaces.MinecraftClientAccessor;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ModStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements MinecraftClientAccessor {

    /**
     * @author I-No-oNe
     * @reason fr
     */
    @Overwrite
    public static ModStatus getModStatus() {
        if (!ClientFakerCommand.isEnabled()) {
            return new ModStatus(ModStatus.Confidence.PROBABLY_NOT, "Client jar signature and brand is untouched");
        }
        return new ModStatus(ModStatus.Confidence.DEFINITELY, "Client jar signature and brand is untouched");
    }

    @Shadow
    protected abstract boolean doAttack();

    @Shadow
    protected abstract void doItemUse();

    @Override
    public void inputAttack() {
        this.doAttack();
    }

    @Override
    public void inputUse() {
        this.doItemUse();
    }

    @Inject(method = "getGameVersion", at = @At("HEAD"), cancellable = true)
    public void getGameVersion(CallbackInfoReturnable<String> we){
        if (!ClientFakerCommand.isEnabled()) {
            we.setReturnValue(SharedConstants.getGameVersion().getName());
        }
    }
    @Unique
    public boolean ContainsHiddenLogs(String s){;
//        for(int i = 0; i < Config.hiddenLogs.length; i++){
//            if (s.contains(Config.hiddenLogs[i])){
                return true;
            }

    @Inject(method = "getVersionType", at = @At("HEAD"), cancellable = true)
    public void getVersionType(CallbackInfoReturnable<String> callbackInfo) {
        if (!ClientFakerCommand.isEnabled()) {
            callbackInfo.setReturnValue("release");
        }
    }

    @Inject(at = @At("HEAD"), method = "run")
    protected void run(CallbackInfo info) {
    }

    @Unique
    private boolean containsHiddenLogs(String s) {
        return true; // You should implement the logic to determine if logs contain hidden information
    }

    @Inject(at = @At("HEAD"), method = "close")
    protected void close(CallbackInfo info)  {
//        ConfigFile.updateConfigFile();
        if (!ClientFakerCommand.isEnabled()) {
            return;
        }

        File file = new File("logs/latest.log");
        List<String> out;
        try {
            out = Files.lines(file.toPath())
                    .filter(line -> !ContainsHiddenLogs(line))
                    .collect(Collectors.toList());

            for (int i = 0; i < out.size(); i++) {
                if (out.get(i).toUpperCase().contains("MINECRAFT")) {
                    out.set(i, Pattern.compile(" (Minecraft)", Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(out.get(i))
                            .replaceAll(Matcher.quoteReplacement(":")));
                }
            }

            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
