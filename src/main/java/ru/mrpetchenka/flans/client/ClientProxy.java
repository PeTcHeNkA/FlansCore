package ru.mrpetchenka.flans.client;

import org.lwjgl.opengl.Display;
import ru.mrpetchenka.flans.common.CommonProxy;
import ru.mrpetchenka.flans.utils.FlansBackend;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();

        //window name
        Display.setTitle(FlansBackend.name + " 1.7.10");
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