/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.util.minecraft;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityPopCham
extends EntityOtherPlayerMP {
    private EntityPlayer master;

    public EntityPopCham(World worldIn, GameProfile gameProfileIn, EntityPlayer master) {
        super(worldIn, gameProfileIn);
        this.master = master;
    }

    public EntityPlayer getMaster() {
        return this.master;
    }
}

