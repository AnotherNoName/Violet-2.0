/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.command;

import com.mojang.brigadier.CommandDispatcher;

public interface VioletCommand {
    public void populate(CommandDispatcher<Object> var1);
}

