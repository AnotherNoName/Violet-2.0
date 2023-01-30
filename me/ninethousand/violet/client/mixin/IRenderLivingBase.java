/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.EntityLivingBase
 */
package me.ninethousand.violet.client.mixin;

import java.util.List;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={RenderLivingBase.class})
public interface IRenderLivingBase<T extends EntityLivingBase> {
    @Accessor(value="layerRenderers")
    public List<LayerRenderer<T>> getLayers();

    @Invoker(value="renderModel")
    public void invokeRenderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7);
}

