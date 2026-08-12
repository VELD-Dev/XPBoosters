package com.velddev.xpalchemy.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

// Single effect standing in for "you're running a Food debt" - replaces
// vanilla Saturation for the talisman's already-full-hunger case. Tick
// behaviour mirrors vanilla Saturation's own hardcoded handling
// (MobEffect#applyEffectTick / InstantenousMobEffect#isDurationEffectTick)
// exactly, just under our own name/icon instead of overloading the vanilla
// effect.
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
