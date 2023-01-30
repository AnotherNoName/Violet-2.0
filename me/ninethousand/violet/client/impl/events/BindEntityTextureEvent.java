/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 */
package me.ninethousand.violet.client.impl.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class BindEntityTextureEvent {
    private Entity entity;
    private ResourceLocation texture;

    public BindEntityTextureEvent(Entity entity, ResourceLocation texture) {
        this.entity = entity;
        this.texture = texture;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }
}

