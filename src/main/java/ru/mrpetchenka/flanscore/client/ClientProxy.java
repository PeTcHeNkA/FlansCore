package ru.mrpetchenka.flanscore.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import org.lwjgl.opengl.Display;
import ru.mrpetchenka.flanscore.client.render.ModRenders;
import ru.mrpetchenka.flanscore.client.render.RenderDummy;
import ru.mrpetchenka.flanscore.client.render.RenderFloatingNumber;
import ru.mrpetchenka.flanscore.common.CommonProxy;
import ru.mrpetchenka.flanscore.common.entity.EntityDummy;
import ru.mrpetchenka.flanscore.common.entity.EntityFloatingNumber;
import ru.mrpetchenka.flanscore.utils.Backend;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        //window name
        Display.setTitle(Backend.name + " Demo 1.7.10");
    }

    @Override
    public void init() {
        super.init();
        new ModRenders();
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}