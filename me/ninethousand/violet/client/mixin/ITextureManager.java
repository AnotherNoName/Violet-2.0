/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureManager
 */
package me.ninethousand.violet.client.mixin;

import java.util.Map;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={TextureManager.class})
public interface ITextureManager {
    @Accessor(value="mapTextureCounters")
    public Map<String, Integer> getMapTextureCounters();
}

