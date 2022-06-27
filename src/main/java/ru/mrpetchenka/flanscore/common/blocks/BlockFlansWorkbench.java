package ru.mrpetchenka.flanscore.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import ru.mrpetchenka.flanscore.common.tabs.ModCreativeTabs;
import ru.mrpetchenka.flanscore.utils.Backend;

public class BlockFlansWorkbench extends Block {
    private static IIcon ISide;
    private static IIcon ITop;
    private static IIcon IBottom;

    public BlockFlansWorkbench() {
        super(Material.iron);
        this.setBlockUnbreakable();
        this.setBlockName("workbench");
        this.setBlockTextureName("workbench");
        this.setCreativeTab(ModCreativeTabs.tabFlanGuns);
        GameRegistry.registerBlock(this, "workbench");
    }

    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return ITop;
        }
        if (side == 0) {
            return IBottom;
        }
        return ISide;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        ITop = register.registerIcon(Backend.modid + ":" + "workbenchTop");
        IBottom = register.registerIcon(Backend.modid + ":" + "workbenchBottom");
        ISide = register.registerIcon(Backend.modid + ":" + "workbenchSide");
    }
}
