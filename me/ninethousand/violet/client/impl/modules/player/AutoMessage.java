//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.modules.player;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.ChatEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.MathUtil;

@Module.Manifest(value=Module.Category.PLAYER, description="Sends automatic messages")
public class AutoMessage
extends Module {
    @Setting
    private boolean coords = true;
    @Setting
    private boolean onlySpawn = true;
    @Setting
    private String shortcut = "@A";
    private static AutoMessage instance;

    @Listener
    private void listen(ChatEvent.Outgoing event) {
        if (this.coords) {
            event.setMessage(event.getMessage().replace(this.shortcut, this.getCoords()));
        }
    }

    private String getCoords() {
        StringBuilder sb = new StringBuilder();
        if (this.onlySpawn && (Constants.mc.player.posX > 5000.0 || Constants.mc.player.posZ > 5000.0)) {
            sb.append("Im Far From Spawn");
        } else {
            sb.append("Im At (" + MathUtil.round(Constants.mc.player.posX, 0) + ", " + MathUtil.round(Constants.mc.player.posX, 0) + ", " + MathUtil.round(Constants.mc.player.posX, 0) + ") ");
        }
        return sb.toString();
    }

    public static AutoMessage getInstance() {
        if (instance == null) {
            instance = new AutoMessage();
        }
        return instance;
    }
}

