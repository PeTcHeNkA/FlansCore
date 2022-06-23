package ru.mrpetchenka.flanscore.common.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.mrpetchenka.flanscore.common.blocks.ModBlocks;

public class ModCreativeTabs {
    public static final CreativeTab tabFlanGuns = new CreativeTab(0);
}

class CreativeTab extends CreativeTabs {
    private final int type;

    CreativeTab(int num) {
        super("tabFlan" + num);
        this.type = num;
    }

    public Item getTabIconItem() {
        return null;
    }

    public ItemStack getIconItemStack() {
        switch (this.type) {
            case 0:
                return new ItemStack(Blocks.wool, 1, 4);
            default:
                return new ItemStack(ModBlocks.workbench);
        }
    }
}
