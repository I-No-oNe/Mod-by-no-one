package net.i_no_am.client;

import net.i_no_am.command.*;
import net.i_no_am.modules.*;
import net.i_no_am.mixin.accesors.ClientConnectionInvoker;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.server.command.ServerCommandSource;

import java.util.LinkedList;

public class ClientEntrypoint implements ClientModInitializer {
    public static final NoArmorRender NO_ARMOR_RENDER = new NoArmorRender();

    public static final AutoAttack AUTO_ATTACK = new AutoAttack();

    public static final FastMine FAST_MINE = new FastMine();

    public static final RenderTweaks RENDER_TWEAKS = new RenderTweaks();

    public static final FlyHack FLY_HACK = new FlyHack();

    public static final Scaffold SCAFFOLD = new Scaffold();

    public static final BoatFly BOAT_FLY = new BoatFly();

public static final FreeCamera FREE_CAMERA = new FreeCamera();

    public static final NoSlow NO_SLOW = new NoSlow();

    public static final ElytraSwitch ELYTRA_SWITCH = new ElytraSwitch();


    public static final ToggledModule[] TOGGLED_MODULES = new ToggledModule[] {
            FLY_HACK,
            RENDER_TWEAKS,
            FAST_MINE,
            AUTO_ATTACK,
            BOAT_FLY,
            FREE_CAMERA,
            ELYTRA_SWITCH,
            NO_ARMOR_RENDER,
            NO_SLOW,
            SCAFFOLD
    };
    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static ClientPlayNetworkHandler networkHandler;
    public static int globalTimer = 0;
    public static final LinkedList<Packet<?>> packetQueue = new LinkedList<>();  // Max 5 per tick

    @Override
    public void onInitializeClient() {
        // Register functions for modules
        for (ToggledModule module : TOGGLED_MODULES) {
            KeyBindingHelper.registerKeyBinding(module.keybind);  // Keybinds
            ClientTickEvents.END_CLIENT_TICK.register(module::tick);  // Every tick
        }

        ClientTickEvents.END_CLIENT_TICK.register(ClientEntrypoint::tickEnd);  // End of every tick
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerClientCommands(dispatcher));  // Client Commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerServerCommands(dispatcher));  // Server Commands

        HudRenderCallback.EVENT.register(Gui::render);  // Render GUI
        new Notifier();
        new PlayerUsernameChecker();
    }


    public static void tickEnd(MinecraftClient client) {
        // Update variables
        networkHandler = client.getNetworkHandler();
        globalTimer++;

        // Send packets from queue (max 5)
        int movementPacketsLeft = 5;
        while (!packetQueue.isEmpty() && movementPacketsLeft > 0) {
            Packet<?> packet = packetQueue.remove(0);
            if (packet instanceof PlayerMoveC2SPacket || packet instanceof VehicleMoveC2SPacket) {
                movementPacketsLeft--;
            }
            ((ClientConnectionInvoker) networkHandler.getConnection())._sendImmediately(packet, null);
        }
    }

    public static void registerClientCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        ClipCommand.register(dispatcher);
        GuiCommand.register(dispatcher);
        ArmorRenderCommand.register(dispatcher);
        CameraOverlayCommand.register(dispatcher);
        CameraSpeedCommand.register(dispatcher);
        ClientFakerCommand.register(dispatcher);
    }



    public static void registerServerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        GetCodeCommand.register(dispatcher);
    }
}
