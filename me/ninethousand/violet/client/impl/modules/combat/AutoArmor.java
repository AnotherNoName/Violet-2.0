//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 */
package me.ninethousand.violet.client.impl.modules.combat;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.TryUseItemEvent;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.client.FriendUtil;
import me.ninethousand.violet.client.util.minecraft.CombatUtil;
import me.ninethousand.violet.client.util.minecraft.EntityUtil;
import me.ninethousand.violet.client.util.minecraft.InventoryUtil;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@Module.Manifest(value=Module.Category.COMBAT, description="Automatically put on armor")
public class AutoArmor
extends Module {
    @Setting(min=0.0, max=750.0)
    private int delay = 60;
    @Setting
    private boolean singleMend = false;
    @Setting(min=0.0, max=100.0)
    private int mendThreshold = 60;
    @Setting(min=0.0, max=750.0)
    private int mendDelay = 60;
    @Setting
    private boolean mendCancel = false;
    @Setting(min=0.0, max=50.0)
    private int enemyRange = 20;
    @Setting
    private boolean crystalCheck = true;
    @Setting
    private boolean swap = false;
    @Setting(min=0.0, max=100.0)
    private int swapThreshold = 10;
    @Setting
    private boolean binding = false;
    private Timer armorTimer = new Timer();
    private Timer mendTimer = new Timer();
    private boolean finishedMending = false;
    private static AutoArmor instance;

    @Listener
    private void listen(TryUseItemEvent event) {
        if (event.getStack().getItem() == Items.EXPERIENCE_BOTTLE) {
            if (this.checkDurability() && this.mendCancel && this.singleMend) {
                event.cancel();
                return;
            }
            this.mendTimer.reset();
        }
    }

    @Listener
    private void listen(UpdateEvent event) {
        int slot;
        if (Constants.mc.currentScreen instanceof GuiInventory || InventoryUtil.isPlayerInWorkstationGui()) {
            return;
        }
        if (this.singleMend && !this.mendTimer.passed(this.mendDelay) && InventoryUtil.getServerItem() == Items.EXPERIENCE_BOTTLE) {
            int[] toMove = new int[]{-1, -1, -1, -1};
            for (int slot2 = 5; slot2 <= 8; ++slot2) {
                int repaired;
                ItemStack item = Constants.mc.player.inventoryContainer.getSlot(slot2).getStack();
                if (item == null) continue;
                int n = repaired = 100.0f * (float)(item.getMaxDamage() - item.getItemDamage()) / (float)item.getMaxDamage() >= (float)this.mendThreshold ? 1 : 0;
                if (repaired == 0 || !(item.getItem() instanceof ItemArmor)) continue;
                toMove[slot2 - 5] = slot2;
            }
            if (!this.isLethalCrystals() && !this.isEnemies()) {
                for (int slot3 : toMove) {
                    if (slot3 == -1) continue;
                    this.move(slot3);
                }
            }
            return;
        }
        ItemStack helm = Constants.mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = Constants.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = Constants.mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack feet = Constants.mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (this.swap) {
            if (helm.getItem() != Items.field_190931_a && 100.0f * (float)(helm.getMaxDamage() - helm.getItemDamage()) / (float)helm.getMaxDamage() <= (float)this.swapThreshold && (slot = this.findBetterArmorSlot(EntityEquipmentSlot.HEAD)) != -1) {
                this.swap(slot, 5);
            }
            if (chest.getItem() != Items.field_190931_a && 100.0f * (float)(chest.getMaxDamage() - chest.getItemDamage()) / (float)chest.getMaxDamage() <= (float)this.swapThreshold && (slot = this.findBetterArmorSlot(EntityEquipmentSlot.CHEST)) != -1) {
                this.swap(slot, 6);
            }
            if (legs.getItem() != Items.field_190931_a && 100.0f * (float)(legs.getMaxDamage() - legs.getItemDamage()) / (float)legs.getMaxDamage() <= (float)this.swapThreshold && (slot = this.findBetterArmorSlot(EntityEquipmentSlot.LEGS)) != -1) {
                this.swap(slot, 7);
            }
            if (feet.getItem() != Items.field_190931_a && 100.0f * (float)(feet.getMaxDamage() - feet.getItemDamage()) / (float)feet.getMaxDamage() <= (float)this.swapThreshold && (slot = this.findBetterArmorSlot(EntityEquipmentSlot.FEET)) != -1) {
                this.swap(slot, 8);
            }
        }
        if (helm.getItem() == Items.field_190931_a && (slot = this.findArmorSlot(EntityEquipmentSlot.HEAD)) != -1) {
            this.move(slot);
        }
        if (chest.getItem() == Items.field_190931_a && (slot = this.findArmorSlot(EntityEquipmentSlot.CHEST)) != -1) {
            this.move(slot);
        }
        if (legs.getItem() == Items.field_190931_a && (slot = this.findArmorSlot(EntityEquipmentSlot.LEGS)) != -1) {
            this.move(slot);
        }
        if (feet.getItem() == Items.field_190931_a && (slot = this.findArmorSlot(EntityEquipmentSlot.FEET)) != -1) {
            this.move(slot);
        }
    }

    private boolean checkDurability() {
        for (int slot = 5; slot <= 8; ++slot) {
            boolean repaired;
            ItemStack item = Constants.mc.player.inventoryContainer.getSlot(slot).getStack();
            if (item == null || !(item.getItem() instanceof ItemArmor)) continue;
            boolean bl = repaired = 100.0f * (float)(item.getMaxDamage() - item.getItemDamage()) / (float)item.getMaxDamage() >= (float)this.mendThreshold;
            if (repaired) continue;
            return false;
        }
        return true;
    }

    private boolean isEnemies() {
        return Constants.mc.world.playerEntities.stream().anyMatch(player -> Constants.mc.player.getDistanceToEntity((Entity)player) < (float)this.enemyRange && !FriendUtil.isFriend(player) && player != Constants.mc.player);
    }

    private boolean isLethalCrystals() {
        return this.crystalCheck && Constants.mc.world.loadedEntityList.stream().anyMatch(entity -> entity instanceof EntityEnderCrystal && CombatUtil.getDamage((Entity)Constants.mc.player, (EntityEnderCrystal)entity) >= EntityUtil.getHealth((EntityLivingBase)Constants.mc.player));
    }

    private int findArmorSlot(EntityEquipmentSlot type) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            boolean cursed;
            ItemStack item = Constants.mc.player.inventoryContainer.getSlot(i).getStack();
            if (item == null || !(item.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)item.getItem();
            if (armor.armorType != type) continue;
            float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)item);
            boolean bl = cursed = this.binding && EnchantmentHelper.func_190938_b((ItemStack)item);
            if (!(currentDamage > damage) || cursed) continue;
            damage = currentDamage;
            slot = i;
        }
        return slot;
    }

    private int findBetterArmorSlot(EntityEquipmentSlot type) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            boolean cursed;
            ItemStack item = Constants.mc.player.inventoryContainer.getSlot(i).getStack();
            if (item == null || !(item.getItem() instanceof ItemArmor) || 100.0f * (float)(item.getMaxDamage() - item.getItemDamage()) / (float)item.getMaxDamage() <= (float)this.swapThreshold) continue;
            ItemArmor armor = (ItemArmor)item.getItem();
            if (armor.armorType != type) continue;
            float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)item);
            boolean bl = cursed = this.binding && EnchantmentHelper.func_190938_b((ItemStack)item);
            if (!(currentDamage > damage) || cursed) continue;
            damage = currentDamage;
            slot = i;
        }
        return slot;
    }

    private void move(int slot) {
        if (!this.armorTimer.passed(this.delay)) {
            return;
        }
        Constants.mc.playerController.windowClick(Constants.mc.player.inventoryContainer.windowId, slot, 0, ClickType.QUICK_MOVE, (EntityPlayer)Constants.mc.player);
        Constants.mc.playerController.updateController();
        this.armorTimer.reset();
    }

    private void swap(int from, int to) {
        if (!this.armorTimer.passed(this.delay)) {
            return;
        }
        Constants.mc.playerController.windowClick(Constants.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Constants.mc.player);
        Constants.mc.playerController.windowClick(Constants.mc.player.inventoryContainer.windowId, to, 0, ClickType.PICKUP, (EntityPlayer)Constants.mc.player);
        Constants.mc.playerController.windowClick(Constants.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Constants.mc.player);
        Constants.mc.playerController.updateController();
        this.armorTimer.reset();
    }

    private void chuck(int slot) {
        if (!this.armorTimer.passed(this.delay)) {
            return;
        }
        Constants.mc.playerController.windowClick(Constants.mc.player.inventoryContainer.windowId, slot, 0, ClickType.THROW, (EntityPlayer)Constants.mc.player);
        Constants.mc.playerController.updateController();
        this.armorTimer.reset();
    }

    public static AutoArmor getInstance() {
        if (instance == null) {
            instance = new AutoArmor();
        }
        return instance;
    }
}

