//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.impl.modules.render;

import java.awt.Color;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@Module.Manifest(value=Module.Category.RENDER, description="Highlights the block ur looking at")
public class BlockHighlight
extends Module {
    @Setting
    private Color color = new Color(-1131345008, true);
    private static BlockHighlight instance;

    @Listener
    public void listen(Render3DEvent event) {
        BlockPos pos;
        IBlockState iblockstate;
        if (Constants.nullCheck()) {
            return;
        }
        RayTraceResult result = Constants.mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && (iblockstate = Constants.mc.world.getBlockState(pos = result.getBlockPos())).getMaterial() != Material.AIR && Constants.mc.world.getWorldBorder().contains(pos)) {
            double d3 = Constants.mc.getRenderViewEntity().lastTickPosX + (Constants.mc.getRenderViewEntity().posX - Constants.mc.getRenderViewEntity().lastTickPosX) * (double)event.getPartialTicks();
            double d4 = Constants.mc.getRenderViewEntity().lastTickPosY + (Constants.mc.getRenderViewEntity().posY - Constants.mc.getRenderViewEntity().lastTickPosY) * (double)event.getPartialTicks();
            double d5 = Constants.mc.getRenderViewEntity().lastTickPosZ + (Constants.mc.getRenderViewEntity().posZ - Constants.mc.getRenderViewEntity().lastTickPosZ) * (double)event.getPartialTicks();
            Constants.renderer.boundedAABB(iblockstate.getSelectedBoundingBox((World)Constants.mc.world, pos).expandXyz((double)0.002f).offset(-d3, -d4, -d5), 0.0, 0.0, 0.0, this.color);
        }
    }

    public static BlockHighlight getInstance() {
        if (instance == null) {
            instance = new BlockHighlight();
        }
        return instance;
    }
}

