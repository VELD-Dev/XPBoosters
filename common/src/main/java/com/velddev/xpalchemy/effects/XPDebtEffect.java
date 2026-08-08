package com.velddev.xpalchemy.effects;

import com.velddev.xpalchemy.CommonMain;
import com.velddev.xpalchemy.access.XpDebtHearts;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
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
        if(livingEntity instanceof Player player) {
            player.heal(player.getMaxHealth() - player.getHealth());
            int totalConsumedXp = getTotalConsumedXp(amplifier, player);
            float xpDebtHearts = getXpDebtHearts(totalConsumedXp);
            player.giveExperiencePoints(-totalConsumedXp);

            // Own pool, separate from vanilla absorption: re-applying XP Debt always
            // overwrites rather than stacking on top of itself or other absorption sources.
            ((XpDebtHearts) player).xpalchemy$setXpDebtHearts(xpDebtHearts);

            // Infinite duration from the start: applyEffectTick below decides when to
            // remove it based on the hearts pool, so there's no need to self-convert
            // from a finite duration (which previously meant removing and re-adding
            // the instance mid-tick, corrupting the entity's active-effects iteration).
            player.addEffect(new MobEffectInstance(this, -1, amplifier, false, true, true));
        }
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!livingEntity.hasEffect(this))
            return;

        if(((XpDebtHearts) livingEntity).xpalchemy$getXpDebtHearts() < 0.2f) {
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

    public static float getXpDebtHearts(int totalConsumedXp) {
        if (totalConsumedXp <= 0) {
            return 0.0F;
        }
        return CommonMain.roundToHalf(1 + (float) Math.log10(totalConsumedXp) * 7.06f);
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
