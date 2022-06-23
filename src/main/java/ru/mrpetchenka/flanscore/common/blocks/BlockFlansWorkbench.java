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
    private IIcon side;
    private IIcon top;
    private IIcon bottom;

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
            return this.top;
        }
        if (side == 0) {
            return this.bottom;
        }
        return this.side;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.top = register.registerIcon(Backend.modid + ":" + "workbenchTop");
        this.bottom = register.registerIcon(Backend.modid + ":" + "workbenchBottom");
        this.side = register.registerIcon(Backend.modid + ":" + "workbenchSide");
    }
}
