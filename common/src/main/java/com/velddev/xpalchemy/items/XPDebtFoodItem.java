package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

// Talisman of Hunger: fills the hunger bar if not full, otherwise grants
// the Food Debt effect (stand-in for Saturation). Food debt decays passively.
public class XPDebtFoodItem extends XPDebtTalismanItem {

    private static final int HUNGER_POINTS_PER_LEVEL = 6;
    private static final int TICKS_PER_DEBT_POINT = 60; // 3 seconds of Food Debt effect per debt point

    public XPDebtFoodItem(Properties properties) {
        super(properties, 20 * 5); // 5 second cooldown
    }

    @Override
    protected PlayerDebtData.DebtType getDebtType() {
        return PlayerDebtData.DebtType.FOOD;
    }

    @Override
    protected void applyTalismanEffect(Player player, int selectedLevels, float debtGain) {
        FoodData foodData = player.getFoodData();
        int currentFood = foodData.getFoodLevel();

        if (currentFood < 20) {
            int rawFill = selectedLevels * HUNGER_POINTS_PER_LEVEL;
            int actualFill = Math.min(rawFill, 20 - currentFood);
            foodData.setFoodLevel(currentFood + actualFill);
        } else {
            int duration = Math.round(debtGain * TICKS_PER_DEBT_POINT);
            player.addEffect(new MobEffectInstance(Effects.XP_FOOD_DEBT, duration, 0, false, true, true));
        }
    }

    @Override
    protected String tooltipKeyPrefix() {
        return "item.xpalchemy.xp_debt_crystal_food";
    }
}
