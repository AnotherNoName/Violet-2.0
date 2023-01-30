//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 *  org.lwjgl.input.Mouse
 */
package me.ninethousand.violet.client.impl.modules.player;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.client.FriendUtil;
import me.ninethousand.violet.client.util.minecraft.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

@Module.Manifest(value=Module.Category.PLAYER, description="Add a friend or throw a pearl")
public class MiddleClick
extends Module {
    @Setting
    private Action action = Action.Friend;
    @Setting
    private InventoryUtil.SwitchMode switchMode = InventoryUtil.SwitchMode.Silent;
    private boolean lastDown;
    private static MiddleClick instance;

    @Override
    protected void onEnable() {
        this.lastDown = false;
    }

    @Listener
    public void listen(UpdateEvent event) {
        if (this.action == Action.Friend) {
            this.attemptAddFriend();
        } else if (this.action == Action.Pearl) {
            this.attemptThrowPearl();
        } else if (Constants.mc.objectMouseOver.entityHit == null) {
            this.attemptThrowPearl();
        } else {
            this.attemptAddFriend();
        }
    }

    private void attemptAddFriend() {
        boolean isDown = Mouse.isButtonDown((int)2);
        if (Constants.mc.objectMouseOver != null && Constants.mc.objectMouseOver.entityHit != null && isDown && !this.lastDown) {
            boolean friendNow = FriendUtil.toggleFriend(Constants.mc.objectMouseOver.entityHit.getUniqueID());
            if (friendNow) {
                this.chat("Added friend: '" + Constants.mc.objectMouseOver.entityHit.getName() + "'.", 0);
            } else {
                this.chat("Removed friend: '" + Constants.mc.objectMouseOver.entityHit.getName() + "'.", 0);
            }
        }
        this.lastDown = isDown;
    }

    private void attemptThrowPearl() {
        boolean offhand;
        if (InventoryUtil.isPlayerInWorkstationGui()) {
            return;
        }
        boolean isDown = Mouse.isButtonDown((int)2);
        int pearlSlot = InventoryUtil.getHotbarPos(Items.ENDER_PEARL);
        boolean bl = offhand = Constants.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if ((pearlSlot != -1 || offhand) && isDown && !this.lastDown) {
            int oldSlot = Constants.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToPos(pearlSlot, this.switchMode);
            }
            Constants.mc.playerController.processRightClick((EntityPlayer)Constants.mc.player, (World)Constants.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToPos(oldSlot, InventoryUtil.SwitchMode.Client);
            }
        }
        this.lastDown = isDown;
    }

    public static MiddleClick getInstance() {
        if (instance == null) {
            instance = new MiddleClick();
        }
        return instance;
    }

    private static enum Action {
        Friend,
        Pearl,
        Both;

    }
}

