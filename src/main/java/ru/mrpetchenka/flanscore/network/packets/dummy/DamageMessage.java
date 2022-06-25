package ru.mrpetchenka.flanscore.network.packets.dummy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.mrpetchenka.flanscore.common.entity.EntityDummy;
import ru.mrpetchenka.flanscore.common.entity.EntityFloatingNumber;
import ru.mrpetchenka.flanscore.network.PacketBase;

public class DamageMessage extends PacketBase {

    private float damage;
    private float shakeAmount;
    private int entityID;
    private int nrID;

    public DamageMessage() {
    }

    public DamageMessage(float damage, float shakeAmount, EntityDummy entity, EntityFloatingNumber e2) {
        this.damage = damage;
        this.shakeAmount = shakeAmount;
        this.entityID = entity.getEntityId();
        this.nrID = ((e2 != null) ? e2.getEntityId() : -1);
    }

    @Override
    protected void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeFloat(this.damage);
        data.writeFloat(this.shakeAmount);
        data.writeInt(this.entityID);
        data.writeInt(this.nrID);
    }

    @Override
    protected void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.damage = data.readFloat();
        this.shakeAmount = data.readFloat();
        this.entityID = data.readInt();
        this.nrID = data.readInt();
    }

    @Override
    protected void handleServerSide(EntityPlayerMP playerEntity) {

    }

    @Override
    protected void handleClientSide(EntityPlayer clientPlayer) {

        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID);
        if (entity != null && entity instanceof EntityDummy) {
            EntityDummy dummy = (EntityDummy) entity;
            dummy.shake = this.shakeAmount;
            dummy.setCustomNameTag(String.valueOf(this.damage / 2.0f));
        }
        if (this.nrID > 0) {
            entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.nrID);
            if (entity instanceof EntityFloatingNumber) {
                ((EntityFloatingNumber) entity).reSet(this.damage);
            }
        }
    }
}
