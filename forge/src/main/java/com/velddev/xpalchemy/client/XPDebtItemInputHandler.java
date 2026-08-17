package com.velddev.xpalchemy.client;

import com.velddev.xpalchemy.items.XPDebtTalismanItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "xpboosters", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class XPDebtItemInputHandler {

    // InputEvent.MouseScrollingEvent only fires when there's no screen open
    // (unlike ScreenEvent.MouseScrolled, which requires one to even be
    // constructed - subscribing to that one meant this handler could never
    // run during normal gameplay). Only cancel it - and thus suppress
    // vanilla's own hotbar-switch handling - when we actually consume the
    // scroll ourselves; otherwise let it fall through untouched.
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) {
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        ItemStack debtItem = null;
        if (mainHand.getItem() instanceof XPDebtTalismanItem) {
            debtItem = mainHand;
        } else if (offHand.getItem() instanceof XPDebtTalismanItem) {
            debtItem = offHand;
        }

        if (debtItem == null) {
            return;
        }

        boolean altPressed = GLFW.glfwGetKey(client.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS ||
                            GLFW.glfwGetKey(client.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;

        if (!altPressed) {
            return;
        }

        int currentLevels = XPDebtTalismanItem.getSelectedLevels(debtItem);
        int newLevels = Mth.clamp((int) (currentLevels + event.getScrollDelta()), 0, player.experienceLevel);
        XPDebtTalismanItem.setSelectedLevels(debtItem, newLevels);
        event.setCanceled(true); // Prevent default scroll behavior
    }
}
