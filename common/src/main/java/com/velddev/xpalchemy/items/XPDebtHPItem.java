package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.access.XpDebtHearts;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.entity.player.Player;

// Talisman of Health: borrows XP to heal to full and grant XP Debt hearts
// (see XpDebtHearts / GuiXpDebtHeartsMixin) on top of vanilla health, capped
// by the room left in the HP debt pool.
//
// Hearts get progressively more expensive the more of them the player
// already has: the same debtGain buys fewer hearts as xpalchemy$getXpDebtHearts()
// grows, via a soft-cap efficiency curve (100% at 0 hearts, 50% at
// HEART_COST_SOFT_CAP hearts, 33% at double that, etc). This keeps stacking
// Health debt from being strictly better than diversifying into Strength/Food -
// the HP debt pool still fills at the "sticker" debtGain rate even though the
// hearts you actually receive taper off, so overinvesting in Health wastes
// shared pool room for diminishing return.
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
