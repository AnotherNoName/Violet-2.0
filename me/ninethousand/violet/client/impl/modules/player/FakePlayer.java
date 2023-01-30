//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.impl.modules.player;

import com.mojang.authlib.GameProfile;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Module.Manifest(value=Module.Category.PLAYER, description="Spawns a fake player")
public class FakePlayer
extends Module {
    private EntityOtherPlayerMP fakePlayer;
    private static FakePlayer instance;

    @Override
    protected void onEnable() {
        if (Constants.nullCheck()) {
            return;
        }
        this.fakePlayer = new EntityOtherPlayerMP((World)Constants.world(), new GameProfile(Constants.player().getUniqueID(), "Test Dummy"));
        this.fakePlayer.copyLocationAndAnglesFrom((Entity)Constants.player());
        this.fakePlayer.inventory = Constants.player().inventory;
        Constants.world().addEntityToWorld(this.fakePlayer.getEntityId(), (Entity)this.fakePlayer);
    }

    @Override
    protected void onDisable() {
        if (Constants.nullCheck() || this.fakePlayer == null) {
            return;
        }
        Constants.world().removeEntity((Entity)this.fakePlayer);
        this.fakePlayer = null;
    }

    public static FakePlayer getInstance() {
        if (instance == null) {
            instance = new FakePlayer();
        }
        return instance;
    }
}

