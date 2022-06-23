package ru.mrpetchenka.flans;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ru.mrpetchenka.flans.common.CommonProxy;
import ru.mrpetchenka.flans.network.PacketHandler;
import ru.mrpetchenka.flans.utils.FlansBackend;
import ru.mrpetchenka.flans.utils.FlansEnumLog;
import ru.mrpetchenka.flans.utils.FlansLogger;

@Mod(modid = FlansBackend.modid, name = FlansBackend.name, version = FlansBackend.version)

public class FlansCore {

    @Mod.Instance(FlansBackend.modid)
    private static FlansCore instance;

    @SidedProxy(clientSide = FlansBackend.clientSide, serverSide = FlansBackend.serverSide)
    private static CommonProxy proxy;
    private static final PacketHandler packetHandler = new PacketHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        FlansLogger.log(FlansEnumLog.Notice, "Pre-initialization complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        packetHandler.init();
        proxy.init();
        FlansLogger.log(FlansEnumLog.Notice, "Initialization complete");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetHandler.postInit();
        proxy.postInit();
        FlansLogger.log(FlansEnumLog.Notice, "Post-initialization complete");
    }

    public static FlansCore getInstance() {
        return instance;
    }

    public static PacketHandler getPacketHandler() {
        return packetHandler;
    }
}