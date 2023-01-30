/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.modules.movement;

import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;

@Module.Manifest(value=Module.Category.MOVEMENT)
public class Step
extends Module {
    @Setting
    private Mode mode = Mode.NCP;

    public static enum Mode {
        VANILLA,
        NCP;

    }
}

