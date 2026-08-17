package com.velddev.xpalchemy.client;

import com.velddev.xpalchemy.items.XPDebtTalismanItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;

@Environment(EnvType.CLIENT)
public class XPDebtItemInputHandler {

    private static boolean initialized = false;

    // glfwSetScrollCallback() *replaces* whatever callback was previously
    // installed - which, by the time this runs, is vanilla's own
    // MouseHandler::onScroll (hotbar switching, GUI scroll, spectator, map
    // zoom, etc). Discarding the return value here (as the original code
    // did) permanently silences all of that; it must be kept and chained.
    private static GLFWScrollCallback previousCallback;

    public static void register() {
        // Defer initialization until first tick when window is ready
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!initialized && client.getWindow().getWindow() != 0L) {
                setupScrollCallback(client);
                initialized = true;
            }
        });
    }

    private static void setupScrollCallback(Minecraft minecraft) {
        long window = minecraft.getWindow().getWindow();

        previousCallback = GLFW.glfwSetScrollCallback(window, (windowHandle, xOffset, yOffset) -> {
            if (!tryHandleScroll(minecraft, windowHandle, yOffset) && previousCallback != null) {
                previousCallback.invoke(windowHandle, xOffset, yOffset);
            }
        });
    }

    private static boolean tryHandleScroll(Minecraft minecraft, long windowHandle, double yOffset) {
        Player player = minecraft.player;

        if (player == null || minecraft.screen != null) {
            return false;
        }

        boolean altPressed = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS ||
                            GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;

        if (!altPressed) {
            return false;
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
            return false;
        }

        int currentLevels = XPDebtTalismanItem.getSelectedLevels(debtItem);
        int newLevels = Mth.clamp((int) (currentLevels + yOffset), 0, player.experienceLevel);
        XPDebtTalismanItem.setSelectedLevels(debtItem, newLevels);

        // Display in action bar (above hotbar)
        player.displayClientMessage(Component.literal("§eXP Debt: " + newLevels + " Levels"), true);
        return true;
    }
}
