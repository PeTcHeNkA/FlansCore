package ru.mrpetchenka.flanscore.common.items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.mrpetchenka.flanscore.FlansCore;
import ru.mrpetchenka.flanscore.common.entity.gun.EntityTracerGloomy;
import ru.mrpetchenka.flanscore.common.tabs.ModCreativeTabs;
import ru.mrpetchenka.flanscore.network.packets.gun.PacketGunFire;
import ru.mrpetchenka.flanscore.network.packets.gun.PacketShootAdditions;
import ru.mrpetchenka.flanscore.utils.Backend;

import java.util.ArrayList;
import java.util.List;

public class ItemGun extends Item {
    private static boolean rightMouseHeld;

    private static boolean lastRightMouseHeld;
    private static boolean leftMouseHeld;
    private static boolean lastLeftMouseHeld;

    private List<EntityTracerGloomy.BulletHitPosition> hits = new ArrayList<>();

    public ItemGun(String name) {
        this.setMaxStackSize(1);
        this.setTextureName(Backend.modid + ":" + name);
        this.setUnlocalizedName(name);
        this.setNoRepair();
        this.setCreativeTab(ModCreativeTabs.tabFlanGuns);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        GameRegistry.registerItem(this, name);
    }

    public void onUpdate(ItemStack itemstack, World world, Entity pEnt, int i, boolean flag) {
        if (world.isRemote) {
            this.onUpdateClient(itemstack, world, pEnt, i, flag);
        }
    }

    @SideOnly(Side.CLIENT)
    public void onUpdateClient(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).inventory.getCurrentItem() == itemstack) {
            EntityPlayer player = (EntityPlayer) entity;

            lastRightMouseHeld = rightMouseHeld;
            lastLeftMouseHeld = leftMouseHeld;
            rightMouseHeld = Mouse.isButtonDown(0);
            leftMouseHeld = Mouse.isButtonDown(1);

            if ((rightMouseHeld)) {
                this.hits.clear();
                Vec3 posVec = Vec3.createVectorHelper(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
                EntityTracerGloomy.BulletHitPosition hit = EntityTracerGloomy.trace(world, posVec, player.rotationYaw, player.rotationPitch, 0, 128.0F, player);
                this.hits.add(hit);

                //render debug tracer
                FlansCore.getPacketHandler().sendToAll(new PacketShootAdditions(player.getCommandSenderName(), hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord));

                //send packet of fire
                FlansCore.getPacketHandler().sendToServer(new PacketGunFire(true, true, player.rotationYaw, player.rotationPitch, hits));
            }
        }
    }

    public void addInformation(ItemStack i, EntityPlayer p, List list, boolean flag) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add("<Нажмите " + EnumChatFormatting.RED + "Shift" + EnumChatFormatting.GRAY + ">");
        } else {
            list.add("Вставьте магазин");
        }
    }

    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return true;
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        return true;
    }


    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        return true;
    }

    public boolean func_150897_b(Block p_150897_1_) {
        return false;
    }

    @SubscribeEvent
    public void onEventBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemGun) {
            event.setCanceled(true);
        }
    }
}
