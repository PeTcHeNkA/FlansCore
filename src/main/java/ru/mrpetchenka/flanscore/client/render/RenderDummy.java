package ru.mrpetchenka.flanscore.client.render;


import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.mrpetchenka.flanscore.client.models.ModelDummy;

public class RenderDummy extends RenderBiped {
    private static final ResourceLocation texture = new ResourceLocation("textures/entity/steve.png");
    private static final ModelDummy model = new ModelDummy(0.0f, 0.0f);

    public RenderDummy() {
        super(model, 0.5f);
    }

    public void doRender(Entity ent, double x, double y, double z, float f, float f1) {
        super.doRender(ent, x, y, z, f, f1);

        Entity entity = ent.getParts()[0];
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);

        float f2 = entity.width / 2.0F;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x - (double) f2, y + (entity.posY - ent.posY), z - (double) f2, x + (double) f2, y + (double) entity.height + (entity.posY - ent.posY), z + (double) f2);
        RenderGlobal.drawOutlinedBoundingBox(axisalignedbb, 0x1fa);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    protected ResourceLocation getEntityTexture(EntityLiving entity) {
        return texture;
    }
}