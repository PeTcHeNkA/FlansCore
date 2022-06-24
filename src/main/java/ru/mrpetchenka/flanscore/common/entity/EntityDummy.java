package ru.mrpetchenka.flanscore.common.entity;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import ru.mrpetchenka.flanscore.FlansCore;
import ru.mrpetchenka.flanscore.common.items.ModItems;
import ru.mrpetchenka.flanscore.network.packets.dummy.DamageMessage;
import ru.mrpetchenka.flanscore.network.packets.dummy.SyncEquipmentMessage;

public class EntityDummy extends EntityLiving implements IEntityAdditionalSpawnData
{
    private float customRotation;
    public float shake;
    public float shakeAnimation;
    public float lastDamage;
    public int lastDamageTick;
    public int firstDamageTick;
    public float damageTaken;
    public EntityFloatingNumber myLittleNumber;

    public EntityDummy(World world) {
        super(world);
        for (int i = 0; i < this.equipmentDropChances.length; ++i) {
            this.equipmentDropChances[i] = 1.1f;
        }
    }

    public void setPlacementRotation(Vec3 lookVector, int side) {
        int r = 0;
        if (side == 0 || side == 1) {
            r = (int)(Math.atan2(lookVector.zCoord, lookVector.xCoord) * 360.0 / 6.283185307179586);
            r += 90;
        }
        else if (side == 2) {
            r = 180;
        }
        else if (side == 3) {
            r = 0;
        }
        else if (side == 4) {
            r = 90;
        }
        else if (side == 5) {
            r = 270;
        }
        this.customRotation = r;
        this.setCustomRotationStuff();
    }

    private void setCustomRotationStuff() {
        float customRotation;
        float r = customRotation = this.customRotation;
        this.rotationYawHead = customRotation;
        this.prevRotationYawHead = customRotation;
        float n = r;
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        this.newRotationYaw = r;
        float n2 = r;
        this.renderYawOffset = n2;
        this.prevRenderYawOffset = n2;
        this.randomYawVelocity = 0.0f;
    }

    protected boolean interact(EntityPlayer player) {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null) {
            Item item = stack.getItem();
            for (int i = 0; i < 4; ++i) {
                if (item.isValidArmor(stack, i, (Entity)player)) {
                    ItemStack armor = this.getEquipmentInSlot(4 - i);
                    if (armor != null && !this.worldObj.isRemote) {
                        this.entityDropItem(armor, 1.0f);
                    }
                    armor = stack.copy();
                    armor.stackSize = 1;
                    if (!this.worldObj.isRemote) {
                        FlansCore.getPacketHandler().sendToAllAround(new SyncEquipmentMessage(this.getEntityId(), 4 - i, armor), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 20.0));
                    }
                    this.setCurrentItemOrArmor(4 - i, armor);
                    ItemStack itemStack = stack;
                    --itemStack.stackSize;
                    return true;
                }
            }
            return false;
        }
        if (!player.isSneaking()) {
            return false;
        }
        for (int j = 0; j < 4; ++j) {
            ItemStack armor2 = this.getEquipmentInSlot(4 - j);
            if (armor2 != null) {
                if (!this.worldObj.isRemote) {
                    if (!player.capabilities.isCreativeMode) {
                        this.entityDropItem(armor2, 1.0f);
                    }
                    FlansCore.getPacketHandler().sendToAllAround(new SyncEquipmentMessage(this.getEntityId(), 4 - j, null), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 20.0));
                }
                this.setCurrentItemOrArmor(4 - j, (ItemStack)null);
                return true;
            }
        }
        return false;
    }

    public void dismantle() {
        if (!this.worldObj.isRemote) {
            this.dropEquipment(true, 999);
            this.dropItem(ModItems.itemDummy, 1);
        }
        this.setDead();
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (source.damageType.equals("player")) {
            EntityPlayer player = (EntityPlayer)source.getEntity();
            if (player.isSneaking() && player.getCurrentEquippedItem() == null) {
                this.dismantle();
                return false;
            }
        }
        if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0f) {
            if (damage <= this.lastDamage) {
                return false;
            }
            this.lastDamage = damage;
        }
        else {
            this.lastDamage = damage;
            this.hurtResistantTime = this.maxHurtResistantTime;
        }
        if (!this.isEntityInvulnerable()) {
            damage = ForgeHooks.onLivingHurt((EntityLivingBase)this, source, damage);
            if (damage > 0.0f) {
                damage = this.applyArmorCalculations(source, damage);
                float f1;
                damage = (f1 = this.applyPotionDamageCalculations(source, damage));
                damage = Math.max(damage - this.getAbsorptionAmount(), 0.0f);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - (f1 - damage));
            }
        }
        if (this.lastDamageTick == this.ticksExisted) {
            this.lastDamage += damage;
            this.shake += damage;
            this.shake = Math.min(this.shake, 30.0f);
        }
        else {
            this.shake = Math.min(damage, 30.0f);
            this.lastDamage = damage;
            this.lastDamageTick = this.ticksExisted;
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
            FlansCore.getPacketHandler().sendToAllAround(new DamageMessage(this.lastDamage, this.shake, this, this.myLittleNumber), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 20.0));
            this.damageTaken += damage;
            if (this.firstDamageTick == 0) {
                this.firstDamageTick = this.ticksExisted;
            }
        }
        return true;
    }

    protected void updateAITasks() {
    }

    public void onUpdate() {
        if (this.shake > 0.0f) {
            ++this.shakeAnimation;
            this.shake -= 0.8f;
            if (this.shake <= 0.0f) {
                this.shakeAnimation = 0.0f;
                this.shake = 0.0f;
            }
        }
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }
        if (this.worldObj.isRemote) {
            this.extinguish();
        }
        if (!this.worldObj.isRemote && this.damageTaken > 0.0f && this.ticksExisted - this.lastDamageTick > 30) {
            if (this.firstDamageTick < this.lastDamageTick) {
                float seconds = (this.lastDamageTick - this.firstDamageTick) / 20.0f + 1.0f;
                float dps = this.damageTaken / seconds;
                EntityFloatingNumber number = new EntityDpsFloatingNumber(this.worldObj, dps, this.posX, this.posY + 3.0, this.posZ);
                this.worldObj.spawnEntityInWorld((Entity)number);
            }
            this.damageTaken = 0.0f;
            this.firstDamageTick = 0;
        }
    }

    public void onEntityUpdate() {
    }

    protected boolean isMovementBlocked() {
        return true;
    }

    public void onLivingUpdate() {
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
        return true;
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeFloat(this.customRotation);
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.customRotation = additionalData.readFloat();
        this.setCustomRotationStuff();
    }

    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setFloat("customRotation", this.customRotation);
        tag.setFloat("shake", this.shake);
    }

    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.customRotation = tag.getFloat("customRotation");
        this.shake = tag.getFloat("shake");
    }
}