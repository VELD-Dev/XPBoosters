package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.access.XpDebtHearts;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.entity.player.Player;

// Talisman of Health: heals to full and grants XP Debt hearts.
// Hearts get more expensive the more the player already has (soft-cap curve).
public class XPDebtHPItem extends XPDebtTalismanItem {

    private static final float HEART_COST_SOFT_CAP = 20.0F; // half-hearts (10 hearts)

    public XPDebtHPItem(Properties properties) {
        super(properties, 20 * 5); // 5 second cooldown
    }

    @Override
    protected PlayerDebtData.DebtType getDebtType() {
        return PlayerDebtData.DebtType.HP;
    }

    @Override
    protected void applyTalismanEffect(Player player, int selectedLevels, float debtGain) {
        Effects.XP_HEALTH_DEBT.grantHearts(player, previewBenefit(player, debtGain));
    }

    @Override
    protected float previewBenefit(Player player, float debtGain) {
        float currentHearts = ((XpDebtHearts) player).xpalchemy$getXpDebtHearts();
        float efficiency = HEART_COST_SOFT_CAP / (HEART_COST_SOFT_CAP + currentHearts);
        return debtGain * efficiency;
    }

    @Override
    protected String tooltipKeyPrefix() {
        return "item.xpalchemy.xp_debt_crystal_hp";
    }
}
