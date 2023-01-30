//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.init.PotionTypes
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.VioletRenderer;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.PacketEvent;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.managers.RotationManager;
import me.ninethousand.violet.client.mixin.ISPacketPlayerPosLook;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.client.FriendUtil;
import me.ninethousand.violet.client.util.minecraft.AngleUtil;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import me.ninethousand.violet.client.util.minecraft.CombatUtil;
import me.ninethousand.violet.client.util.minecraft.EntityUtil;
import me.ninethousand.violet.client.util.minecraft.InventoryUtil;
import me.ninethousand.violet.client.util.minecraft.SwingHand;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

@Module.Manifest(value=Module.Category.COMBAT, description="Crystals go brrr")
public class AutoCrystal
extends Module {
    @Setting(category="Placement")
    private boolean place = true;
    @Setting(category="Placement", min=0.0, max=500.0)
    private int placeDelay = 60;
    @Setting(category="Placement", min=0.1, max=6.0)
    private double placeRange = 5.0;
    @Setting(category="Placement")
    private boolean packetPlace = true;
    @Setting(category="Placement")
    private boolean fastPlace = true;
    @Setting(category="Placement")
    private boolean fastPlaceIgnoreTimer = true;
    @Setting(category="Placement")
    private boolean placeRaytrace = false;
    @Setting(category="Placement")
    private boolean placeOneThirteen = false;
    @Setting(category="Placement")
    private boolean multiplace = false;
    @Setting(category="Placement", min=0.1, max=36.0)
    private double placeMinDamage = 5.0;
    @Setting(category="Placement", min=0.1, max=36.0)
    private double placeMaxSelfDamage = 5.0;
    @Setting(category="Breaking", name="Break")
    private boolean break_ = true;
    @Setting(category="Breaking", min=0.0, max=500.0)
    private int breakDelay = 60;
    @Setting(category="Breaking", min=0.1, max=6.0)
    private double breakRange = 5.0;
    @Setting(category="Breaking", min=0.1, max=36.0)
    private double breakMinDamage = 5.0;
    @Setting(category="Breaking", min=0.1, max=36.0)
    private double breakMaxSelfDamage = 5.0;
    @Setting(category="Breaking")
    private boolean antiweakness = true;
    @Setting(category="Breaking", min=1.0, max=5.0)
    private int maxBreakAttempts = 1;
    @Setting(category="Breaking")
    private SwingHand swingHand = SwingHand.Mainhand;
    @Setting(category="Rotation")
    private Rotation.Rotate rotation = Rotation.Rotate.PACKET;
    @Setting(category="Rotation", min=0.0, max=180.0)
    private float stepInterval = 15.0f;
    @Setting(category="Rotation", min=0.0, max=10.0)
    private int pauseTicks = 1;
    @Setting(category="Rotation")
    private boolean antiRubber = true;
    @Setting(category="Targeting", min=1.0, max=20.0)
    private int targetingRange = 10;
    @Setting(category="Targeting")
    private boolean faceplace = true;
    @Setting(category="Targeting", min=0.1, max=36.0)
    private double faceplaceHP = 5.0;
    @Setting(category="Misc", min=0.0, max=5000.0)
    private int timeout = 1000;
    @Setting(category="Render")
    private Color color = new Color(1861810424, true);
    private final Timer placeTimer = new Timer();
    private final Timer explodeTimer = new Timer();
    private final Timer clearIgnoredIDsTimer = new Timer();
    private final Timer timeoutTimer = new Timer();
    private final List<Integer> ignoredIDs = new ArrayList<Integer>();
    private final Map<Integer, Integer> amountHit = new HashMap<Integer, Integer>();
    private BlockPos renderPos = null;
    private Vec3d rotateTarget = null;
    private boolean busy = false;
    private static AutoCrystal instance;

    @Override
    protected void onEnable() {
        this.placeTimer.reset();
        this.explodeTimer.reset();
        this.clearIgnoredIDsTimer.reset();
        this.timeoutTimer.reset();
        this.ignoredIDs.clear();
        this.amountHit.clear();
        this.renderPos = null;
        this.rotateTarget = null;
        this.busy = false;
    }

    @Listener
    private void listen(Render3DEvent event) {
        if (this.renderPos != null) {
            Constants.renderer.renderBlockPos(this.renderPos, this.color, VioletRenderer.BoxMode.FilledBounded);
        }
    }

    @Listener
    private void listen(UpdateEvent event) {
        if (this.clearIgnoredIDsTimer.passed(100L)) {
            this.ignoredIDs.clear();
            this.amountHit.clear();
        } else {
            this.ignoredIDs.removeIf(i -> Constants.world().getEntityByID(i.intValue()) == null);
        }
        boolean active = false;
        if (this.break_ && this.explodeTimer.passed(this.breakDelay)) {
            active = this.busy = this.explodeCrystal();
        }
        if (this.place && this.placeTimer.passed(this.placeDelay) && !(this.busy = this.placeCrystal()) && !active) {
            this.renderPos = null;
        }
        if (this.busy) {
            this.timeoutTimer.reset();
        }
        if (this.rotateTarget == null) {
            return;
        }
        Rotation angles = AngleUtil.calculateAngles(this.rotateTarget);
        RotationManager.get().addRotation(new Rotation(angles.getYaw(), angles.getPitch(), this.rotation, this.stepInterval));
    }

    @Listener
    private void listen(PacketEvent.Read event) {
        if (this.rotation != Rotation.Rotate.NONE && this.antiRubber && event.getPacket() instanceof SPacketPlayerPosLook) {
            ISPacketPlayerPosLook isPacketPosLook = (ISPacketPlayerPosLook)event.getPacket();
            isPacketPosLook.setYaw(Constants.mc.player.rotationYawHead);
            isPacketPosLook.setPitch(Constants.mc.player.rotationPitch);
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion sPacketExplosion = (SPacketExplosion)event.getPacket();
            for (Entity entity : Constants.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistance(sPacketExplosion.getX(), sPacketExplosion.getY(), sPacketExplosion.getZ()) < 6.0)) continue;
                this.ignoredIDs.add(entity.getEntityId());
                if (!this.fastPlace || !this.placeTimer.passed(this.placeDelay) && !this.fastPlaceIgnoreTimer) continue;
                this.placeCrystal();
            }
        }
    }

    private boolean placeCrystal() {
        RayTraceResult result;
        BlockPos pos = null;
        EnumFacing facing = EnumFacing.UP;
        double damage = 0.0;
        List<BlockPos> blockPosList = BlockUtil.getPlaceableBlocks(EntityUtil.getEntityPos((Entity)Constants.player()), this.placeRange, this.placeOneThirteen, this.multiplace, this.ignoredIDs);
        for (EntityPlayer possibleTarget : Constants.world().playerEntities) {
            if (possibleTarget == Constants.player() || possibleTarget.isDead || Constants.getHealthPoints((EntityLivingBase)possibleTarget) <= 0.0 || Constants.player().getDistanceToEntity((Entity)possibleTarget) > (float)this.targetingRange || FriendUtil.isFriend(possibleTarget)) continue;
            boolean shouldFaceplace = this.shouldFaceplace(possibleTarget);
            for (BlockPos possiblePos : blockPosList) {
                double calculatedSelfDmg;
                double calculatedDmg = CombatUtil.getDamage((Entity)possibleTarget, possiblePos);
                if (calculatedDmg < this.placeMinDamage || calculatedDmg < damage || (calculatedSelfDmg = (double)CombatUtil.getDamage((Entity)Constants.player(), possiblePos)) > this.placeMaxSelfDamage || calculatedSelfDmg > Constants.getHealthPoints((EntityLivingBase)Constants.player())) continue;
                pos = possiblePos;
                damage = calculatedDmg;
            }
        }
        if (pos == null) {
            return false;
        }
        if (this.placeRaytrace && (result = Constants.world().rayTraceBlocks(EntityUtil.getEyePos((Entity)Constants.player()), BlockUtil.getCenter(pos))) != null) {
            facing = result.sideHit;
        }
        return this.placeCrystal(pos, facing);
    }

    private boolean placeCrystal(BlockPos pos, EnumFacing facing) {
        if (pos == null || facing == null) {
            return false;
        }
        this.rotateTarget = BlockUtil.blockPosToVec(pos).addVector(0.5, 0.5, 0.5);
        if (!this.finishedRotation()) {
            return false;
        }
        boolean offhand = Constants.player().getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        EnumHand hand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        Vec3d lookVec = Constants.player().getLookVec();
        this.renderPos = pos;
        if (!offhand && InventoryUtil.switchToItem(Items.END_CRYSTAL, InventoryUtil.SwitchMode.Client) == -1) {
            return false;
        }
        BlockUtil.placeCrystal(pos, facing, hand, lookVec, this.packetPlace);
        this.swingHand.swing();
        this.placeTimer.reset();
        return true;
    }

    private boolean explodeCrystal() {
        int crystal = -1;
        Vec3d crystalPos = Vec3d.ZERO;
        double damage = 0.0;
        ArrayList<EntityEnderCrystal> crystalList = new ArrayList<EntityEnderCrystal>();
        for (Entity e : Constants.loadedEntityList()) {
            if (!(e instanceof EntityEnderCrystal) || e.isDead || this.ignoredIDs.contains(e.getEntityId()) || (double)Constants.player().getDistanceToEntity(e) > this.breakRange) continue;
            crystalList.add((EntityEnderCrystal)e);
        }
        for (EntityPlayer possibleTarget : Constants.playerEntities()) {
            if (possibleTarget == Constants.player() || Constants.player().getDistanceToEntity((Entity)possibleTarget) > (float)this.targetingRange || possibleTarget.isDead || Constants.getHealthPoints((EntityLivingBase)possibleTarget) <= 0.0 || FriendUtil.isFriend(possibleTarget)) continue;
            boolean shouldFaceplace = this.shouldFaceplace(possibleTarget);
            for (EntityEnderCrystal possibleCrystal : crystalList) {
                double calculatedSelfDamage;
                double calculatedDamage = CombatUtil.getDamage((Entity)possibleTarget, possibleCrystal);
                if ((calculatedDamage < damage || calculatedDamage < this.placeMinDamage) && !shouldFaceplace || (calculatedSelfDamage = (double)CombatUtil.getDamage((Entity)Constants.player(), possibleCrystal)) > this.breakMaxSelfDamage || calculatedSelfDamage > Constants.getHealthPoints((EntityLivingBase)Constants.player())) continue;
                crystal = possibleCrystal.getEntityId();
                crystalPos = EntityUtil.getCenterPos((Entity)possibleCrystal);
                damage = calculatedDamage;
            }
        }
        return this.explodeCrystal(crystal, crystalPos);
    }

    private boolean explodeCrystal(int crystal, Vec3d pos) {
        boolean weakened;
        if (crystal == -1) {
            return false;
        }
        this.rotateTarget = EntityUtil.getCenterPos(Constants.mc.world.getEntityByID(crystal));
        if (!this.finishedRotation()) {
            return false;
        }
        boolean bl = weakened = Constants.player().getActivePotionEffect(((PotionEffect)PotionTypes.WEAKNESS.getEffects().get(0)).getPotion()) != null;
        if (weakened && !InventoryUtil.switchToSword(InventoryUtil.SwitchMode.Client)) {
            return false;
        }
        Constants.sendPacket(Constants.attackEntityPacket(crystal));
        this.swingHand.swing();
        int newAmount = this.amountHit.getOrDefault(crystal, 0) + 1;
        this.amountHit.put(crystal, newAmount);
        if (newAmount >= this.maxBreakAttempts) {
            this.ignoredIDs.add(crystal);
        }
        this.explodeTimer.reset();
        return true;
    }

    private boolean finishedRotation() {
        return RotationManager.get().hasRotated(this.rotateTarget, 0.5f);
    }

    public boolean isBusy() {
        return this.busy;
    }

    private boolean shouldFaceplace(EntityPlayer entityPlayer) {
        return this.faceplace && Constants.getHealthPoints((EntityLivingBase)entityPlayer) < this.faceplaceHP;
    }

    public static AutoCrystal getInstance() {
        if (instance == null) {
            instance = new AutoCrystal();
        }
        return instance;
    }
}

