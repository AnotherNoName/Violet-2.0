//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAnvil
 *  net.minecraft.block.BlockBed
 *  net.minecraft.block.BlockButton
 *  net.minecraft.block.BlockCake
 *  net.minecraft.block.BlockContainer
 *  net.minecraft.block.BlockDoor
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.BlockRedstoneDiode
 *  net.minecraft.block.BlockTrapDoor
 *  net.minecraft.block.BlockWorkbench
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.util.minecraft;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import me.ninethousand.violet.client.mixin.IBlock;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isSurrounded(BlockPos pos) {
        if (BlockUtil.isHardBlock(pos)) {
            return true;
        }
        if (!BlockUtil.isHardBlock(pos.east())) {
            return false;
        }
        if (!BlockUtil.isHardBlock(pos.north())) {
            return false;
        }
        if (!BlockUtil.isHardBlock(pos.west())) {
            return false;
        }
        if (!BlockUtil.isHardBlock(pos.south())) {
            return false;
        }
        return BlockUtil.isHardBlock(pos.down());
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.xCoord - (double)pos.getX());
            float f1 = (float)(vec.yCoord - (double)pos.getY());
            float f2 = (float)(vec.zCoord - (double)pos.getZ());
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(hand));
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
            BlockUtil.mc.player.swingArm(hand);
        }
    }

    public static boolean shouldSneakWhileRightClicking(BlockPos blockPos) {
        Block block = BlockUtil.mc.world.getBlockState(blockPos).getBlock();
        TileEntity tileEntity = null;
        for (TileEntity tE : BlockUtil.mc.world.loadedTileEntityList) {
            if (!tE.getPos().equals((Object)blockPos)) continue;
            tileEntity = tE;
            break;
        }
        return tileEntity != null || block instanceof BlockBed || block instanceof BlockContainer || block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate || block instanceof BlockButton || block instanceof BlockAnvil || block instanceof BlockWorkbench || block instanceof BlockCake || block instanceof BlockRedstoneDiode;
    }

    public static float[] calcAngle(Vec3d from, BlockPos to) {
        double difX = (double)to.getX() - from.xCoord;
        double difY = ((double)to.getY() - from.yCoord) * -1.0;
        double difZ = (double)to.getZ() - from.zCoord;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static boolean isHardBlock(BlockPos pos) {
        Block block = Constants.world().getBlockState(pos).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL || block == Blocks.ENCHANTING_TABLE;
    }

    public static boolean isCrystalPlaceableBlock(BlockPos pos) {
        Block block = Constants.world().getBlockState(pos).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK;
    }

    public static boolean isBreakable(BlockPos pos) {
        Block block = Constants.world().getBlockState(pos).getBlock();
        return block != Blocks.BEDROCK;
    }

    public static boolean isAirBlock(BlockPos pos) {
        Block block = Constants.world().getBlockState(pos).getBlock();
        return block == Blocks.AIR;
    }

    public static void placeCrystal(BlockPos pos, EnumFacing facing, EnumHand hand, Vec3d lookVec, boolean packet) {
        if (packet) {
            Constants.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, (float)lookVec.xCoord, (float)lookVec.yCoord, (float)lookVec.zCoord));
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(Constants.player(), Constants.world(), pos, facing, lookVec, hand);
        }
    }

    public static boolean placeBlock(BlockPos pos, boolean packet) {
        if (!(Constants.player().getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            return false;
        }
        EnumFacing facing = BlockUtil.getNeighbor(pos);
        if (facing == null) {
            return false;
        }
        Vec3d lookVec = Constants.player().getLookVec();
        if (packet) {
            Constants.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos.offset(facing), facing.getOpposite(), EnumHand.MAIN_HAND, (float)lookVec.xCoord, (float)lookVec.yCoord, (float)lookVec.zCoord));
            Constants.player().swingArm(EnumHand.MAIN_HAND);
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(Constants.player(), Constants.world(), pos.offset(facing), facing.getOpposite(), lookVec, EnumHand.MAIN_HAND);
        }
        return true;
    }

    public static boolean placeBlock(BlockPos pos, boolean packet, EnumFacing facing) {
        if (!(Constants.player().getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            return false;
        }
        Vec3d lookVec = Constants.player().getLookVec();
        if (packet) {
            Constants.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos.offset(facing), facing.getOpposite(), EnumHand.MAIN_HAND, (float)lookVec.xCoord, (float)lookVec.yCoord, (float)lookVec.zCoord));
            Constants.player().swingArm(EnumHand.MAIN_HAND);
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(Constants.player(), Constants.world(), pos.offset(facing), facing.getOpposite(), lookVec, EnumHand.MAIN_HAND);
        }
        return true;
    }

    @Nullable
    public static EnumFacing getNeighbor(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (Constants.world().getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) continue;
            return facing;
        }
        return null;
    }

    public static Vec3d getCenter(BlockPos pos) {
        return new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
    }

    public static List<BlockPos> getPlaceableBlocks(BlockPos source, double radius, boolean oneThirteen, boolean multiplace, List<Integer> ignoredEntities) {
        ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();
        for (BlockPos pos : BlockUtil.getSphere(source, (float)radius, (int)radius, false, true, 0)) {
            if (!BlockUtil.canPlaceCrystal(pos, oneThirteen, multiplace, ignoredEntities)) continue;
            blockPosList.add(pos);
        }
        return blockPosList;
    }

    public static boolean canPlaceCrystal(BlockPos pos, boolean oneThirteen, boolean multiplace, List<Integer> ignoredEntities) {
        Block material = Constants.world().getBlockState(pos).getBlock();
        if (material != Blocks.OBSIDIAN && material != Blocks.BEDROCK) {
            return false;
        }
        return Constants.world().getBlockState(pos.up()).getBlock() == Blocks.AIR && (oneThirteen || Constants.world().getBlockState(pos.up().up()).getBlock() == Blocks.AIR) && Constants.world().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1)), entity -> (multiplace || !(entity instanceof EntityEnderCrystal)) && entity.isEntityAlive() && !ignoredEntities.contains(entity.getEntityId())).isEmpty();
    }

    public static boolean canPlaceCrystalTotal(BlockPos pos, boolean oneThirteen) {
        Block material = Constants.world().getBlockState(pos).getBlock();
        if (material != Blocks.OBSIDIAN && material != Blocks.BEDROCK) {
            return false;
        }
        return Constants.world().getBlockState(pos.up()).getBlock() == Blocks.AIR && (oneThirteen || Constants.world().getBlockState(pos.up().up()).getBlock() == Blocks.AIR) && Constants.world().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1))).isEmpty();
    }

    public static boolean isCrystalInPos(BlockPos pos) {
        for (Entity entity : Constants.world().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1)))) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            return true;
        }
        return false;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float radius, int height, boolean hollow, boolean sphere, int yOffset) {
        ArrayList<BlockPos> circleBlocks = new ArrayList<BlockPos>();
        int currentX = pos.getX() - (int)radius;
        while ((float)currentX <= (float)pos.getX() + radius) {
            int currentZ = pos.getZ() - (int)radius;
            while ((float)currentZ <= (float)pos.getZ() + radius) {
                int currentY = sphere ? pos.getY() - (int)radius : pos.getY();
                while (true) {
                    float f2;
                    float f = f2 = sphere ? (float)pos.getY() + radius : (float)(pos.getY() + height);
                    if (!((float)currentY < f2)) break;
                    double distance = (pos.getX() - currentX) * (pos.getX() - currentX) + (pos.getZ() - currentZ) * (pos.getZ() - currentZ) + (sphere ? (pos.getY() - currentY) * (pos.getY() - currentY) : 0);
                    if (distance < (double)(radius * radius) || hollow && distance < (double)((radius - 1.0f) * (radius - 1.0f))) {
                        circleBlocks.add(new BlockPos(currentX, currentY + yOffset, currentZ));
                    }
                    ++currentY;
                }
                ++currentZ;
            }
            ++currentX;
        }
        return circleBlocks;
    }

    public static boolean isSamePosition(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
    }

    public static float getDistance(Entity e, BlockPos bp) {
        float f = (float)(e.posX - (double)bp.getX());
        float f1 = (float)(e.posY - (double)bp.getY());
        float f2 = (float)(e.posZ - (double)bp.getZ());
        return MathHelper.sqrt((float)(f * f + f1 * f1 + f2 * f2));
    }

    public static void mineBlockLegit(BlockPos pos) {
        BlockUtil.mc.playerController.onPlayerDamageBlock(pos, BlockUtil.mc.objectMouseOver.sideHit);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static Vec3d blockPosToVec(BlockPos pos) {
        return new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    public static Vec3d centerBlockPosToVec(BlockPos pos) {
        return new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()).addVector(0.5, 0.5, 0.5);
    }

    public static double getDestroyTime(Block block, ItemStack stack) {
        float speedMultiplier = stack.getStrVsBlock(block.getDefaultState());
        float damage = stack.canHarvestBlock(block.getDefaultState()) ? speedMultiplier / ((IBlock)block).getBlockHardness() / 30.0f : speedMultiplier / ((IBlock)block).getBlockHardness() / 100.0f;
        return (float)Math.ceil(1.0f / damage);
    }

    private BlockUtil() {
        throw new UnsupportedOperationException();
    }
}

