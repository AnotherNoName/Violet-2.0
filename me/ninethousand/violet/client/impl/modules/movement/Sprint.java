//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.modules.movement;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.util.Constants;

@Module.Manifest(value=Module.Category.MOVEMENT, description="Sprints like Usain Bolt")
public class Sprint
extends Module {
    private static Sprint instance;

    @Listener
    public void listen(UpdateEvent event) {
        if (!Constants.mc.player.isSprinting()) {
            Constants.mc.player.setSprinting(true);
        }
    }

    public static Sprint getInstance() {
        if (instance == null) {
            instance = new Sprint();
        }
        return instance;
    }
}

