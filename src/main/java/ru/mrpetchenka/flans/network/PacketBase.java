package ru.mrpetchenka.flans.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

//base class for all packets
public abstract class PacketBase {
	
	//encode the packet into a ByteBuf stream. Advanced data handlers can be found at @link{cpw.mods.fml.common.network.ByteBufUtils}
	protected abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf data);

	//decode the packet from a ByteBuf stream. Advanced data handlers can be found at @link{cpw.mods.fml.common.network.ByteBufUtils}
	protected abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf data);

	//handle the packet on server side, post-decoding
	protected abstract void handleServerSide(EntityPlayerMP playerEntity);

	//handle the packet on client side, post-decoding
	@SideOnly(Side.CLIENT)
	protected abstract void handleClientSide(EntityPlayer clientPlayer);

	//util method for quickly writing strings
	protected void writeUTF(ByteBuf data, String s) {
		ByteBufUtils.writeUTF8String(data, s);
	}

	//util method for quickly reading strings
	protected String readUTF(ByteBuf data) {
		return ByteBufUtils.readUTF8String(data);
	}
}