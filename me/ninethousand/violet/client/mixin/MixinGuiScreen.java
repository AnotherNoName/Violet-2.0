/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.item.ItemStack
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.TooltipEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreen.class})
public class MixinGuiScreen {
    @Inject(method={"renderToolTip"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        TooltipEvent event = new TooltipEvent(stack, x, y);
        EventManager.get().post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}

