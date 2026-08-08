package com.velddev.xpalchemy.mixins.client;

import com.velddev.xpalchemy.Constants;
import com.velddev.xpalchemy.access.XpDebtHearts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Continues the vanilla health/absorption heart row with XP Debt hearts,
// exactly like vanilla continues health hearts with absorption hearts: same
// row, same wrap-at-10 and same dynamic row-height shrink, just picking up
// at the slot right after the last health/absorption heart.
@Mixin(Gui.class)
public abstract class GuiXpDebtHeartsMixin {

    @Unique
    private static final ResourceLocation XPALCHEMY$TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/xp_debt_hearts.png");
    @Unique
    private static final int XPALCHEMY$TEXTURE_WIDTH = 27;
    @Unique
    private static final int XPALCHEMY$TEXTURE_HEIGHT = 9;
    @Unique
    private static final int XPALCHEMY$ICON_SIZE = 9;
    @Unique
    private static final int XPALCHEMY$HEARTS_PER_ROW = 10;

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "renderPlayerHealth", at = @At("TAIL"))
    private void xpalchemy$renderXpDebtHearts(GuiGraphics guiGraphics, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        float xpDebtHearts = ((XpDebtHearts) player).xpalchemy$getXpDebtHearts();
        if (xpDebtHearts < 0.2F) {
            return;
        }

        float maxHealth = Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH), player.getHealth());
        int absorptionAmount = Mth.ceil(player.getAbsorptionAmount());
        int healthSlots = Mth.ceil(maxHealth / 2.0F);
        int absorptionSlots = Mth.ceil(absorptionAmount / 2.0F);
        int rowsNeeded = Mth.ceil((maxHealth + (float) absorptionAmount) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (rowsNeeded - 2), 3);

        int left = this.screenWidth / 2 - 91;
        int top = this.screenHeight - 39;
        int startSlot = healthSlots + absorptionSlots;
        int xpDebtSlots = Mth.ceil(xpDebtHearts / 2.0F);

        float remaining = xpDebtHearts;
        for (int i = 0; i < xpDebtSlots; i++) {
            int slot = startSlot + i;
            int x = left + (slot % XPALCHEMY$HEARTS_PER_ROW) * 8;
            int y = top - (slot / XPALCHEMY$HEARTS_PER_ROW) * rowHeight;

            guiGraphics.blit(XPALCHEMY$TEXTURE, x, y, 0.0F, 0.0F, XPALCHEMY$ICON_SIZE, XPALCHEMY$ICON_SIZE, XPALCHEMY$TEXTURE_WIDTH, XPALCHEMY$TEXTURE_HEIGHT);
            int u = remaining >= 2.0F ? XPALCHEMY$ICON_SIZE * 2 : XPALCHEMY$ICON_SIZE;
            guiGraphics.blit(XPALCHEMY$TEXTURE, x, y, (float) u, 0.0F, XPALCHEMY$ICON_SIZE, XPALCHEMY$ICON_SIZE, XPALCHEMY$TEXTURE_WIDTH, XPALCHEMY$TEXTURE_HEIGHT);
            remaining -= 2.0F;
        }
    }
}
