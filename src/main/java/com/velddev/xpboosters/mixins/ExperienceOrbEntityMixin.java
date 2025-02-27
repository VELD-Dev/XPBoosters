package com.velddev.xpboosters.mixins;

import com.velddev.xpboosters.Effects;
import com.velddev.xpboosters.Main;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    @ModifyArg(
            method = "onPlayerCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ExperienceOrbEntity;repairPlayerGears(Lnet/minecraft/entity/player/PlayerEntity;I)I"
            ),
            index = 1
    )
    private int multiplyXP(PlayerEntity player, int xpAmount) {
        StatusEffectInstance xpBoostEffect = player.getStatusEffect(Effects.XP_BOOST_EFFECT);

        if(xpBoostEffect != null) {
            int newXpValue = xpAmount * (xpBoostEffect.getAmplifier() + 2);
            xpAmount = MathHelper.clamp(newXpValue, 0, Integer.MAX_VALUE);
        }

        return xpAmount;
    }
}
