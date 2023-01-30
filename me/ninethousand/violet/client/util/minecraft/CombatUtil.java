//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.util.minecraft;

import me.ninethousand.violet.client.util.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CombatUtil {
    public static float getDamage(Entity entity, EntityEnderCrystal crystal) {
        return CombatUtil.getDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    public static float getDamage(Entity entity, BlockPos pos) {
        return CombatUtil.getDamage((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5, entity);
    }

    public static float getDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedSize = entity.getDistance(posX, posY, posZ) / (double)doubleExplosionSize;
        double blockDensity = entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        double v = (1.0 - distancedSize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        if (entity instanceof EntityLivingBase) {
            return CombatUtil.getBlastReduction((EntityLivingBase)entity, CombatUtil.getDamageMultiplied(damage), new Explosion((World)Constants.world(), null, posX, posY, posZ, 6.0f, false, true));
        }
        return 1.0f;
    }

    private static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)ep.getTotalArmorValue(), (float)((float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            damage *= 1.0f - (float)MathHelper.clamp((int)EnchantmentHelper.getEnchantmentModifierDamage((Iterable)ep.getArmorInventoryList(), (DamageSource)DamageSource.causeExplosionDamage((Explosion)explosion)), (int)0, (int)20) / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById((int)11))) {
                damage -= damage / 4.0f;
            }
            return Math.max(damage, 0.0f);
        }
        return CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
    }

    private static float getDamageMultiplied(float damage) {
        int diff = Constants.world().getDifficulty().getDifficultyId();
        return damage * (diff == 0 ? 0.0f : (diff == 1 ? 0.5f : (diff == 2 ? 1.0f : 1.5f)));
    }

    public static int ping() {
        if (Constants.mc.getConnection() == null) {
            return 50;
        }
        if (Constants.mc.player == null) {
            return 50;
        }
        try {
            return Constants.mc.getConnection().getPlayerInfo(Constants.mc.player.getUniqueID()).getResponseTime();
        }
        catch (NullPointerException nullPointerException) {
            return 50;
        }
    }

    private CombatUtil() {
        throw new UnsupportedOperationException();
    }
}

