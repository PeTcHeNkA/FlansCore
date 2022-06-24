package ru.mrpetchenka.flanscore.network.packets.dummy;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import ru.mrpetchenka.flanscore.common.entity.EntityDummy;
import ru.mrpetchenka.flanscore.network.PacketBase;

public class SyncEquipmentMessage extends PacketBase {
    private int entityID;
    private int slotId;
    private ItemStack itemstack;

    public SyncEquipmentMessage() {
    }

    public SyncEquipmentMessage(final int entityId, final int slotId, final ItemStack itemstack) {
        this.entityID = entityId;
        this.slotId = slotId;
        this.itemstack = ((itemstack == null) ? null : itemstack.copy());
    }

    @Override
    protected void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(this.entityID);
        data.writeInt(this.slotId);
        ByteBufUtils.writeItemStack(data, this.itemstack);
    }

    @Override
    protected void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.entityID = data.readInt();
        this.slotId = data.readInt();
        this.itemstack = ByteBufUtils.readItemStack(data);
    }

    @Override
    protected void handleServerSide(EntityPlayerMP playerEntity) {

    }

    @Override
    protected void handleClientSide(EntityPlayer clientPlayer) {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID);
        if (entity instanceof EntityDummy) {
            entity.setCurrentItemOrArmor(this.slotId, this.itemstack);
        }
    }
}
