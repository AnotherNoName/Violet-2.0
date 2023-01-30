//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.ninethousand.violet.client.impl.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.managers.RotationManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.client.FriendUtil;
import me.ninethousand.violet.client.util.minecraft.AngleUtil;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import me.ninethousand.violet.client.util.minecraft.CombatUtil;
import me.ninethousand.violet.client.util.minecraft.SwingHand;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Manifest(value=Module.Category.COMBAT, description="Push crystals with pistons to kill the opps")
public class PistonAura
extends Module {
    @Setting(min=1.0, max=6.0)
    private double range = 4.0;
    @Setting(min=1.0, max=500.0)
    private long delay = 30L;
    @Setting
    private boolean antiSuicide = true;
    @Setting
    private boolean mine = true;
    @Setting
    private boolean extraChecks = true;
    @Setting
    private boolean onlyHoles = false;
    @Setting
    private Rotation.Rotate rotation = Rotation.Rotate.PACKET;
    @Setting
    private boolean fastAsFuck = false;
    @Setting(min=1.0, max=15.0)
    private int fuckFactor = 4;
    private Stage stage = Stage.FindingTarget;
    private EntityPlayer target;
    private EntityEnderCrystal crystal;
    private BlockPos crystalPos;
    private BlockPos pistonPos;
    private BlockPos redstonePos;
    private EnumFacing pistonDirection;
    private Vec3d rotateTarget;
    private final Timer delayTimer = new Timer();
    private Runnable action;
    private int originalSlot = -1;
    private int crystalSlot = -1;
    private int pistonSlot = -1;
    private int redstoneSlot = -1;
    private int pickaxeSlot = -1;
    private static PistonAura instance;

    @Override
    protected void onEnable() {
        if (Constants.nullCheck()) {
            return;
        }
        this.stage = Stage.FindingTarget;
        this.crystalPos = null;
        this.pistonPos = null;
        this.redstonePos = null;
        this.action = null;
        this.originalSlot = -1;
        this.crystalSlot = -1;
        this.pickaxeSlot = -1;
        this.redstoneSlot = -1;
        this.pistonSlot = -1;
        this.rotateTarget = null;
    }

    @Override
    protected void onDisable() {
    }

    @Listener
    private void listen(UpdateEvent event) {
        if (Constants.nullCheck()) {
            return;
        }
        if (this.target != null) {
            this.setInfo(this.target.getName());
        }
        if (this.fastAsFuck) {
            for (int i = 0; i < this.fuckFactor; ++i) {
                this.doPistonAura();
                if (this.action != null) {
                    this.action.run();
                    this.action = null;
                }
                if (this.rotateTarget == null) {
                    return;
                }
                Rotation angles = AngleUtil.calculateAngles(this.rotateTarget);
                RotationManager.get().addRotation(new Rotation(angles.getYaw(), angles.getPitch(), this.rotation));
            }
        } else {
            this.doPistonAura();
            if (this.action != null && this.delayTimer.passed(this.delay)) {
                this.action.run();
                this.action = null;
            }
            if (this.rotateTarget == null) {
                return;
            }
            Rotation angles = AngleUtil.calculateAngles(this.rotateTarget);
            RotationManager.get().addRotation(new Rotation(angles.getYaw(), angles.getPitch(), this.rotation, 80.0f));
        }
    }

    @Listener
    private void listen(Render3DEvent event) {
        if (Constants.nullCheck()) {
            return;
        }
    }

    private void doPistonAura() {
        block0 : switch (this.stage) {
            case FindingTarget: {
                for (EntityPlayer target : this.getTargets()) {
                    if (!this.checkTargetAndSetupPositions(target)) continue;
                    this.stage = Stage.Piston;
                    break block0;
                }
                break;
            }
            case Piston: {
                if (!this.checkTargetAndSetupPositions(this.target)) {
                    this.stage = Stage.FindingTarget;
                    break;
                }
                this.rotateTarget = new Vec3d((Vec3i)this.pistonPos).addVector(0.5, 0.0, 0.5).add(new Vec3d(this.pistonDirection.getDirectionVec()).scale(0.5));
                this.action = () -> {
                    if (!this.finishedRotation()) {
                        return;
                    }
                    this.delayTimer.reset();
                    boolean isSprinting = Constants.mc.player.isSprinting();
                    boolean shouldSneak = BlockUtil.shouldSneakWhileRightClicking(this.pistonPos.down());
                    this.pistonSlot = this.getPistonSlot();
                    if (this.pistonSlot == -1) {
                        this.chat("No pistons found!", 0);
                        this.setEnabled(!this.isEnabled());
                        return;
                    }
                    this.originalSlot = Constants.mc.player.inventory.currentItem;
                    if (this.originalSlot != this.pistonSlot) {
                        Constants.mc.player.inventory.currentItem = this.pistonSlot;
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.pistonSlot));
                    }
                    if (isSprinting) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    if (shouldSneak) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    BlockUtil.placeBlock(this.pistonPos, true);
                    if (isSprinting) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    }
                    if (shouldSneak) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    this.stage = Stage.Crystal;
                };
                break;
            }
            case Crystal: {
                this.rotateTarget = new Vec3d((Vec3i)this.crystalPos.down()).addVector(0.5, 0.0, 0.5);
                this.action = () -> {
                    if (!this.finishedRotation() && !this.fastAsFuck) {
                        return;
                    }
                    this.delayTimer.reset();
                    EnumHand hand = EnumHand.MAIN_HAND;
                    if (Constants.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                        hand = EnumHand.OFF_HAND;
                    } else {
                        this.crystalSlot = this.getCrystalSlot();
                        if (this.crystalSlot == -1) {
                            this.chat("No crystals found!", 0);
                            this.setEnabled(!this.isEnabled());
                            return;
                        }
                        if (Constants.mc.player.inventory.currentItem != this.crystalSlot) {
                            Constants.mc.player.inventory.currentItem = this.crystalSlot;
                            Constants.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.crystalSlot));
                        }
                    }
                    BlockUtil.placeCrystal(this.crystalPos.down(), EnumFacing.UP, hand, this.rotateTarget, false);
                    this.stage = Stage.Redstone;
                };
                break;
            }
            case Redstone: {
                this.rotateTarget = new Vec3d((Vec3i)this.redstonePos).addVector(0.5, 0.0, 0.5);
                this.action = () -> {
                    if (!this.finishedRotation() && !this.fastAsFuck) {
                        return;
                    }
                    this.delayTimer.reset();
                    boolean isSprinting = Constants.mc.player.isSprinting();
                    boolean shouldSneak = BlockUtil.shouldSneakWhileRightClicking(this.pistonPos.down());
                    this.redstoneSlot = this.getRedstoneBlockSlot();
                    if (this.redstoneSlot == -1) {
                        this.chat("No redstone found!");
                        this.setEnabled(!this.isEnabled());
                        return;
                    }
                    if (Constants.mc.player.inventory.currentItem != this.redstoneSlot) {
                        Constants.mc.player.inventory.currentItem = this.redstoneSlot;
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.redstoneSlot));
                    }
                    if (isSprinting) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    if (shouldSneak) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    BlockUtil.placeBlock(this.redstonePos, true);
                    if (isSprinting) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    }
                    if (shouldSneak) {
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Constants.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    this.stage = Stage.BreakCrystal;
                };
                break;
            }
            case BreakCrystal: {
                this.rotateTarget = new Vec3d((Vec3i)this.crystalPos).addVector(0.5, 0.0, 0.5);
                this.action = () -> {
                    if (!this.finishedRotation() && !this.fastAsFuck) {
                        return;
                    }
                    this.delayTimer.reset();
                    this.crystal = Constants.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal).filter(e -> (double)Constants.mc.player.getDistanceToEntity(e) <= this.range + 4.0).min(Comparator.comparing(c -> Float.valueOf(Constants.mc.player.getDistanceToEntity(c)))).orElse(null);
                    if (this.crystal == null) {
                        return;
                    }
                    if (!this.antiSuicide || !(CombatUtil.getDamage((Entity)Constants.mc.player, this.crystal) >= Constants.mc.player.getHealth() + Constants.mc.player.getAbsorptionAmount()) && this.antiSuicide) {
                        Constants.sendPacket(Constants.attackEntityPacket(this.crystal.getEntityId()));
                        SwingHand.Mainhand.swing();
                    }
                    this.stage = !BlockUtil.isAirBlock(this.redstonePos) && BlockUtil.isBreakable(this.redstonePos) && this.mine ? Stage.BreakRedstone : Stage.FindingTarget;
                };
                break;
            }
            case BreakRedstone: {
                this.rotateTarget = new Vec3d((Vec3i)this.redstonePos).addVector(0.5, 0.0, 0.5);
                this.action = () -> {
                    if (!this.finishedRotation() && !this.fastAsFuck) {
                        return;
                    }
                    this.delayTimer.reset();
                    this.pickaxeSlot = this.getPickaxeSlot();
                    if (this.pickaxeSlot == -1) {
                        this.chat("No pickaxe found!", 0);
                        this.setEnabled(!this.isEnabled());
                        return;
                    }
                    if (Constants.mc.player.inventory.currentItem != this.pickaxeSlot) {
                        Constants.mc.player.inventory.currentItem = this.pickaxeSlot;
                        Constants.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.pickaxeSlot));
                    }
                    if (this.redstonePos == null) {
                        return;
                    }
                    Constants.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Constants.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.redstonePos, this.pistonDirection.getOpposite()));
                    Constants.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.redstonePos, this.pistonDirection.getOpposite()));
                    this.stage = this.extraChecks ? Stage.DelayPhase : Stage.FindingTarget;
                };
                break;
            }
            case DelayPhase: {
                this.action = () -> {
                    this.delayTimer.reset();
                    this.stage = !BlockUtil.isAirBlock(this.pistonPos) ? Stage.Crystal : Stage.FindingTarget;
                };
            }
        }
    }

    private boolean checkTargetAndSetupPositions(EntityPlayer targetPlayer) {
        BlockPos feet = new BlockPos((Entity)targetPlayer);
        if (!BlockUtil.isAirBlock(feet)) {
            return false;
        }
        BlockPos face = feet.up();
        if (!BlockUtil.isAirBlock(face)) {
            return false;
        }
        for (EnumFacing offset : EnumFacing.HORIZONTALS) {
            if (!BlockUtil.isAirBlock(face.offset(offset)) || !BlockUtil.canPlaceCrystal(face.offset(offset).down(), false, false, null) || !BlockUtil.isAirBlock(face.offset(offset).offset(offset)) || BlockUtil.isAirBlock(face.offset(offset).offset(offset).down()) || !BlockUtil.isAirBlock(face.offset(offset).offset(offset).offset(offset)) || BlockUtil.isAirBlock(face.offset(offset).offset(offset).offset(offset).down())) continue;
            this.target = targetPlayer;
            this.crystalPos = face.offset(offset);
            this.pistonPos = face.offset(offset).offset(offset);
            this.redstonePos = face.offset(offset).offset(offset).offset(offset);
            this.pistonDirection = offset;
            return true;
        }
        BlockPos top = face.up();
        for (EnumFacing offset : EnumFacing.HORIZONTALS) {
            if (!BlockUtil.isAirBlock(top.offset(offset)) || !BlockUtil.canPlaceCrystal(top.offset(offset).down(), false, false, null) || !BlockUtil.isAirBlock(top.offset(offset).offset(offset)) || BlockUtil.isAirBlock(top.offset(offset).offset(offset).down()) || !BlockUtil.isAirBlock(top.offset(offset).offset(offset).offset(offset)) || BlockUtil.isAirBlock(top.offset(offset).offset(offset).offset(offset).down())) continue;
            this.target = targetPlayer;
            this.crystalPos = top.offset(offset);
            this.pistonPos = top.offset(offset).offset(offset);
            this.redstonePos = top.offset(offset).offset(offset).offset(offset);
            this.pistonDirection = offset;
            return true;
        }
        return false;
    }

    private List<EntityPlayer> getTargets() {
        return Constants.mc.world.playerEntities.stream().filter(entityPlayer -> !FriendUtil.isFriend(entityPlayer)).filter(entityPlayer -> entityPlayer != Constants.mc.player).filter(e -> (double)Constants.mc.player.getDistanceToEntity((Entity)e) < this.range).filter(e -> {
            if (this.onlyHoles) {
                return BlockUtil.isSurrounded(new BlockPos((Entity)e));
            }
            return true;
        }).sorted(Comparator.comparing(e -> Float.valueOf(Constants.mc.player.getDistanceToEntity((Entity)e)))).collect(Collectors.toList());
    }

    private int getPistonSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = Constants.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.field_190927_a || !(stack.getItem() instanceof ItemBlock) || !((block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockPistonBase)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private int getRedstoneBlockSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = Constants.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.field_190927_a || !(stack.getItem() instanceof ItemBlock) || (block = ((ItemBlock)stack.getItem()).getBlock()) != Blocks.REDSTONE_BLOCK && block != Blocks.REDSTONE_TORCH) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private int getCrystalSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Constants.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.field_190927_a || !(stack.getItem() instanceof ItemEndCrystal)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private int getPickaxeSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Constants.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.field_190927_a || !(stack.getItem() instanceof ItemPickaxe)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private boolean finishedRotation() {
        return RotationManager.get().hasRotated(this.rotateTarget, 0.5f);
    }

    public static PistonAura getInstance() {
        if (instance == null) {
            instance = new PistonAura();
        }
        return instance;
    }

    private static enum Stage {
        FindingTarget,
        Piston,
        Crystal,
        Redstone,
        BreakCrystal,
        BreakRedstone,
        DelayPhase;

    }
}

