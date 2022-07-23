package ru.mrpetchenka.flanscore.common.entity.dummy;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import ru.mrpetchenka.flanscore.FlansCore;
import ru.mrpetchenka.flanscore.common.items.ModItems;
import ru.mrpetchenka.flanscore.network.packets.dummy.DamageMessage;

public class EntityDummy extends EntityLiving implements IEntityMultiPartDummy, IEntityAdditionalSpawnData {
    private final EntityDummyPart[] dummyPartArray;
    private final EntityDummyPart dummyPartHead;
    public float customRotation;
    public float lastDamage;
    public EntityFloatingNumber myLittleNumber;

    public EntityDummy(World world) {
        super(world);
        this.dummyPartArray = new EntityDummyPart[]{this.dummyPartHead = new EntityDummyPart(this, "head", (this.height - this.width) / 6, (this.height - this.width) / 6)};
    }

    public Entity[] getParts() {
        return this.dummyPartArray;
    }

    public void dismantle(EntityPlayer player) {
        if (!this.worldObj.isRemote && !player.inventory.hasItem(ModItems.itemDummy)) {
            this.dropItem(ModItems.itemDummy, 1);
        }
        this.setDead();
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (source.damageType.equals("player")) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            if (player.isSneaking() && player.getCurrentEquippedItem() == null) {
                this.dismantle(player);
                return false;
            }
        }
        if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0f) {
            if (damage <= this.lastDamage) {
                return false;
            }
            this.lastDamage = damage;
        } else {
            this.lastDamage = damage;
            this.hurtResistantTime = this.maxHurtResistantTime;
        }
        if (!this.isEntityInvulnerable()) {
            damage = ForgeHooks.onLivingHurt(this, source, damage);
            if (damage > 0.0f) {
                damage = this.applyArmorCalculations(source, damage);
                float f1;
                damage = (f1 = this.applyPotionDamageCalculations(source, damage));
                damage = Math.max(damage - this.getAbsorptionAmount(), 0.0f);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - (f1 - damage));
            }
        }
        int n = 10;
        this.maxHurtTime = n;
        this.hurtTime = n;
        if (!this.worldObj.isRemote) {
            if (this.myLittleNumber != null && !this.myLittleNumber.isDead) {
                this.myLittleNumber.setDead();
            }
            EntityFloatingNumber number = new EntityFloatingNumber(this.worldObj, damage, this.posX, this.posY + 2.0, this.posZ);
            this.myLittleNumber = number;
            this.worldObj.spawnEntityInWorld(number);
            FlansCore.getPacketHandler().sendToAllAround(new DamageMessage(this.lastDamage, this.myLittleNumber), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 20.0));
        }
        return false;
    }


    public void onUpdate() {

        this.dummyPartHead.width = this.dummyPartHead.height = (this.height - this.width) / 6;
        this.dummyPartHead.onUpdate();
        this.dummyPartHead.setLocationAndAngles(this.posX, this.posY + this.height - this.dummyPartHead.height, this.posZ, this.rotationYaw, this.rotationPitch);

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }
    }

    public void onLivingUpdate() {
    }

    protected boolean isMovementBlocked() {
        return true;
    }

    public void setPlacementRotation(Vec3 lookVector) {
        this.customRotation = (int) (((Math.atan2(lookVector.zCoord, lookVector.xCoord) * 360.0 / 6.2) + 90) / 45) * 45;
        this.setCustomRotationStuff();
    }

    private void setCustomRotationStuff() {
        this.rotationYawHead = this.customRotation;
        this.prevRotationYawHead = this.customRotation;
        this.rotationYaw = this.customRotation;
        this.prevRotationYaw = this.customRotation;
        this.newRotationYaw = this.customRotation;
        this.renderYawOffset = this.customRotation;
        this.prevRenderYawOffset = this.customRotation;
        this.randomYawVelocity = 0.0f;
    }

    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.customRotation = tag.getFloat("customRotation");
    }

    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setFloat("customRotation", this.customRotation);
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.customRotation = additionalData.readFloat();
        this.setCustomRotationStuff();
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeFloat(this.customRotation);
    }

    protected boolean canDespawn() {
        return false;
    }

    public boolean isEntityAlive() {
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public World getWorld() {
        return this.worldObj;
    }

    public boolean attackEntityFromPart(EntityDummyPart entityPart, DamageSource source, float damage) {
        if (entityPart == this.dummyPartHead) damage *= 2f;

        //Temp... It's bad option of fix problem with sound and hurt entity!
        this.worldObj.setEntityState(this, (byte) 2);
        this.playSound(this.getHurtSound(), this.getSoundVolume() - 0.5f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

        return this.attackEntityFrom(source, damage);
    }

    protected String getHurtSound() {
        return "game.player.hurt";
    }
}