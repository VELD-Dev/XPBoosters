package com.velddev.xpalchemy.mixins;

import com.velddev.xpalchemy.Constants;
import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Shadow
    private int value;

    // Collecting XP pays back Strength debt 1-for-1
    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void xpalchemy$reduceStrengthDebtOnXpPickup(Player entity, CallbackInfo ci) {
        if (!entity.level().isClientSide && entity.takeXpDelay == 0) {
            PlayerDebtData.reduceDebt(entity, PlayerDebtData.DebtType.STRENGTH, this.value);
        }
    }

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
