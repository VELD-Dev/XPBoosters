package com.velddev.xpalchemy.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

// Stand-in for vanilla Saturation, mirrors its tick behaviour exactly.
public class XPDebtFoodEffect extends MobEffect {

    public XPDebtFoodEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player && !player.level().isClientSide) {
            player.getFoodData().eat(amplifier + 1, 1.0F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }
}
