//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelEnderCrystal
 *  net.minecraft.client.model.ModelRenderer
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.RenderCrystalModelBaseEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ModelEnderCrystal.class})
public class MixinModelEnderCrystal {
    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V"))
    public void renderModel(ModelRenderer instance, float j) {
        RenderCrystalModelBaseEvent renderCrystalModelBaseEvent = new RenderCrystalModelBaseEvent(instance, j);
        EventManager.get().post(renderCrystalModelBaseEvent);
        if (!renderCrystalModelBaseEvent.isCancelled()) {
            instance.render(j);
        }
    }
}

