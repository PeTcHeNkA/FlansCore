package ru.mrpetchenka.flanscore.client.render;

import cpw.mods.fml.client.registry.RenderingRegistry;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityDummy;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityFloatingNumber;

public class ModRenders {
    public ModRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDummy.class, new RenderDummy());
        RenderingRegistry.registerEntityRenderingHandler(EntityFloatingNumber.class, new RenderFloatingNumber());
    }
}
