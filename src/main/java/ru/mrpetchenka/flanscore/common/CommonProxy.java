package ru.mrpetchenka.flanscore.common;

import ru.mrpetchenka.flanscore.common.blocks.ModBlocks;
import ru.mrpetchenka.flanscore.common.entity.ModEntity;
import ru.mrpetchenka.flanscore.common.items.ModItems;
import ru.mrpetchenka.flanscore.common.tabs.ModCreativeTabs;

public class CommonProxy {
    public void preInit() {
        new ModBlocks();
        new ModItems();
        new ModCreativeTabs();
        new ModEntity();
    }

    public void init() {

    }

    public void postInit() {
    }
}