package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

// Talisman of Strength: grants the Strength Debt effect (attack + mining
// speed), amplifier scaling with debtGain. Lasts as long as the debt does.
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
