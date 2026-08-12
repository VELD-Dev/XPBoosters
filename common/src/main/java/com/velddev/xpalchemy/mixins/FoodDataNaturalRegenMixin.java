package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Strength debt side-effect: while it's outstanding, natural regeneration
// (both the fast saturation-driven heal and the slow food-level-driven heal
// in FoodData#tick) is suppressed. Redirecting Player#heal specifically
// (rather than cancelling the whole tick) leaves exhaustion/saturation
// depletion and starvation damage untouched - only the two natural-regen
// heal calls in this method are affected. XP Debt hearts (Health talisman)
// are unaffected since they never go through Player#heal from here.
@Mixin(FoodData.class)
public abstract class FoodDataNaturalRegenMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;heal(F)V"))
    private void xpalchemy$blockRegenWithStrengthDebt(Player player, float amount) {
        if (PlayerDebtData.getStrengthDebt(player) > 0.0F) {
            return;
        }
        player.heal(amount);
    }
}
