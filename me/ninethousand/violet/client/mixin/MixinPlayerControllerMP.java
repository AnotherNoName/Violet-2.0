//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.PlayerDamageBlockEvent;
import me.ninethousand.violet.client.impl.events.TryUseItemEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PlayerControllerMP.class})
public class MixinPlayerControllerMP {
    @Inject(method={"processRightClick"}, at={@At(value="HEAD")}, cancellable=true)
    public void processRightClick(EntityPlayer player, World worldIn, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        TryUseItemEvent tryUseItemEvent = new TryUseItemEvent(player.getHeldItem(hand));
        EventManager.get().post(tryUseItemEvent);
        if (tryUseItemEvent.isCancelled()) {
            cir.cancel();
        }
    }

    @Inject(method={"onPlayerDamageBlock"}, at={@At(value="HEAD")}, cancellable=true)
    public void onOnPlayerDamageBlockHead(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> cir) {
        PlayerDamageBlockEvent playerDamageBlockEvent = new PlayerDamageBlockEvent(pos, facing);
        EventManager.get().post(playerDamageBlockEvent);
        if (playerDamageBlockEvent.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}

