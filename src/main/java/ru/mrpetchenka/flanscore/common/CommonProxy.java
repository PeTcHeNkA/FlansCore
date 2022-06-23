package ru.mrpetchenka.flanscore.common;

import ru.mrpetchenka.flanscore.common.blocks.ModBlocks;
import ru.mrpetchenka.flanscore.common.tabs.ModCreativeTabs;

public class CommonProxy {
    public void preInit() {
        new ModBlocks();
        new ModCreativeTabs();
    }

    public void init() {
    }

    public void postInit() {
    }
}