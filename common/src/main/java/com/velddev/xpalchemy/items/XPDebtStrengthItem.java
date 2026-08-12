package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

// Talisman of Strength: borrows XP for the Strength Debt effect (attack
// damage + mining speed in one, see XPDebtStrengthEffect). The amplifier
// scales with debtGain, which is itself derived from the XP injected - so a
// bigger XP dump means a stronger buff, at the cost of proportionally more
// Strength debt. The effect itself is applied with an infinite duration and
// lasts exactly as long as the debt does (self-removed by XPDebtStrengthEffect
// the moment the debt hits 0), rather than a fixed timer. While Strength debt
// is outstanding, natural regeneration is blocked (see
// FoodDataNaturalRegenMixin) - the debt is paid back passively by collecting
// XP (see ExperienceOrbMixin).
public class XPDebtStrengthItem extends XPDebtTalismanItem {

    public XPDebtStrengthItem(Properties properties) {
        super(properties, 20 * 5); // 5 second cooldown
    }

    @Override
    protected PlayerDebtData.DebtType getDebtType() {
        return PlayerDebtData.DebtType.STRENGTH;
    }

    @Override
    protected void applyTalismanEffect(Player player, int selectedLevels, float debtGain) {
        int amplifier = debtGain >= 20.0F ? 2 : debtGain >= 10.0F ? 1 : 0;
        player.addEffect(new MobEffectInstance(Effects.XP_STRENGTH_DEBT, MobEffectInstance.INFINITE_DURATION, amplifier, false, true, true));
    }

    @Override
    protected String tooltipKeyPrefix() {
        return "item.xpalchemy.xp_debt_crystal_strength";
    }
}
