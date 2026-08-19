package com.velddev.xpalchemy.data;

import com.velddev.xpalchemy.CommonMain;
import com.velddev.xpalchemy.access.PlayerDebt;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PlayerDebtData {

    public static final float MAX_DEBT_PER_TYPE = 100.0F;
    public static final float MAX_TOTAL_DEBT = 100.0F;

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

    public static float remainingCapacity(Player player) {
        return Math.max(0.0F, MAX_TOTAL_DEBT - getTotalDebt(player));
    }

    public static float remainingCapacity(Player player, DebtType type) {
        return Math.max(0.0F, Math.min(MAX_DEBT_PER_TYPE - getDebt(player, type), remainingCapacity(player)));
    }

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

    public static float reduceDebt(Player player, DebtType type, float amount) {
        if (amount <= 0.0F) {
            return 0.0F;
        }

        float current = getDebt(player, type);
        float toRemove = Math.min(amount, current);
        setDebtValue(player, type, current - toRemove);
        return toRemove;
    }

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

    public static float calculateDebtGain(int consumedXp) {
        if (consumedXp <= 0) {
            return 0.0F;
        }
        return CommonMain.roundToHalf(1 + (float) Math.log10(consumedXp) * 7.06f);
    }

    public static int getTotalConsumedXp(int consumedLevels, Player player) {
        int baseLevel = player.experienceLevel;
        consumedLevels = Mth.clamp(consumedLevels, 0, baseLevel);
        return totalXpToLevel(baseLevel) - totalXpToLevel(baseLevel - consumedLevels);
    }

    public static int totalXpToLevel(int level) {
        int xp = 0;
        for (int i = 0; i < level; i++) {
            if(i <= 15) {
                xp += i * 2 + 7;
            } else if(i <= 30) {
                xp += i * 5 - 38;
            } else {
                xp += i * 9 - 158;
            }
        }
        return xp;
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
