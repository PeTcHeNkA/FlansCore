package ru.mrpetchenka.flanscore.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.mrpetchenka.flanscore.network.PacketBase;
import ru.mrpetchenka.flanscore.utils.EnumLog;
import ru.mrpetchenka.flanscore.utils.Logger;

public class PacketExample extends PacketBase {
    private int testInt;
    private float testFloat;
    private String testString;
    private boolean testBoolean;
    private String[] testStrings;

    public PacketExample() {
    }

    public PacketExample(int testInt, float testFloat, String testString, boolean testBoolean, String[] testStrings) {
        this.testInt = testInt;
        this.testFloat = testFloat;
        this.testString = testString;
        this.testBoolean = testBoolean;
        this.testStrings = testStrings;
    }

    @Override
    protected void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(testInt);
        data.writeFloat(testFloat);
        writeUTF(data, testString);
        data.writeBoolean(testBoolean);
        data.writeInt(testStrings.length);

        for (String map : testStrings)
            writeUTF(data, map);
    }

    @Override
    protected void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        testInt = data.readInt();
        testFloat = data.readFloat();
        testString = readUTF(data);
        testBoolean = data.readBoolean();
        int testStringsLength = data.readInt();
        testStrings = new String[testStringsLength];

        for (int i = 0; i < testStringsLength; i++)
            testStrings[i] = readUTF(data);
    }

    @Override
    protected void handleServerSide(EntityPlayerMP player) {
        Logger.log(EnumLog.Verbose, "Perform an action on server " + testInt + "  " + testFloat + "  " + testString + "  " + testBoolean);
    }

    @Override
    protected void handleClientSide(EntityPlayer player) {
        Logger.log(EnumLog.Verbose, "Perform an action on client " + testInt + "  " + testFloat + "  " + testString + "  " + testBoolean);
    }
}