package com.velddev.xpalchemy.data;

import com.velddev.xpalchemy.CommonMain;
import com.velddev.xpalchemy.access.PlayerDebt;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * Facade over the three debt pools (HP, Strength, Food) backed by
 * {@link com.velddev.xpalchemy.mixins.PlayerDebtMixin}.
 * Each type: 0-100. Sum of all three types: max 100 - filling one type
 * shrinks the room left for the others.
 */
public class PlayerDebtData {

    public static final float MAX_DEBT_PER_TYPE = 100.0F;
    public static final float MAX_TOTAL_DEBT = 100.0F;

    /**
     * How many pixels the debt bar occupies on the HUD (bar height + gaps),
     * used by GuiXpDebtBarMixin/GuiXpDebtHeartsMixin to shift the XP bar and
     * the health/armor/food/air row up to make room for it, instead of
     * drawing on top of the XP bar.
     */
    public static final int DEBT_BAR_SHIFT_PX = 7;

    public static float getHPDebt(Player player) {
        return ((PlayerDebt) player).xpalchemy$getHpDebt();
    }

    public static float getStrengthDebt(Player player) {
        return ((PlayerDebt) player).xpalchemy$getStrengthDebt();
    }

    public static float getFoodDebt(Player player) {
        return ((PlayerDebt) player).xpalchemy$getFoodDebt();
    }

    public static float getDebt(Player player, DebtType type) {
        return switch (type) {
            case HP -> getHPDebt(player);
            case STRENGTH -> getStrengthDebt(player);
            case FOOD -> getFoodDebt(player);
        };
    }

    public static float getTotalDebt(Player player) {
        return getHPDebt(player) + getStrengthDebt(player) + getFoodDebt(player);
    }

    public static boolean hasVisibleDebt(Player player) {
        return getTotalDebt(player) >= 0.1F;
    }

    /**
     * Room left in the shared 100-point pool, ignoring per-type caps.
     */
    public static float remainingCapacity(Player player) {
        return Math.max(0.0F, MAX_TOTAL_DEBT - getTotalDebt(player));
    }

    /**
     * Room left for a specific type: bounded by both its own 0-100 range and
     * whatever space the other two types have left in the shared pool.
     */
    public static float remainingCapacity(Player player, DebtType type) {
        return Math.max(0.0F, Math.min(MAX_DEBT_PER_TYPE - getDebt(player, type), remainingCapacity(player)));
    }

    /**
     * Adds debt to a specific type, respecting both its own cap and the
     * shared total cap. Returns the amount actually added (may be less than
     * requested - this is the "talisman effect is ceiled at the remaining
     * empty debt left" rule).
     */
    public static float addDebt(Player player, DebtType type, float amount) {
        if (amount <= 0.0F) {
            return 0.0F;
        }

        float toAdd = Math.min(amount, remainingCapacity(player, type));
        if (toAdd <= 0.0F) {
            return 0.0F;
        }

        setDebtValue(player, type, getDebt(player, type) + toAdd);
        return toAdd;
    }

    /**
     * Reduces debt for a specific type - used by the per-type counter-effects
     * (block break/damage dealt for HP, XP collection for Strength, passive
     * decay for Food). Never pushes below 0.
     */
    public static float reduceDebt(Player player, DebtType type, float amount) {
        if (amount <= 0.0F) {
            return 0.0F;
        }

        float current = getDebt(player, type);
        float toRemove = Math.min(amount, current);
        setDebtValue(player, type, current - toRemove);
        return toRemove;
    }

    /**
     * Directly sets a type's debt, clamped to [0,100] but NOT to the shared
     * pool cap - unlike addDebt, this can leave the total above 100. Meant
     * for debugging (/xpalchemy debt set); gameplay code should use addDebt.
     */
    public static void setDebt(Player player, DebtType type, float value) {
        setDebtValue(player, type, Mth.clamp(value, 0.0F, MAX_DEBT_PER_TYPE));
    }

    public static void clearAllDebts(Player player) {
        setDebtValue(player, DebtType.HP, 0.0F);
        setDebtValue(player, DebtType.STRENGTH, 0.0F);
        setDebtValue(player, DebtType.FOOD, 0.0F);
    }

    private static void setDebtValue(Player player, DebtType type, float value) {
        PlayerDebt debt = (PlayerDebt) player;
        switch (type) {
            case HP -> debt.xpalchemy$setHpDebt(value);
            case STRENGTH -> debt.xpalchemy$setStrengthDebt(value);
            case FOOD -> debt.xpalchemy$setFoodDebt(value);
        }
    }

    /**
     * Shared "how much debt does borrowing this much XP create" curve, used
     * by the Health and Strength talismans (Food doesn't borrow XP at all).
     */
    public static float calculateDebtGain(int consumedXp) {
        if (consumedXp <= 0) {
            return 0.0F;
        }
        return CommonMain.roundToHalf(1 + (float) Math.log10(consumedXp) * 7.06f);
    }

    /**
     * XP valuation of {@code consumedLevels} whole levels below the player's
     * current level (baseLevel-1 down to baseLevel-consumedLevels) - i.e.
     * exactly what giveExperienceLevels(-consumedLevels) actually gives back,
     * same as vanilla's own anvil enchant-cost bookkeeping. Deliberately
     * doesn't touch whatever fractional progress is sitting in the current
     * level bar: the actual charge is applied via giveExperienceLevels
     * (whole levels only, see XPDebtTalismanItem), not by subtracting this
     * many raw XP points - going through giveExperiencePoints instead would
     * make the actual level drop drift from what's shown, since it rescales
     * progress using a different per-level cost at each step.
     */
    public static int getTotalConsumedXp(int consumedLevels, Player player) {
        int baseLevel = player.experienceLevel;
        consumedLevels = Mth.clamp(consumedLevels, 0, baseLevel);
        int totalConsumedXp = 0;
        for (int i = 1; i <= consumedLevels; i++) {
            int level = baseLevel - i;
            int xp;
            if (level >= 30) {
                xp = 112 + (level - 30) * 9;
            } else {
                xp = level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
            }
            totalConsumedXp += xp;
        }
        return totalConsumedXp;
    }

    public enum DebtType {
        HP(0xFFFF3333),        // Red
        STRENGTH(0xFFFF9933),  // Orange
        FOOD(0xFFFFCC33);      // Yellow

        public final int color;

        DebtType(int color) {
            this.color = color;
        }
    }
}
