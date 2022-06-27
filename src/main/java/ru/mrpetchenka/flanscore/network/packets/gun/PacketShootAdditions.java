package ru.mrpetchenka.flanscore.network.packets.gun;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.mrpetchenka.flanscore.common.entity.gun.EntityBulletTracer;
import ru.mrpetchenka.flanscore.network.PacketBase;
import ru.mrpetchenka.flanscore.utils.EnumLog;
import ru.mrpetchenka.flanscore.utils.Logger;

public class PacketShootAdditions extends PacketBase {
    private String shooter;
    private double mx;
    private double my;
    private double mz;

    public PacketShootAdditions() {
    }

    public PacketShootAdditions(String shooter, double mx, double my, double mz) {
        this.shooter = shooter;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
    }

    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.writeUTF(data, this.shooter);
        data.writeDouble(this.mx);
        data.writeDouble(this.my);
        data.writeDouble(this.mz);
    }

    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.shooter = this.readUTF(data);
        this.mx = data.readDouble();
        this.my = data.readDouble();
        this.mz = data.readDouble();
    }

    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer clientPlayer) {
        EntityPlayer shooter = clientPlayer.worldObj.getPlayerEntityByName(this.shooter);
        if (shooter != null) {
            double y = 0.0;
            double x = (-Math.cos((double) (shooter.rotationYawHead / 180.0f) * 3.14)) * 0.3;
            double z = (-Math.sin((double) (shooter.rotationYawHead / 180.0f) * 3.14)) * 0.3;
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityBulletTracer(shooter, clientPlayer.worldObj, shooter.posX + x, shooter.posY + (double) shooter.getEyeHeight() - 0.20000000298023224 + y, shooter.posZ + z, this.mx, this.my, this.mz));
        }
    }

    public void handleServerSide(EntityPlayerMP playerEntity) {
        Logger.log(EnumLog.Notice, "Received particle packet on server. Disregarding.");
    }
}
