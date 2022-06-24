package ru.mrpetchenka.flanscore.common.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFloatingNumber extends Entity implements IEntityAdditionalSpawnData {
    protected int age;
    public float damage;
    protected int speed;

    public EntityFloatingNumber(final World world) {
        super(world);
    }

    public EntityFloatingNumber(final World world, final float damage, final double x, final double y, final double z) {
        super(world);
        this.damage = damage;
        this.posX = x;
        this.lastTickPosX = x;
        this.posY = y;
        this.lastTickPosY = y;
        this.posZ = z;
        this.lastTickPosZ = z;
    }

    protected void entityInit() {
        this.age = 0;
        this.speed = 500;
    }

    public void onEntityUpdate() {
        if (this.age++ > 50) {
            this.setDead();
        }
        this.posY += this.speed / 500.0;
        if (this.speed > 1) {
            this.speed /= 2;
        } else if (this.speed == 1) {
            this.speed = 0;
        }
    }

    public void moveEntity(final double p_70091_1_, final double p_70091_3_, final double p_70091_5_) {
    }

    public void reSet(final float damage) {
        this.damage = damage;
        this.age = 0;
    }

    protected void readEntityFromNBT(final NBTTagCompound p_70037_1_) {
    }

    protected void writeEntityToNBT(final NBTTagCompound p_70014_1_) {
    }

    public void writeSpawnData(final ByteBuf buffer) {
        buffer.writeFloat(this.damage);
    }

    public void readSpawnData(final ByteBuf additionalData) {
        this.damage = additionalData.readFloat();
    }
}
