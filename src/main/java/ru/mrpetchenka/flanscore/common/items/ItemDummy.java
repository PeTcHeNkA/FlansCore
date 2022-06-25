package ru.mrpetchenka.flanscore.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import ru.mrpetchenka.flanscore.common.entity.EntityDummy;
import ru.mrpetchenka.flanscore.common.tabs.ModCreativeTabs;
import ru.mrpetchenka.flanscore.utils.Backend;

public class ItemDummy extends Item {
    public ItemDummy(String name) {
        this.setMaxStackSize(1);
        this.setUnlocalizedName(name);
        this.setTextureName(Backend.modid + ":" + name);
        this.setNoRepair();
        this.setCreativeTab(ModCreativeTabs.tabFlanGuns);
        GameRegistry.registerItem(this, name);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float fx, float fy, float fz) {
        if (!world.isRemote && side == 1) {
            Vec3 foo = player.getLookVec();
            EntityDummy entity = new EntityDummy(world);
            entity.setPosition(x + 0.5, y + 1, z + 0.5);
            entity.setPlacementRotation(foo);
            world.spawnEntityInWorld(entity);
            --stack.stackSize;
        }
        return true;
    }
}
