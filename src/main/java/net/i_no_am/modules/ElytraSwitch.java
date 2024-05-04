package net.i_no_am.modules;

import com.google.common.collect.ImmutableSet;
import net.i_no_am.utils.InteractionUtils;
import net.i_no_am.utils.SwitchUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.Set;

import static net.i_no_am.client.ClientEntrypoint.ELYTRA_SWITCH;
import static net.minecraft.item.Items.ELYTRA;

public class ElytraSwitch extends ToggledModule {

    private static final Set<ArmorItem> CHESTPLATES = ImmutableSet.of(
            (ArmorItem) Items.LEATHER_CHESTPLATE,
            (ArmorItem) Items.IRON_CHESTPLATE,
            (ArmorItem) Items.GOLDEN_CHESTPLATE,
            (ArmorItem) Items.DIAMOND_CHESTPLATE,
            (ArmorItem) Items.NETHERITE_CHESTPLATE);

    public ElytraSwitch() {
        super("Elytra Switch", GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void tick(MinecraftClient client) {
        super.tick(client);
        if (client.player == null) return;
        if (ELYTRA_SWITCH.enabled) {
            ItemStack chestplateStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
            boolean isElytraEquipped = chestplateStack.getItem() == ELYTRA;
            if (isElytraEquipped && client.player.isOnGround()) {
                for (ArmorItem chestplate : CHESTPLATES) {
                    if (SwitchUtils.search(chestplate)) {
                        InteractionUtils.inputUse();
                    }
                }
            }
        }
    }
}
