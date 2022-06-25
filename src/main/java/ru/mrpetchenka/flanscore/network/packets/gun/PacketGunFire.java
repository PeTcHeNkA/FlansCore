package ru.mrpetchenka.flanscore.network.packets.gun;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import ru.mrpetchenka.flanscore.common.entity.EntityTracerGloomy;
import ru.mrpetchenka.flanscore.common.items.ItemGun;
import ru.mrpetchenka.flanscore.network.PacketBase;
import ru.mrpetchenka.flanscore.utils.EnumLog;
import ru.mrpetchenka.flanscore.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class PacketGunFire extends PacketBase {
    public boolean held;
    public boolean left;
    public float yaw;
    public float pitch;
    public List<EntityTracerGloomy.BulletHitPosition> hits;
    public int entityId;

    public PacketGunFire() {
    }

    public PacketGunFire(boolean l, boolean h, float y, float p, List<EntityTracerGloomy.BulletHitPosition> hits) {
        this.left = l;
        this.held = h;
        this.yaw = y;
        this.pitch = p;
        this.hits = hits;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeBoolean(this.held);
        data.writeBoolean(this.left);
        data.writeFloat(this.yaw);
        data.writeFloat(this.pitch);
        data.writeInt(this.hits.size());
        for (EntityTracerGloomy.BulletHitPosition hit : this.hits) {
            data.writeFloat(hit.distance);
            data.writeDouble(hit.hitVec.xCoord);
            data.writeDouble(hit.hitVec.yCoord);
            data.writeDouble(hit.hitVec.zCoord);
            if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
                data.writeInt(0);
            } else if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                data.writeInt(1);
                data.writeInt(hit.blockX);
                data.writeInt(hit.blockY);
                data.writeInt(hit.blockZ);
                data.writeInt(hit.sideHit);
            } else {
                data.writeInt(2);
                data.writeInt(hit.entityHit.getEntityId());
            }
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.held = data.readBoolean();
        this.left = data.readBoolean();
        this.yaw = data.readFloat();
        this.pitch = data.readFloat();
        int size = data.readInt();
        this.hits = new ArrayList<EntityTracerGloomy.BulletHitPosition>(size);
        for (int i = 0; i < size; ++i) {
            EntityTracerGloomy.BulletHitPosition hit = null;
            float distance = data.readFloat();
            Vec3 hitVec = Vec3.createVectorHelper(data.readDouble(), data.readDouble(), data.readDouble());
            int type = data.readInt();
            switch (type) {
                case 0: {
                    hit = new EntityTracerGloomy.BulletHitPosition(0, 0, 0, 0, hitVec);
                    hit.typeOfHit = MovingObjectPosition.MovingObjectType.MISS;
                    break;
                }
                case 1: {
                    hit = new EntityTracerGloomy.BulletHitPosition(data.readInt(), data.readInt(), data.readInt(), data.readInt(), hitVec);
                    hit.typeOfHit = MovingObjectPosition.MovingObjectType.BLOCK;
                    break;
                }
                case 2: {
                    hit = new EntityTracerGloomy.BulletHitPosition(0, 0, 0, 0, hitVec);
                    hit.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
                    this.entityId = data.readInt();
                    break;
                }
            }
            if (hit != null) {
                hit.distance = distance;
                this.hits.add(hit);
            }
        }
    }

    @Override
    public void handleServerSide(EntityPlayerMP playerEntity) {
        ItemStack currentItem = playerEntity.inventory.getCurrentItem();
        if (currentItem != null && currentItem.getItem() != null && currentItem.getItem() instanceof ItemGun) {
            for (EntityTracerGloomy.BulletHitPosition hit : this.hits) {
                if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    hit.entityHit = playerEntity.worldObj.getEntityByID(this.entityId);
                    if (hit.entityHit != null) {
                        hit.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(playerEntity), 10);
                    }
                    hit.typeOfHit = MovingObjectPosition.MovingObjectType.MISS;
                }
            }
            float bkYaw = playerEntity.rotationYaw;
            float bkPitch = playerEntity.rotationPitch;
            playerEntity.rotationYaw = this.yaw;
            playerEntity.rotationPitch = this.pitch;
            playerEntity.rotationYaw = bkYaw;
            playerEntity.rotationPitch = bkPitch;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer clientPlayer) {
        Logger.log(EnumLog.Warning, "Received gun button packet on client. Skipping.");
    }
}
