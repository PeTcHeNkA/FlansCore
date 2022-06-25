package ru.mrpetchenka.flanscore.common.entity;

import net.minecraft.world.World;

public class EntityDpsFloatingNumber extends EntityFloatingNumber {
    public EntityDpsFloatingNumber(World world) {
        super(world);
    }

    public EntityDpsFloatingNumber(World world, float damage, double x, double y, double z) {
        super(world, damage, x, y, z);
    }

    @Override
    protected void entityInit() {
        this.age = 0;
        this.speed = 100;
    }

    @Override
    public void onEntityUpdate() {
        if (this.age++ > 150) {
            this.setDead();
        }
        this.posY += this.speed / 500.0;
        if (this.speed > 1) {
            this.speed /= 2;
        } else if (this.speed == 1) {
            this.speed = 0;
        }
    }
}
