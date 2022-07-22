package ru.mrpetchenka.flanscore.network.packets.gun;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import ru.mrpetchenka.flanscore.FlansCore;
import ru.mrpetchenka.flanscore.network.PacketBase;
import ru.mrpetchenka.flanscore.utils.Backend;
import ru.mrpetchenka.flanscore.utils.EnumLog;
import ru.mrpetchenka.flanscore.utils.Logger;

public class PacketPlaySound extends PacketBase {
    private float posX;
    private float posY;
    private float posZ;
    private String sound;
    private boolean distort;
    private boolean silenced;

    public PacketPlaySound() {
    }

    public static void sendSoundPacket(double x, double y, double z, double range, int dimension, String soundName, boolean distort) {
        sendSoundPacket(x, y, z, range, dimension, soundName, distort, false);
    }

    public static void sendSoundPacket(double x, double y, double z, double range, int dimension, String soundName, boolean distort, boolean silenced) {
        if (soundName != null && !soundName.isEmpty()) {
            FlansCore.getPacketHandler().sendToAllAround(new PacketPlaySound(x, y, z, soundName, distort, silenced), x, y, z, (float) range, dimension);
        }

    }

    public PacketPlaySound(double x, double y, double z, String soundName) {
        this(x, y, z, soundName, false);
    }

    public PacketPlaySound(double x, double y, double z, String soundName, boolean distort) {
        this(x, y, z, soundName, distort, false);
    }

    public PacketPlaySound(double x, double y, double z, String soundName, boolean distort, boolean silenced) {
        this.posX = (float) x;
        this.posY = (float) y;
        this.posZ = (float) z;
        this.sound = soundName;
        this.distort = distort;
        this.silenced = silenced;
    }

    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeFloat(this.posX);
        data.writeFloat(this.posY);
        data.writeFloat(this.posZ);
        this.writeUTF(data, this.sound);
        data.writeBoolean(this.distort);
        data.writeBoolean(this.silenced);
    }

    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.posX = data.readFloat();
        this.posY = data.readFloat();
        this.posZ = data.readFloat();
        this.sound = this.readUTF(data);
        this.distort = data.readBoolean();
        this.silenced = data.readBoolean();
    }

    public void handleServerSide(EntityPlayerMP playerEntity) {
        Logger.log(EnumLog.Warning, "Received play sound packet on server. Skipping.");
    }

    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer clientPlayer) {
        FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(Backend.modid, this.sound), 0.3F, 1F, this.posX, this.posY, this.posZ));
    }
}
