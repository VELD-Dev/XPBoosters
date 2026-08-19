package com.velddev.xpalchemy.effects;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

// Stand-in for vanilla Strength + Haste, applied with infinite duration and
// self-removed once the Strength debt it represents hits 0.
public class XPDebtStrengthEffect extends MobEffect {

    private static final String ATTACK_DAMAGE_MODIFIER_UUID = "8f3f9dcd-c560-4874-8bf0-8614e853c6c0";

    public XPDebtStrengthEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_UUID, 3.0D, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && PlayerDebtData.getStrengthDebt(player) <= 0.0F) {
            entity.removeEffect(this);
        }
    }
}
