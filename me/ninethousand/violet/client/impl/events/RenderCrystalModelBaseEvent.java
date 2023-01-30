/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelRenderer
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.client.model.ModelRenderer;

public class RenderCrystalModelBaseEvent
extends CancellableEvent {
    private ModelRenderer modelRenderer;
    private float j;

    public RenderCrystalModelBaseEvent(ModelRenderer modelRenderer, float j) {
        this.modelRenderer = modelRenderer;
        this.j = j;
    }

    public ModelRenderer getModelRenderer() {
        return this.modelRenderer;
    }

    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public float getJ() {
        return this.j;
    }

    public void setJ(float j) {
        this.j = j;
    }
}

