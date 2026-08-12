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

// HP debt counter-effect: dealing damage to any entity pays back debt equal
// to half the damage dealt. Mixed into LivingEntity#hurt (the victim's side)
// rather than an attacker-side hook, since it needs to know the damage
// actually landed (Player#hurt calls super.hurt() so this fires for both
// mob and player victims).
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
