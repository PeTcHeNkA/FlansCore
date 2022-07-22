package ru.mrpetchenka.flanscore.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import ru.mrpetchenka.flanscore.network.packets.dummy.DamageMessage;
import ru.mrpetchenka.flanscore.network.packets.gun.PacketGunFire;
import ru.mrpetchenka.flanscore.network.packets.gun.PacketPlaySound;
import ru.mrpetchenka.flanscore.network.packets.gun.PacketShootAdditions;
import ru.mrpetchenka.flanscore.utils.Backend;
import ru.mrpetchenka.flanscore.utils.EnumLog;
import ru.mrpetchenka.flanscore.utils.Logger;

import java.util.*;

//mod packet handler class. Directs packet data to packet classes.
//with much inspiration from http://www.minecraftforge.net/wiki/Netty_Packet_Handling
@ChannelHandler.Sharable
public class PacketHandler extends MessageToMessageCodec<FMLProxyPacket, PacketBase> {

    //map of channels for each side
    private EnumMap<Side, FMLEmbeddedChannel> channels;

    //the list of registered packets. Should contain no more than 256 packets
    private final LinkedList<Class<? extends PacketBase>> packets = new LinkedList<Class<? extends PacketBase>>();

    //whether or not mod has initialised yet. Once true, no more packets may be registered
    private boolean modInitialised = false;

    //initialisation method called from FMLInitializationEvent in mod
    public void init() {
        channels = NetworkRegistry.INSTANCE.newChannel(Backend.modid, this);
        //gun
        registerPacket(PacketGunFire.class);
        registerPacket(PacketShootAdditions.class);
        //dummy
        registerPacket(DamageMessage.class);
        registerPacket(PacketPlaySound.class);
    }

    //post-Initialisation method called from FMLPostInitializationEvent in mod
    //logically sorts the packets client and server side to ensure a matching ordering
    public void postInit() {
        if (modInitialised) return;

        modInitialised = true;

        //define our comparator on the fly and apply it to our list
        Collections.sort(packets, new Comparator<Class<? extends PacketBase>>() {
            @Override
            public int compare(Class<? extends PacketBase> c1, Class<? extends PacketBase> c2) {
                int com = String.CASE_INSENSITIVE_ORDER.compare(c1.getCanonicalName(), c2.getCanonicalName());

                if (com == 0) com = c1.getCanonicalName().compareTo(c2.getCanonicalName());

                return com;
            }
        });
    }

    //registers a packet with the handler
    public boolean registerPacket(Class<? extends PacketBase> cl) {
        if (packets.size() > 256) {
            Logger.log(EnumLog.Error, "Packet limit exceeded in " + Backend.name + " packet handler by packet " + cl.getCanonicalName());
            return false;
        }

        if (packets.contains(cl)) {
            Logger.log(EnumLog.Error, "Tried to register " + cl.getCanonicalName() + " packet class twice");
            return false;
        }

        if (modInitialised) {
            Logger.log(EnumLog.Error, "Tried to register packet " + cl.getCanonicalName() + " after mod initialisation");
            return false;
        }

        packets.add(cl);
        return true;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, PacketBase msg, List<Object> out) throws Exception {
        //define a new buffer to store our data upon encoding
        ByteBuf encodedData = Unpooled.buffer();

        //get the packet class
        Class<? extends PacketBase> cl = msg.getClass();

        //if this packet has not been registered by our handler, reject it
        if (!packets.contains(cl)) throw new NullPointerException("Packet not registered : " + cl.getCanonicalName());

        //like a packet ID. Stored as the first entry in the packet code for recognition
        byte discriminator = (byte) packets.indexOf(cl);
        encodedData.writeByte(discriminator);

        //get the packet class to encode our packet
        msg.encodeInto(ctx, encodedData);

        //convert our packet into a Forge packet to get it through the Netty system
        FMLProxyPacket proxyPacket = new FMLProxyPacket(encodedData.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());

        //add our packet to the outgoing packet queue
        out.add(proxyPacket);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        //get the encoded data from the incoming packet
        ByteBuf encodedData = msg.payload();

        //get the class for interpreting this packet
        byte discriminator = encodedData.readByte();
        Class<? extends PacketBase> cl = packets.get(discriminator);

        //if this discriminator returns no class, reject it
        if (cl == null) throw new NullPointerException("Packet not registered for discriminator : " + discriminator);

        //create an empty packet and decode our packet data into it
        PacketBase packet = cl.newInstance();
        packet.decodeInto(ctx, encodedData.slice());

        //check the side and handle our packet accordingly
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT:
                packet.handleClientSide(getClientPlayer());
                break;
            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                packet.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity);
                break;
        }
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    //send a packet to all players
    public void sendToAll(PacketBase packet) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    //send a packet to a player
    public void sendTo(PacketBase packet, EntityPlayerMP player) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    //send a packet to all around a point
    public void sendToAllAround(PacketBase packet, NetworkRegistry.TargetPoint point) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    //send a packet to all in a dimension
    public void sendToDimension(PacketBase packet, int dimensionID) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionID);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    //send a packet to the server
    public void sendToServer(PacketBase packet) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(packet);
    }

    //vanilla packets follow

    //send a packet to all players
    public void sendToAll(Packet packet) {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
    }

    //send a packet to a player
    public void sendTo(Packet packet, EntityPlayerMP player) {
        player.playerNetServerHandler.sendPacket(packet);
    }

    //send a packet to all around a point
    public void sendToAllAround(Packet packet, NetworkRegistry.TargetPoint point) {
        MinecraftServer.getServer().getConfigurationManager().sendToAllNear(point.x, point.y, point.z, point.range, point.dimension, packet);
    }

    //send a packet to all in a dimension
    public void sendToDimension(Packet packet, int dimensionID) {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dimensionID);
    }

    //send a packet to the server
    public void sendToServer(Packet packet) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }

    //send a packet to all around a point without having to create one's own TargetPoint
    public void sendToAllAround(PacketBase packet, double x, double y, double z, float range, int dimension) {
        sendToAllAround(packet, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }
}