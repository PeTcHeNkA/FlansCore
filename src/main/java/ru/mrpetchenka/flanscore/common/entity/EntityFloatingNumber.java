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

    public EntityFloatingNumber(World world, float damage, double x, double y, double z) {
        super(world);
        this.damage = damage;
        this.posX = x;
        this.lastTickPosX = x;
        this.posY = y;
        this.lastTickPosY = y;
        this.posZ = z;
        this.lastTickPosZ = z;
    }

    public EntityFloatingNumber(World world) {
        super(world);
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

    public void moveEntity(double x, double y, double z) {
    }

    public void reSet(float damage) {
        this.damage = damage;
        this.age = 0;
    }

    protected void readEntityFromNBT(NBTTagCompound nbt) {
    }

    protected void writeEntityToNBT(NBTTagCompound nbt) {
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeFloat(this.damage);
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.damage = additionalData.readFloat();
    }
}
