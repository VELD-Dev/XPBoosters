package com.velddev.xpboosters.mixins;

import com.velddev.xpboosters.Constants;
import com.velddev.xpboosters.Effects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @ModifyArg(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ExperienceOrb;repairPlayerItems(Lnet/minecraft/world/entity/player/Player;I)I"
            ),
            index = 1
    )
    private int multiplyXP(Player player, int xpAmount) {
        MobEffectInstance xpBoostEffect = player.getEffect(Effects.XP_BOOST_EFFECT);

        Constants.LOGGER.info("Will boost XP ?");

        if(xpBoostEffect != null) {
            int newXpValue = xpAmount * (xpBoostEffect.getAmplifier() + 2);
            Constants.LOGGER.info("Boosting XP from {} to {} !", xpAmount, newXpValue);
            xpAmount = Math.clamp(newXpValue, 0, Integer.MAX_VALUE);
        }

        return xpAmount;
    }
}
