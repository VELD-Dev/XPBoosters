package com.velddev.xpalchemy.client;

import com.velddev.xpalchemy.items.XPDebtHPItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "xpboosters", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class XPDebtItemInputHandler {

    @SubscribeEvent
    public static void onMouseScroll(ScreenEvent.MouseScrolled event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        // Only process when NOT in a screen (playing in world)
        if (player == null || event.getScreen() != null) {
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        ItemStack debtItem = null;
        if (mainHand.getItem() instanceof XPDebtHPItem) {
            debtItem = mainHand;
        } else if (offHand.getItem() instanceof XPDebtHPItem) {
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

        int currentLevels = XPDebtHPItem.getSelectedLevels(debtItem);
        int newLevels = (int) (currentLevels + event.getScrollDelta());
        XPDebtHPItem.setSelectedLevels(debtItem, newLevels);
        event.setCanceled(true); // Prevent default scroll behavior
    }
}
