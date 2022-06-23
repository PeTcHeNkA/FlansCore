package ru.mrpetchenka.flanscore.client;

import org.lwjgl.opengl.Display;
import ru.mrpetchenka.flanscore.common.CommonProxy;
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
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}