package com.velddev.xpalchemy.effects;

import com.velddev.xpalchemy.CommonMain;
import com.velddev.xpalchemy.Constants;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class XPDebtEffect extends MobEffect {

    public XPDebtEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        super.applyInstantenousEffect(source, indirectSource, livingEntity, amplifier, health);
        if(livingEntity instanceof Player player) {
            int totalConsumedXp = getTotalConsumedXp(amplifier, player);
            Constants.LOGGER.info("Consumed XP: {}", totalConsumedXp);
            float absorption = CommonMain.roundToHalf(1 + (float)Math.log10(totalConsumedXp) * 7.06f);
            player.giveExperiencePoints(-totalConsumedXp);
            float baseAbsorption = 0;
            if(!player.hasEffect(this)) {
                baseAbsorption = player.getAbsorptionAmount();
            }
            Constants.LOGGER.info("Absorption Level: {}", (baseAbsorption + absorption));
            player.setAbsorptionAmount(baseAbsorption + absorption);
        }
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity.getAbsorptionAmount() < 1 && livingEntity.hasEffect(this)) {
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
