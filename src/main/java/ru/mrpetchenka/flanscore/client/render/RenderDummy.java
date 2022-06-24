package ru.mrpetchenka.flanscore.client.render;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import ru.mrpetchenka.flanscore.client.models.ModelDummy;
import ru.mrpetchenka.flanscore.utils.Backend;

public class RenderDummy extends RenderBiped {

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final ResourceLocation texture = new ResourceLocation(Backend.modid, "textures/models/dummy.png");
    private static final ModelDummy model = new ModelDummy(0.0f, 0.0f);

    public RenderDummy() {
        super(model, 0.125f);
    }

    protected ResourceLocation getEntityTexture(EntityLiving entity) {
        return texture;
    }

    protected void func_82421_b() {
        this.field_82423_g = new ModelDummy(1.0f, 0.0f, 64, 32);
        this.field_82425_h = new ModelDummy(0.5f, 0.0f, 64, 32);
        ((ModelDummy) this.field_82423_g).standPlate.showModel = false;
        ((ModelDummy) this.field_82425_h).standPlate.showModel = false;
    }
}