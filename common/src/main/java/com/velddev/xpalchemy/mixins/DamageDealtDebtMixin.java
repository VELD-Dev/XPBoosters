package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Dealing damage pays back HP debt equal to half the damage dealt
@Mixin(LivingEntity.class)
public abstract class DamageDealtDebtMixin {

    @Inject(method = "hurt", at = @At("RETURN"))
    private void xpalchemy$reduceHpDebtOnDamageDealt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            return;
        }

        Entity attacker = source.getEntity();
        if (attacker instanceof Player player && !player.getAbilities().instabuild) {
            PlayerDebtData.reduceDebt(player, PlayerDebtData.DebtType.HP, amount / 2.0F);
        }
    }
}
