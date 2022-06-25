package ru.mrpetchenka.flanscore.client.render;


import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import ru.mrpetchenka.flanscore.client.models.ModelDummy;

public class RenderDummy extends RenderBiped {
    private static final ResourceLocation texture = new ResourceLocation("textures/entity/steve.png");
    private static final ModelDummy model = new ModelDummy(0.0f, 0.0f);

    public RenderDummy() {
        super(model, 0.4f);
    }

    protected ResourceLocation getEntityTexture(EntityLiving entity) {
        return texture;
    }
}