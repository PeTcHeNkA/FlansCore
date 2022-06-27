package ru.mrpetchenka.flanscore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ru.mrpetchenka.flanscore.common.CommonProxy;
import ru.mrpetchenka.flanscore.network.PacketHandler;
import ru.mrpetchenka.flanscore.utils.Backend;
import ru.mrpetchenka.flanscore.utils.EnumLog;
import ru.mrpetchenka.flanscore.utils.Logger;

@Mod(modid = Backend.modid, name = Backend.name, version = Backend.version)
public class FlansCore {
    @Mod.Instance(Backend.modid)
    private static FlansCore instance;

    @SidedProxy(clientSide = Backend.clientSide, serverSide = Backend.serverSide)
    public static CommonProxy proxy;
    private static final PacketHandler packetHandler = new PacketHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        Logger.log(EnumLog.Notice, "Pre-initialization complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        packetHandler.init();
        proxy.init();
        Logger.log(EnumLog.Notice, "Initialization complete");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetHandler.postInit();
        proxy.postInit();
        Logger.log(EnumLog.Notice, "Post-initialization complete");
    }

    public static FlansCore getInstance() {
        return instance;
    }

    public static PacketHandler getPacketHandler() {
        return packetHandler;
    }
}