/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.modules.combat;

import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.util.minecraft.InventoryUtil;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;

@Module.Manifest(value=Module.Category.COMBAT, description="Rubberbands you into a block")
public class Burrow
extends Module {
    @Setting(category="Offset")
    private OffsetType offsetType = OffsetType.Dynamic;
    @Setting(category="Offset", min=-100.0, max=100.0)
    private double clip = -2.0;
    @Setting(category="Offset", min=0.0, max=200.0)
    private double minOffset = 3.0;
    @Setting(category="Offset", min=0.0, max=200.0)
    private double maxOffset = 10.0;
    @Setting(category="Attack")
    private boolean attack = true;
    @Setting(category="Attack")
    private InventoryUtil.SwitchMode antiWeakness = InventoryUtil.SwitchMode.Client;
    @Setting(category="Attack")
    private boolean suicide = false;
    @Setting(category="Attack", min=0.1, max=36.0)
    private double maxSelfDamage = 10.0;
    @Setting(category="Attack", min=0.0, max=500.0)
    private int attackDelay = 60;
    @Setting(category="Rotation")
    private Rotation.Rotate rotate = Rotation.Rotate.PACKET;
    @Setting(category="Rotation", min=0.0, max=500.0)
    private int rotationDelay = 60;
    @Setting(category="Misc")
    private InventoryUtil.SwitchMode switchMode = InventoryUtil.SwitchMode.Silent;

    private static enum OffsetType {
        Static,
        Dynamic;

    }
}

