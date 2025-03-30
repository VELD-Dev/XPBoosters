package com.velddev.xpalchemy.effects;

import com.velddev.xpalchemy.CommonMain;
import com.velddev.xpalchemy.Constants;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class XPDebtEffect extends MobEffect {

    public XPDebtEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        if(livingEntity instanceof Player player) {
            player.heal(player.getMaxHealth() - player.getHealth());
            int totalConsumedXp = getTotalConsumedXp(amplifier, player);
            Constants.LOGGER.info("Consumed XP: {}", totalConsumedXp);
            float absorption = CommonMain.roundToHalf(1 + (float)Math.log10(totalConsumedXp) * 7.06f);
            player.giveExperiencePoints(-totalConsumedXp);
            float baseAbsorption = 0;
            baseAbsorption = player.getAbsorptionAmount();

            if(player.hasEffect(this) && baseAbsorption < absorption) {
                player.setAbsorptionAmount(absorption);
            } else if(player.hasEffect(this)){
                player.setAbsorptionAmount(absorption);
            } else {
                absorption += baseAbsorption;
                player.setAbsorptionAmount(absorption);
            }

            Constants.LOGGER.info("Absorption Level: {}", absorption);

            player.addEffect(new MobEffectInstance(this, 10, amplifier, false, true, true));
            var effect = player.getEffect(this);
            Constants.LOGGER.info("[SPAWN] XP DEBT EFFECT VISIBLE: {}", effect.showIcon());
        }
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!livingEntity.hasEffect(this))
            return;

        var effect = livingEntity.getEffect(this);
        if(!effect.isInfiniteDuration()) {
            livingEntity.removeEffect(this);
            livingEntity.addEffect(new MobEffectInstance(this, -1, amplifier, false, true, true));
        }

        if(livingEntity.getAbsorptionAmount() < 0.2f) {
            livingEntity.removeEffect(this);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    public static int getTotalConsumedXp(int amplifier, Player player) {
        int baseLevel = player.experienceLevel;
        int baseXp = Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
        int consumedLevels = Mth.clamp(Math.round(player.experienceLevel * 0.2f * (amplifier + 1)), 0, player.experienceLevel + 1);
        int totalConsumedXp = baseXp;
        for(int i = 1; i < consumedLevels; i++)
        {
            int xp = 0;
            if (baseLevel - i >= 30) {
                xp = 112 + (baseLevel - i - 30) * 9;
            } else {
                xp = baseLevel - i >= 15 ? 37 + (baseLevel - i - 15) * 5 : 7 + (baseLevel - i) * 2;
            }
            totalConsumedXp += xp;
        }
        return totalConsumedXp;
    }
}
