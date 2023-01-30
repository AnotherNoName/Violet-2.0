//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.ninethousand.violet.client.impl.modules.movement;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import me.ninethousand.violet.client.util.minecraft.EntityUtil;
import net.minecraft.entity.Entity;

@Module.Manifest(value=Module.Category.MOVEMENT, description="Steps downwards")
public class ReverseStep
extends Module {
    @Setting(min=1.0)
    private int height = 2;
    @Setting(min=0.1, max=20.0)
    private float speed = 10.0f;
    private static ReverseStep instance;

    @Listener
    public void listen(UpdateEvent event) {
        if (!Constants.player().onGround || Constants.player().motionY > 0.0 || Constants.player().isInLava() || Constants.player().isInWater() || Constants.player().isOnLadder()) {
            return;
        }
        for (int i = this.height + 1; i >= 0; --i) {
            if (BlockUtil.isAirBlock(EntityUtil.getEntityPos((Entity)Constants.player()).add(0, -i, 0))) continue;
            Constants.player().motionY = -this.speed;
            break;
        }
    }

    public static ReverseStep getInstance() {
        if (instance == null) {
            instance = new ReverseStep();
        }
        return instance;
    }
}

