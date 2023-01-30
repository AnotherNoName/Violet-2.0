/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumHand
 */
package me.ninethousand.violet.client.impl.modules.render;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.SwingHandEvent;
import net.minecraft.util.EnumHand;

@Module.Manifest(value=Module.Category.RENDER, description="Modify how the hands look")
public class Viewmodel
extends Module {
    @Setting
    private Hand hand = Hand.Mainhand;
    @Setting
    private boolean customSpeed = false;
    @Setting
    private Speed speed = Speed.Fast;
    @Setting(max=5.0)
    private double factor = 1.0;

    @Listener
    private void listen(SwingHandEvent event) {
        switch (this.hand) {
            case Mainhand: {
                event.setHand(EnumHand.MAIN_HAND);
                break;
            }
            case Offhand: {
                event.setHand(EnumHand.OFF_HAND);
                break;
            }
            case Alternate: {
                if (event.getHand() == EnumHand.MAIN_HAND) {
                    event.setHand(EnumHand.OFF_HAND);
                    break;
                }
                event.setHand(EnumHand.MAIN_HAND);
            }
        }
    }

    private static enum Speed {
        Fast,
        Slow;

    }

    private static enum Hand {
        Mainhand,
        Offhand,
        Alternate;

    }
}

