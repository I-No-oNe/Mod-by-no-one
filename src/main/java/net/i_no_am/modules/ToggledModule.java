package net.i_no_am.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.i_no_am.client.ClientEntrypoint.client;
import static net.i_no_am.client.ClientEntrypoint.networkHandler;


public abstract class ToggledModule {
    public static final String I_NO_AM_CATEGORY = "category.i_no_am";

    public final KeyBinding keybind;
    public final String name;
    public boolean defaultEnabled = false;
    public boolean enabled = defaultEnabled;

    /**
     * A hack that can be toggled on/off
     * @param name Display name of the hack
     * @param key The GLFW key code of the keybind
     */
    ToggledModule(String name, int key) {
        keybind = new KeyBinding("key.i_no_am." + this.getClass().getSimpleName().toLowerCase() + "_toggle",
                key, I_NO_AM_CATEGORY);
        this.name = name;
    }

    /**
     * Called every tick
     */
    public void tick(MinecraftClient client) {  // Called every tick
        if (keybind.wasPressed()) {
            toggle();
        }
        if (enabled && networkHandler != null) {  // networkHandler = Safety check
            tickEnabled();
        }
    }

    /**
     * Toggle the hack on/off
     */
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Called every tick, but only when the hack is enabled
     */
    void tickEnabled() {}

    /**
     * Called when the hack is enabled
     */
    public void enable() {
        enabled = true;
        message("§aON");
        LOGGER.info("Enabled " + name);
        onEnable();
    }
    /**
     * Overridable method to add extra functionality when the hack is enabled
     */
    void onEnable() {}

    /**
     * Called when the hack is disabled
     */
    public void disable() {
        enabled = false;
        message("§cOFF");
        LOGGER.info("Disabled " + name);
        onDisable();
    }
    /**
     * Overridable method to add extra functionality when the hack is disabled
     */
    void onDisable() {}

    /**
     * Send a message via the action bar with the prefix
     * @param message The message to send
     */
    public void message(String message) {
        if (client.player == null) return;

        message = "§7" + message.replace("§r", "§7");  // Make white text gray

        client.player.sendMessage(Text.of(  name + ": " + message), true);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
