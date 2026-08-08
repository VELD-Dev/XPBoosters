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
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

// Right-click alternative to the XP Health Debt potions: same effect and math
// (delegates straight to XPDebtEffect), but as an item the tooltip can preview
// the XP cost and hearts gained against the viewing player's current XP before
// they commit to using it.
public class XPDebtItem extends Item {

    private final int amplifier;

    public XPDebtItem(int amplifier, Properties properties) {
        super(properties);
        this.amplifier = amplifier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int totalConsumedXp = XPDebtEffect.getTotalConsumedXp(this.amplifier, player);
        if (totalConsumedXp <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            Effects.XP_HEALTH_DEBT.applyInstantenousEffect(player, player, player, this.amplifier, 1.0D);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
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

        int totalConsumedXp = XPDebtEffect.getTotalConsumedXp(this.amplifier, player);
        if (totalConsumedXp <= 0) {
            tooltip.add(Component.translatable("item.xpalchemy.xp_debt_crystal.tooltip.insufficient").withStyle(ChatFormatting.RED));
            return;
        }

        float hearts = XPDebtEffect.getXpDebtHearts(totalConsumedXp) / 2.0F;
        tooltip.add(Component.translatable("item.xpalchemy.xp_debt_crystal.tooltip.cost", totalConsumedXp).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.xpalchemy.xp_debt_crystal.tooltip.hearts", hearts).withStyle(ChatFormatting.GOLD));
    }
}
