package ru.mrpetchenka.flanscore.common.entity.dummy;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPartDummy {
    World getWorld();

    boolean attackEntityFromPart(EntityDummyPart entity, DamageSource source, float value);
}
