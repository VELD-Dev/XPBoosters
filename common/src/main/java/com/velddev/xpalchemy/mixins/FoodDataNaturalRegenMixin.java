package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Natural regeneration is suppressed while Strength debt is outstanding
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
