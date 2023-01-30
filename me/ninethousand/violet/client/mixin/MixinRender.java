//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.BindEntityTextureEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={Render.class})
public abstract class MixinRender<T extends Entity> {
    @Shadow
    protected abstract ResourceLocation getEntityTexture(T var1);

    @Redirect(method={"bindEntityTexture"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/Render;getEntityTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/ResourceLocation;"))
    protected ResourceLocation bindEntityTexture(Render instance, T t) {
        BindEntityTextureEvent bindEntityTextureEvent = new BindEntityTextureEvent((Entity)t, this.getEntityTexture(t));
        EventManager.get().post(bindEntityTextureEvent);
        return bindEntityTextureEvent.getTexture();
    }
}

