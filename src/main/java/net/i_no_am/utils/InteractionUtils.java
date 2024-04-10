package net.i_no_am.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Objects;


/**
 * Client interaction utils
 */
public final class InteractionUtils {

    /**
     * Left clicks as if the player has inputted the click
     */
    public static void doAttack(MinecraftClient mc) {
        final HitResult hit = mc.crosshairTarget;
        final ClientPlayerInteractionManager im = mc.interactionManager;

        if (hit == null) return;

        switch (hit.getType()) {
            case ENTITY -> Objects.requireNonNull(im).attackEntity(mc.player, ((EntityHitResult) hit).getEntity());
            case BLOCK -> Objects.requireNonNull(im).attackBlock(((BlockHitResult) hit).getBlockPos(), ((BlockHitResult) hit).getSide());
        }

        Objects.requireNonNull(mc.player).swingHand(Hand.MAIN_HAND);
    }

    public static void doUse(MinecraftClient mc) {
        final HitResult hit = mc.crosshairTarget;
        final ClientPlayerInteractionManager im = mc.interactionManager;

        if (hit == null) return;

        switch (hit.getType()) {
            case ENTITY -> Objects.requireNonNull(im).interactEntity(mc.player, ((EntityHitResult) hit).getEntity(), Hand.MAIN_HAND);
            case BLOCK -> Objects.requireNonNull(im).interactBlock(mc.player, Hand.MAIN_HAND, (BlockHitResult) hit);
            case MISS -> Objects.requireNonNull(im).interactItem(mc.player, Hand.MAIN_HAND);
        }

        Objects.requireNonNull(mc.player).swingHand(Hand.MAIN_HAND);
    }
}
