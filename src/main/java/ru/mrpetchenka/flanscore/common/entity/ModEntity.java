package ru.mrpetchenka.flanscore.common.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import ru.mrpetchenka.flanscore.FlansCore;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityDummy;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityFloatingNumber;

public class ModEntity {
    public ModEntity() {
        EntityRegistry.registerModEntity(EntityDummy.class, "Dummy", 0, FlansCore.getInstance(), 32, 10, false);
        EntityRegistry.registerModEntity(EntityFloatingNumber.class, "FloatingNumber", 1, FlansCore.getInstance(), 32, 1, false);
    }
}
