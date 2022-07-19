package ru.mrpetchenka.flanscore.client;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import ru.mrpetchenka.flanscore.client.render.ModRenders;
import ru.mrpetchenka.flanscore.common.CommonProxy;
import ru.mrpetchenka.flanscore.utils.Backend;

public class ClientProxy extends CommonProxy {
    public static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void preInit() {
        super.preInit();

        //window name
        Display.setTitle(Backend.name + " Demo 1.7.10 - " + mc.getSession().getUsername());
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