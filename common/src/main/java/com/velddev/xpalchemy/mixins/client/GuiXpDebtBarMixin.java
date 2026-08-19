package com.velddev.xpalchemy.mixins.client;

import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// The debt bar takes the XP bar's HUD row; the XP bar and health/armor/food
// row above it get shifted up by DEBT_BAR_SHIFT_PX whenever there's debt.
@Mixin(Gui.class)
public abstract class GuiXpDebtBarMixin {
    private static final ResourceLocation BARS_TEXTURE = new ResourceLocation("minecraft", "textures/gui/bars.png");

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @ModifyConstant(method = "renderExperienceBar", constant = @Constant(intValue = 32))
    private int xpalchemy$shiftXpBarGraphic(int original) {
        return this.xpalchemy$hasVisibleDebt() ? original + PlayerDebtData.DEBT_BAR_SHIFT_PX : original;
    }

    @ModifyConstant(method = "renderExperienceBar", constant = @Constant(intValue = 31))
    private int xpalchemy$shiftXpLevelText(int original) {
        return this.xpalchemy$hasVisibleDebt() ? original + PlayerDebtData.DEBT_BAR_SHIFT_PX : original;
    }

    @ModifyConstant(method = "renderPlayerHealth", constant = @Constant(intValue = 39))
    private int xpalchemy$shiftHealthRow(int original) {
        return this.xpalchemy$hasVisibleDebt() ? original + PlayerDebtData.DEBT_BAR_SHIFT_PX : original;
    }

    @Inject(method = "renderExperienceBar", at = @At("TAIL"))
    private void xpalchemy$renderDebtBar(GuiGraphics guiGraphics, int x, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !PlayerDebtData.hasVisibleDebt(player)) {
            return;
        }

        int barX = this.screenWidth / 2 - 91;
        int barY = this.screenHeight - 29; // The XP bar's original, now-vacated slot
        int barWidth = 182;
        int barHeight = 5;

        guiGraphics.blit(BARS_TEXTURE, barX, barY, 0, 64, barWidth, barHeight);

        float hpDebt = PlayerDebtData.getHPDebt(player);
        float strengthDebt = PlayerDebtData.getStrengthDebt(player);
        float foodDebt = PlayerDebtData.getFoodDebt(player);

        int hpWidth = (int) ((hpDebt / 100.0F) * barWidth);
        int strengthWidth = (int) ((strengthDebt / 100.0F) * barWidth);
        int foodWidth = (int) ((foodDebt / 100.0F) * barWidth);

        int currentX = barX;

        if (hpWidth > 0) {
            guiGraphics.fill(currentX, barY, currentX + hpWidth, barY + barHeight, PlayerDebtData.DebtType.HP.color);
            currentX += hpWidth;
        }

        if (strengthWidth > 0) {
            guiGraphics.fill(currentX, barY, currentX + strengthWidth, barY + barHeight, PlayerDebtData.DebtType.STRENGTH.color);
            currentX += strengthWidth;
        }

        if (foodWidth > 0) {
            guiGraphics.fill(currentX, barY, currentX + foodWidth, barY + barHeight, PlayerDebtData.DebtType.FOOD.color);
        }
    }

    private boolean xpalchemy$hasVisibleDebt() {
        Player player = Minecraft.getInstance().player;
        return player != null && PlayerDebtData.hasVisibleDebt(player);
    }
}
