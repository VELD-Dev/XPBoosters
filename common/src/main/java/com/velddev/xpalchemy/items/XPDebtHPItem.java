package com.velddev.xpalchemy.items;

import com.velddev.xpalchemy.Effects;
import com.velddev.xpalchemy.effects.XPDebtEffect;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

// Right-click alternative to the XP Health Debt potions: same effect and math
// (delegates straight to XPDebtEffect), but as an item the tooltip can preview
// the XP cost and hearts gained against the viewing player's current XP before
// they commit to using it.
// 
// Usage: Alt + Scroll to select levels, then hold right-click to activate with cooldown.
public class XPDebtHPItem extends Item {

    private static final String NBT_SELECTED_LEVELS = "SelectedLevels";
    public final int cooldown = 5;

    public XPDebtHPItem(Properties properties) {
        super(properties);
    }

    public static int getSelectedLevels(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_SELECTED_LEVELS);
    }

    public static void setSelectedLevels(ItemStack stack, int levels) {
        stack.getOrCreateTag().putInt(NBT_SELECTED_LEVELS, Math.max(1, Math.min(levels, 64)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int selectedLevels = getSelectedLevels(stack);
        
        if (selectedLevels <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            int totalConsumedXp = XPDebtEffect.getTotalConsumedXp(selectedLevels, player);
            if (totalConsumedXp <= 0) {
                return InteractionResultHolder.fail(stack);
            }

            Effects.XP_HEALTH_DEBT.applyInstantenousEffect(player, player, player, selectedLevels - 1, 1.0D);
            player.getCooldowns().addCooldown(this, 20*5);  // 5 seconds cooldown
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Max hold time (60 seconds)
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

        int selectedLevels = getSelectedLevels(stack);
        tooltip.add(Component.literal("§6§lHold Alt + Scroll§r to select levels").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("§eSelected Levels: " + selectedLevels).withStyle(ChatFormatting.YELLOW));

        if (selectedLevels <= 0) {
            tooltip.add(Component.translatable("item.xpalchemy.xp_debt_crystal.tooltip.insufficient").withStyle(ChatFormatting.RED));
            return;
        }

        int totalConsumedXp = XPDebtEffect.getTotalConsumedXp(selectedLevels, player);
        float hearts = XPDebtEffect.getXpDebtHearts(totalConsumedXp);
        tooltip.add(Component.translatable("item.xpalchemy.xp_debt_crystal.tooltip.cost", totalConsumedXp).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.xpalchemy.xp_debt_crystal.tooltip.hearts", hearts).withStyle(ChatFormatting.GOLD));
    }
}
