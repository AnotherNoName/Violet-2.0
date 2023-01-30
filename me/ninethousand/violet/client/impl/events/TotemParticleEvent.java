/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumParticleTypes
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

public class TotemParticleEvent
extends CancellableEvent {
    private Entity entity;
    private EnumParticleTypes type;
    private int amount;

    public TotemParticleEvent(Entity entity, EnumParticleTypes type, int amount) {
        this.entity = entity;
        this.type = type;
        this.amount = amount;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public EnumParticleTypes getType() {
        return this.type;
    }

    public void setType(EnumParticleTypes type) {
        this.type = type;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

