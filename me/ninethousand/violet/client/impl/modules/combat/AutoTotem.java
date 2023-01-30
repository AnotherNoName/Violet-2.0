//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemSword
 *  org.lwjgl.input.Mouse
 */
package me.ninethousand.violet.client.impl.modules.combat;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import me.ninethousand.violet.client.util.minecraft.EntityUtil;
import me.ninethousand.violet.client.util.minecraft.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;

@Module.Manifest(value=Module.Category.COMBAT, description="Makes you not die (maybe)")
public class AutoTotem
extends Module {
    @Setting
    private ItemChoice item = ItemChoice.Crystal;
    @Setting(min=0.1, max=36.0)
    private double minHP = 12.0;
    @Setting(min=0.1, max=36.0)
    private double holeHP = 12.0;
    @Setting
    private int delayTicks = 10;
    @Setting
    private boolean searchHotbar = false;
    @Setting
    private boolean gapSwitch = false;
    @Setting
    private boolean swordGap = false;
    @Setting
    private boolean rightClickGap = false;
    private int waited = 0;
    private static AutoTotem instance;

    @Override
    protected void onEnable() {
        this.waited = this.delayTicks;
    }

    @Listener
    public void listen(UpdateEvent event) {
        if (this.waited++ < this.delayTicks) {
            return;
        }
        Item item = this.getItem();
        if (Constants.mc.player.getHeldItemOffhand().getItem() == item) {
            return;
        }
        if (InventoryUtil.putInOffhand(item, this.searchHotbar)) {
            this.waited = 0;
        }
    }

    private Item getItem() {
        double d = EntityUtil.getHealth((EntityLivingBase)Constants.mc.player);
        double d2 = BlockUtil.isSurrounded(EntityUtil.getEntityPos((Entity)Constants.mc.player)) ? this.holeHP : this.minHP;
        if (d < d2) {
            return Items.field_190929_cY;
        }
        if (this.gapSwitch) {
            if (this.swordGap && Constants.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                return Items.GOLDEN_APPLE;
            }
            if (this.rightClickGap && Mouse.isButtonDown((int)1)) {
                return Items.GOLDEN_APPLE;
            }
        }
        return this.item.item;
    }

    public static AutoTotem getInstance() {
        if (instance == null) {
            instance = new AutoTotem();
        }
        return instance;
    }

    public static enum ItemChoice {
        Crystal(Items.END_CRYSTAL),
        GoldenApple(Items.GOLDEN_APPLE),
        Totem(Items.field_190929_cY);

        private final Item item;

        private ItemChoice(Item item) {
            this.item = item;
        }
    }
}

