/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public final class PlayerDamageBlockEvent
extends CancellableEvent {
    private final BlockPos pos;
    private final EnumFacing direction;

    public PlayerDamageBlockEvent(BlockPos pos, EnumFacing direction) {
        this.pos = pos;
        this.direction = direction;
    }

    public BlockPos pos() {
        return this.pos;
    }

    public EnumFacing direction() {
        return this.direction;
    }
}

