//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiHopper
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiCrafting
 *  net.minecraft.client.gui.inventory.GuiDispenser
 *  net.minecraft.client.gui.inventory.GuiFurnace
 *  net.minecraft.client.gui.inventory.GuiShulkerBox
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.server.SPacketHeldItemChange
 */
package me.ninethousand.violet.client.util.minecraft;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.impl.events.PacketEvent;
import me.ninethousand.violet.client.impl.modules.client.ClickGui;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketHeldItemChange;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int spoofedSlot = -1;

    public static boolean putInOffhand(Item item, boolean searchHotbar) {
        int inventoryPos = InventoryUtil.getInventoryPos(item, searchHotbar);
        if (inventoryPos == -1) {
            return false;
        }
        InventoryUtil.mc.playerController.windowClick(Constants.player().inventoryContainer.windowId, inventoryPos, 0, ClickType.PICKUP, (EntityPlayer)Constants.player());
        InventoryUtil.mc.playerController.windowClick(Constants.player().inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)Constants.player());
        InventoryUtil.mc.playerController.windowClick(Constants.player().inventoryContainer.windowId, inventoryPos, 0, ClickType.PICKUP, (EntityPlayer)Constants.player());
        return true;
    }

    public static int switchToItem(Item item, SwitchMode switchMode) {
        if (Constants.player().getHeldItemMainhand().getItem() == item) {
            return Constants.player().inventory.currentItem;
        }
        return InventoryUtil.switchToPos(InventoryUtil.getHotbarPos(item), switchMode);
    }

    public static int switchToPos(int pos, SwitchMode switchMode) {
        if (pos == -1) {
            return -1;
        }
        switch (switchMode) {
            case Client: {
                Constants.player().inventory.currentItem = pos;
                break;
            }
            case Silent: {
                if (pos == Constants.player().inventory.currentItem) {
                    return Constants.player().inventory.currentItem;
                }
                Constants.sendPacket(new CPacketHeldItemChange(pos));
            }
        }
        return pos;
    }

    public static boolean switchToSword(SwitchMode switchMode) {
        for (int i = 0; i < 9; ++i) {
            if (!(Constants.player().inventory.getStackInSlot(i).getItem() instanceof ItemSword)) continue;
            return InventoryUtil.switchToPos(i, switchMode) != -1;
        }
        return false;
    }

    public static boolean switchToShulker(SwitchMode switchMode) {
        for (int i = 0; i < 9; ++i) {
            if (!(Constants.player().inventory.getStackInSlot(i).getItem() instanceof ItemShulkerBox)) continue;
            return InventoryUtil.switchToPos(i, switchMode) != -1;
        }
        return false;
    }

    public static int getHotbarPos(Item item) {
        for (int i = 0; i < 9; ++i) {
            if (Constants.player().inventory.getStackInSlot(i).getItem() != item) continue;
            return i;
        }
        return -1;
    }

    public static int getInventoryPos(Item item, boolean searchHotbar) {
        int i;
        int n = i = searchHotbar ? 0 : 9;
        while (i < 45) {
            if (Constants.player().inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public static Item getServerItem() {
        if (spoofedSlot != -1) {
            return Constants.player().inventory.getStackInSlot(spoofedSlot).getItem();
        }
        return InventoryUtil.mc.player.getHeldItemMainhand().getItem();
    }

    public static boolean isPlayerInWorkstationGui() {
        return InventoryUtil.mc.currentScreen instanceof GuiChest || InventoryUtil.mc.currentScreen instanceof GuiShulkerBox || InventoryUtil.mc.currentScreen instanceof GuiDispenser || InventoryUtil.mc.currentScreen instanceof GuiShulkerBox || InventoryUtil.mc.currentScreen instanceof GuiHopper || InventoryUtil.mc.currentScreen instanceof GuiFurnace || InventoryUtil.mc.currentScreen instanceof GuiCrafting || ClickGui.getInstance().isEnabled();
    }

    public static Item getBestItem(Block block) {
        String tool = block.getHarvestTool(block.getDefaultState());
        if (tool != null) {
            switch (tool) {
                case "axe": {
                    return Items.DIAMOND_AXE;
                }
                case "shovel": {
                    return Items.DIAMOND_SHOVEL;
                }
            }
            return Items.DIAMOND_PICKAXE;
        }
        return Items.DIAMOND_PICKAXE;
    }

    public static int getSpoofedSlot() {
        return spoofedSlot;
    }

    @Listener
    public static void listen(PacketEvent event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            spoofedSlot = ((CPacketHeldItemChange)event.getPacket()).getSlotId();
        }
        if (event.getPacket() instanceof SPacketHeldItemChange) {
            spoofedSlot = ((SPacketHeldItemChange)event.getPacket()).getHeldItemHotbarIndex();
        }
    }

    private InventoryUtil() {
        throw new UnsupportedOperationException();
    }

    public static enum SwitchMode {
        None,
        Client,
        Silent;

    }
}

