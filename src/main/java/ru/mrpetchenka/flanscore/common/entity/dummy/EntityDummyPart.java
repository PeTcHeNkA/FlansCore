package ru.mrpetchenka.flanscore.common.entity.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class EntityDummyPart extends Entity {
    public final IEntityMultiPartDummy entityDummyObj;
    public final String partName;

    public EntityDummyPart(IEntityMultiPartDummy multiPart, String partName, float sizeX, float sizeY) {
        super(multiPart.getWorld());
        this.setSize(sizeX, sizeY);
        this.entityDummyObj = multiPart;
        this.partName = partName;
    }


    protected void entityInit() {
    }

    //Protected helper method to read subclass entity data from NBT.
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    //Protected helper method to write subclass entity data to NBT.
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }

    //Returns true if other Entities should be prevented from moving through this Entity.
    public boolean canBeCollidedWith() {
        return true;
    }

    //Called when the entity is attacked.
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return !this.isEntityInvulnerable() && this.entityDummyObj.attackEntityFromPart(this, p_70097_1_, p_70097_2_);
    }

    //Returns true if Entity argument is equal to this Entity
    public boolean isEntityEqual(Entity p_70028_1_) {
        return this == p_70028_1_ || this.entityDummyObj == p_70028_1_;
    }
}
