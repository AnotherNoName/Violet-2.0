//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.TooltipEvent;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.mixin.IRenderManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

@Module.Manifest(value=Module.Category.RENDER, description="Render shulker contents")
public class Tooltips
extends Module {
    @Setting
    private boolean otherPlayers = true;
    @Setting
    private boolean inventory = true;
    @Setting
    private Color background = new Color(1580676919, true);
    private List<ShulkerContainer> shulkers;
    private static Tooltips instance;

    @Override
    protected void onEnable() {
        this.shulkers = new ArrayList<ShulkerContainer>();
    }

    @Override
    protected void onDisable() {
        this.shulkers = null;
    }

    @Listener
    private void listen(UpdateEvent event) {
        if (!this.otherPlayers) {
            return;
        }
        List<ShulkerContainer> additions = Constants.mc.world.playerEntities.stream().filter(player -> player != Constants.mc.player).filter(player -> player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox || player.getHeldItemOffhand().getItem() instanceof ItemShulkerBox).map(ShulkerContainer::new).collect(Collectors.toList());
        additions.forEach(shulkerContainer -> this.shulkers.removeIf(oldContainer -> oldContainer.player == shulkerContainer.player));
        this.shulkers.removeIf(container -> container.timer.passed(3000L));
        this.shulkers.addAll(additions);
    }

    @Listener
    private void listen(Render3DEvent event) {
        if (Constants.nullCheck() || !this.otherPlayers) {
            return;
        }
        this.shulkers.forEach(container -> this.renderShulkerIn3D(container.player, container.shulker, container.player.getPositionVector(), this.background));
    }

    @Listener
    private void listen(TooltipEvent event) {
        NBTTagCompound blockEntityTag;
        if (!(event.getStack().getItem() instanceof ItemShulkerBox) || !this.inventory) {
            return;
        }
        NBTTagCompound tagCompound = event.getStack().getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            event.cancel();
            NonNullList nonnulllist = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int width = Math.max(144, FontManager.get().getWidth(event.getStack().getDisplayName()) + 3);
            int x1 = event.getX() + 12;
            int y1 = event.getY() - 12;
            int height = 57;
            Constants.mc.getRenderItem().zLevel = 300.0f;
            Vec2f pos = new Vec2f((float)x1, (float)y1);
            FontManager.get().drawCenteredString(event.getStack().getDisplayName(), x1 + width / 2, y1 + 1, Color.WHITE);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = event.getX() + i % 9 * 16 + 11;
                int iY = event.getY() + i / 9 * 16 - 11 + 8;
                ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                Constants.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, iX, iY);
                Constants.mc.getRenderItem().renderItemOverlayIntoGUI(Constants.mc.fontRendererObj, itemStack, iX, iY, null);
            }
            RenderHelper.disableStandardItemLighting();
            Constants.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    private void renderShulkerIn3D(EntityPlayer player, ItemStack itemStack, Vec3d pos, Color color) {
        NBTTagCompound blockEntityTag;
        if (!(itemStack.getItem() instanceof ItemShulkerBox)) {
            return;
        }
        double renderX = ((IRenderManager)Constants.mc.getRenderManager()).getRenderPosX();
        double renderY = ((IRenderManager)Constants.mc.getRenderManager()).getRenderPosY();
        double renderZ = ((IRenderManager)Constants.mc.getRenderManager()).getRenderPosZ();
        double scaling = 0.02f;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((double)(pos.xCoord - renderX), (double)(pos.yCoord + 4.0 - renderY), (double)(pos.zCoord - renderZ));
        GlStateManager.rotate((float)(-Constants.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)Constants.mc.getRenderManager().playerViewX, (float)(Constants.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scaling), (double)(-scaling), (double)scaling);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        String title = player.getName() + " - " + itemStack.getDisplayName();
        int width = Math.max(144, FontManager.get().getWidth(title) + 3);
        int x1 = 12 - width / 2 - 20;
        int y1 = -12;
        int height = 57;
        Constants.renderer.fill(new Vec2f((float)(x1 + 10), (float)y1), new Vec2f((float)(x1 + width + 15), (float)(y1 + height)), color);
        FontManager.get().drawCenteredString(title, x1 + width / 2 + 10, y1 + 1, Color.WHITE);
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            NonNullList nonnulllist = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            Constants.mc.getRenderItem().zLevel = -150.0f;
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = x1 + i % 9 * 16 + 11;
                int iY = i / 9 * 16 - 11 + 8;
                ItemStack itemStack1 = (ItemStack)nonnulllist.get(i);
                Constants.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack1, iX, iY);
                Constants.mc.getRenderItem().renderItemOverlayIntoGUI(Constants.mc.fontRendererObj, itemStack1, iX, iY, null);
            }
            Constants.mc.getRenderItem().zLevel = 0.0f;
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
        GlStateManager.popMatrix();
    }

    public static Tooltips getInstance() {
        if (instance == null) {
            instance = new Tooltips();
        }
        return instance;
    }

    private static class ShulkerContainer {
        EntityPlayer player;
        ItemStack shulker;
        Timer timer;

        public ShulkerContainer(EntityPlayer player) {
            this.player = player;
            this.shulker = player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
            this.timer = new Timer();
        }
    }
}

