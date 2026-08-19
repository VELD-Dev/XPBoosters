package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

// Shared behaviour for all three talismans: pick levels via Alt+Scroll,
// preview cost/gain in the tooltip, convert to XP debt on use.
public abstract class XPDebtTalismanItem extends Item {

    private static final String NBT_SELECTED_LEVELS = "SelectedLevels";
    private final int cooldownTicks;

    protected XPDebtTalismanItem(Properties properties, int cooldownTicks) {
        super(properties);
        this.cooldownTicks = cooldownTicks;
    }

    public static int getSelectedLevels(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_SELECTED_LEVELS);
    }

    public static void setSelectedLevels(ItemStack stack, int levels) {
        stack.getOrCreateTag().putInt(NBT_SELECTED_LEVELS, Math.max(0, levels));
    }

    protected abstract PlayerDebtData.DebtType getDebtType();

    // debtGain is already capped to available room, do not cap it again
    protected abstract void applyTalismanEffect(Player player, int selectedLevels, float debtGain);

    protected abstract String tooltipKeyPrefix();

    // override when debt does not convert 1:1 into the talisman's benefit
    protected float previewBenefit(Player player, float debtGain) {
        return debtGain;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (PlayerDebtData.remainingCapacity(player) <= 0.0F) {
            return InteractionResultHolder.fail(stack);
        }

        int selectedLevels = getSelectedLevels(stack);
        if (selectedLevels <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        int consumedXp = PlayerDebtData.getTotalConsumedXp(selectedLevels, player);
        if (consumedXp <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        float rawGain = PlayerDebtData.calculateDebtGain(consumedXp);
        float cappedGain = Math.min(rawGain, PlayerDebtData.remainingCapacity(player, getDebtType()));
        if (cappedGain <= 0.0F) {
            return InteractionResultHolder.fail(stack);
        }

        // Cost never shrinks, only the benefit (cappedGain) does when capped
        if (!level.isClientSide) {
            // For some reason needs both to be roughly right idk why
            player.giveExperiencePoints(-consumedXp);
            player.giveExperienceLevels(-selectedLevels);
            PlayerDebtData.addDebt(player, getDebtType(), cappedGain);
            applyTalismanEffect(player, selectedLevels, cappedGain);

            player.getCooldowns().addCooldown(this, this.cooldownTicks);
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (level == null || !level.isClientSide()) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        String prefix = tooltipKeyPrefix();
        tooltip.add(Component.translatable(prefix + ".tooltip.hint").withStyle(ChatFormatting.GOLD));

        int selectedLevels = getSelectedLevels(stack);
        tooltip.add(Component.translatable(prefix + ".tooltip.selected_levels", selectedLevels).withStyle(ChatFormatting.YELLOW));

        if (PlayerDebtData.remainingCapacity(player) <= 0.0F) {
            tooltip.add(Component.translatable(prefix + ".tooltip.debt_full").withStyle(ChatFormatting.RED));
            return;
        }

        if (selectedLevels <= 0) {
            tooltip.add(Component.translatable(prefix + ".tooltip.insufficient").withStyle(ChatFormatting.RED));
            return;
        }

        int consumedXp = PlayerDebtData.getTotalConsumedXp(selectedLevels, player);
        float rawGain = PlayerDebtData.calculateDebtGain(consumedXp);
        float cappedGain = Math.min(rawGain, PlayerDebtData.remainingCapacity(player, getDebtType()));

        tooltip.add(Component.translatable(prefix + ".tooltip.cost", selectedLevels).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(prefix + ".tooltip.debt_gain", previewBenefit(player, cappedGain)).withStyle(ChatFormatting.GOLD));
        if (cappedGain < rawGain) {
            tooltip.add(Component.translatable(prefix + ".tooltip.capped").withStyle(ChatFormatting.RED));
        }
    }
}
