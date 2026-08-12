package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.Effects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Mining speed isn't attribute-driven in 1.20.1 - vanilla Haste is checked
// by identity in Player#getDestroySpeed via MobEffectUtil. This mirrors that
// same multiplier (1 + (amplifier+1)*0.2) for the Strength Debt effect
// instead of also granting vanilla Haste.
@Mixin(Player.class)
public abstract class PlayerMiningSpeedDebtMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void xpalchemy$applyStrengthDebtMiningSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player self = (Player) (Object) this;
        MobEffectInstance instance = self.getEffect(Effects.XP_STRENGTH_DEBT);
        if (instance != null) {
            float factor = 1.0F + (float) (instance.getAmplifier() + 1) * 0.2F;
            cir.setReturnValue(cir.getReturnValue() * factor);
        }
    }
}
