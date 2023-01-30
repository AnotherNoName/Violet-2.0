//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.VioletRenderer;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.mixin.IRenderManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Manifest(value=Module.Category.RENDER)
public class BurrowESP
extends Module {
    @Setting
    private Mode mode = Mode.Text;
    @Setting
    private Color color = new Color(-1131345008, true);
    private ArrayList<BlockPos> blocks;
    private static BurrowESP instance;

    @Override
    protected void onEnable() {
        this.blocks = new ArrayList();
    }

    @Override
    protected void onDisable() {
        this.blocks = null;
    }

    @Listener
    private void listen(UpdateEvent event) {
        this.blocks.clear();
        Constants.mc.world.playerEntities.stream().filter(this::isBurrowed).map(BlockPos::new).forEach(this.blocks::add);
    }

    @Listener
    private void listen(Render3DEvent event) {
        this.blocks.forEach(this::render);
    }

    private boolean isBurrowed(EntityPlayer player) {
        BlockPos pos = new BlockPos((Entity)player);
        Block block = Constants.mc.world.getBlockState(pos).getBlock();
        return block != Blocks.AIR;
    }

    private void render(BlockPos blockPos) {
        switch (this.mode) {
            case Fill: {
                Constants.renderer.renderBlockPos(blockPos, this.color, VioletRenderer.BoxMode.FilledBounded);
                break;
            }
            case Text: {
                Vec3d pos = BlockUtil.getCenter(blockPos);
                double renderX = ((IRenderManager)Constants.mc.getRenderManager()).getRenderPosX();
                double renderY = ((IRenderManager)Constants.mc.getRenderManager()).getRenderPosY();
                double renderZ = ((IRenderManager)Constants.mc.getRenderManager()).getRenderPosZ();
                double scaling = 0.05f;
                GlStateManager.pushMatrix();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
                GlStateManager.disableLighting();
                GlStateManager.translate((double)(pos.xCoord - renderX), (double)(pos.yCoord - renderY), (double)(pos.zCoord - renderZ));
                GlStateManager.rotate((float)(-Constants.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.rotate((float)Constants.mc.getRenderManager().playerViewX, (float)(Constants.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
                GlStateManager.scale((double)(-scaling), (double)(-scaling), (double)scaling);
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                String text = "Burrowed";
                FontManager.get().drawCenteredString(text, 0.0f, 0.0f, this.color);
                GlStateManager.enableDepth();
                GlStateManager.disableBlend();
                GlStateManager.disablePolygonOffset();
                GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
                GlStateManager.popMatrix();
            }
        }
    }

    public static BurrowESP getInstance() {
        if (instance == null) {
            instance = new BurrowESP();
        }
        return instance;
    }

    private static enum Mode {
        Text,
        Fill;

    }
}

