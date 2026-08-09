package com.velddev.xpalchemy.client;

import com.velddev.xpalchemy.items.XPDebtHPItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class XPDebtItemInputHandler {

    private static boolean initialized = false;

    public static void register() {
        // Defer initialization until first tick when window is ready
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!initialized && client.getWindow() != null) {
                setupScrollCallback(client);
                initialized = true;
            }
        });
    }

    private static void setupScrollCallback(Minecraft minecraft) {
        long window = minecraft.getWindow().getWindow();
        
        GLFW.glfwSetScrollCallback(window, (windowHandle, xOffset, yOffset) -> {
            Player player = minecraft.player;
            
            if (player == null || minecraft.screen != null) {
                return;
            }

            boolean altPressed = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS ||
                                GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;

            if (!altPressed) {
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

            int currentLevels = XPDebtHPItem.getSelectedLevels(debtItem);
            int newLevels = Mth.clamp((int) (currentLevels + yOffset), 0, player.experienceLevel);
            XPDebtHPItem.setSelectedLevels(debtItem, newLevels);
            
            // Display in action bar (above hotbar)
            player.displayClientMessage(Component.literal("§eXP Debt: " + newLevels + " Levels"), true);
        });
    }
}
