/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.DamageSource
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class EntityDamagedEvent
extends CancellableEvent {
    private final Entity entity;
    private final DamageSource source;
    private final float amount;

    public EntityDamagedEvent(Entity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public float getAmount() {
        return this.amount;
    }
}

