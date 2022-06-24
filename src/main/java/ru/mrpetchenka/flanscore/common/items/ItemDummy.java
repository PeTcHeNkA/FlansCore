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
        this.setUnlocalizedName(name);
        this.setTextureName(Backend.modid + ":" + name);
        this.setCreativeTab(ModCreativeTabs.tabFlanGuns);
        GameRegistry.registerItem(this, name);
    }

    public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World world, int x, int y, int z, final int side, final float fx, final float fy, final float fz) {
        if (!world.isRemote) {
            switch (side) {
                case 0: {
                    --y;
                    --y;
                    break;
                }
                case 1: {
                    ++y;
                    break;
                }
                case 2: {
                    --z;
                    break;
                }
                case 3: {
                    ++z;
                    break;
                }
                case 4: {
                    --x;
                    break;
                }
                case 5: {
                    ++x;
                    break;
                }
            }
            final Vec3 foo = player.getLookVec();
            final EntityDummy entity = new EntityDummy(world);
            entity.setPosition(x + 0.5, (double) y, z + 0.5);
            entity.setPlacementRotation(foo, side);
            world.spawnEntityInWorld(entity);
            --stack.stackSize;
        }
        return true;
    }
}
