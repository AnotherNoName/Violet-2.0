/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.item.ItemStack;

public class TooltipEvent
extends CancellableEvent {
    protected final ItemStack stack;
    protected int x;
    protected int y;

    public TooltipEvent(ItemStack stack, int x, int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

