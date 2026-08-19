package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.access.XpDebtHearts;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// XP Debt hearts are consumed before armor/magic reduction and absorption
@Mixin(Player.class)
public abstract class PlayerXpDebtMixin {

    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true)
    private float xpalchemy$consumeXpDebtHearts(float amount, DamageSource damageSource) {
        Player self = (Player) (Object) this;
        XpDebtHearts hearts = (XpDebtHearts) self;
        float current = hearts.xpalchemy$getXpDebtHearts();
        if (amount <= 0.0F || current <= 0.0F || self.isInvulnerableTo(damageSource)) {
            return amount;
        }

        float consumed = Math.min(current, amount);
        hearts.xpalchemy$setXpDebtHearts(current - consumed);
        return amount - consumed;
    }
}
