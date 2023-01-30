//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package me.ninethousand.violet.client.impl.modules.player;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.InventoryUtil;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

@Module.Manifest(value=Module.Category.PLAYER, description="Get ur blockgame funds up")
public class AutoDuper
extends Module {
    @Setting
    private DupeMode mode = DupeMode.ItemFrame;
    @Setting(min=0.0, max=2000.0)
    private int delay = 60;
    @Setting
    private boolean onlyShulkers = false;
    private Timer delayTimer;
    private static AutoDuper instance;

    @Override
    protected void onEnable() {
        this.delayTimer = new Timer();
    }

    @Override
    protected void onDisable() {
        this.delayTimer = null;
    }

    @Listener
    private void listen(UpdateEvent event) {
        switch (this.mode) {
            case ItemFrame: {
                this.doFrameDupe();
            }
        }
    }

    private void doFrameDupe() {
        boolean air;
        RayTraceResult looking = Constants.mc.objectMouseOver;
        if (looking == null || looking.typeOfHit != RayTraceResult.Type.ENTITY || looking.entityHit == null) {
            return;
        }
        Entity lookingEntity = Constants.mc.world.getEntityByID(looking.entityHit.getEntityId());
        if (!(lookingEntity instanceof EntityItemFrame)) {
            return;
        }
        boolean bl = air = ((EntityItemFrame)lookingEntity).getDisplayedItem().getItem() == Items.field_190931_a;
        if (this.onlyShulkers && !(Constants.mc.player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox) && air) {
            if (!InventoryUtil.switchToShulker(InventoryUtil.SwitchMode.Client)) {
                this.chat("No Shulkers in Hotbar!", 0);
                this.toggle();
            }
            return;
        }
        if (this.delayTimer.passed(this.delay) && !air) {
            Constants.mc.playerController.attackEntity((EntityPlayer)Constants.mc.player, lookingEntity);
            this.delayTimer.reset();
        } else {
            Constants.mc.playerController.interactWithEntity((EntityPlayer)Constants.mc.player, lookingEntity, EnumHand.MAIN_HAND);
        }
    }

    public static AutoDuper getInstance() {
        if (instance == null) {
            instance = new AutoDuper();
        }
        return instance;
    }

    public static enum DupeMode {
        ItemFrame;

    }
}

