package ru.mrpetchenka.flanscore.network.packets.dummy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityDummy;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityFloatingNumber;
import ru.mrpetchenka.flanscore.network.PacketBase;

public class DamageMessage extends PacketBase {

    private float damage;
    private int nrID;

    public DamageMessage() {
    }

    public DamageMessage(float damage, EntityFloatingNumber e2) {
        this.damage = damage;
        this.nrID = ((e2 != null) ? e2.getEntityId() : -1);
    }

    @Override
    protected void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeFloat(this.damage);
        data.writeInt(this.nrID);
    }

    @Override
    protected void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.damage = data.readFloat();
        this.nrID = data.readInt();
    }

    @Override
    protected void handleServerSide(EntityPlayerMP playerEntity) {
    }

    @Override
    protected void handleClientSide(EntityPlayer clientPlayer) {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.nrID);
        if (entity instanceof EntityFloatingNumber) {
            ((EntityFloatingNumber) entity).reSet(this.damage);
        }
    }
}
