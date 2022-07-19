package ru.mrpetchenka.flanscore.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.mrpetchenka.flanscore.client.ClientProxy;
import ru.mrpetchenka.flanscore.common.entity.dummy.EntityFloatingNumber;

import java.text.DecimalFormat;

public class RenderFloatingNumber extends Render {
    private static final FontRenderer fontRenderer = ClientProxy.mc.fontRenderer;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public void doRender(Entity entity, double x, double y, double z, float picX, float picY) {
        this.doRender((EntityFloatingNumber) entity, x, y, z);
    }

    public void doRender(EntityFloatingNumber entity, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        EntityPlayer player = (EntityPlayer) ClientProxy.mc.thePlayer;
        double xd = player.posX - entity.posX;
        double yd = player.posY - entity.posY;
        double zd = player.posZ - entity.posZ;
        double l = Math.sqrt(xd * xd + yd * yd + zd * zd);
        double scale = 0.01 * l;
        GL11.glScaled(-scale, -scale, scale);
        GL11.glTranslated(0.0, -l / 10.0, 0.0);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        String s = df.format(entity.damage / 2.0f);
        GL11.glTranslated((float) -fontRenderer.getStringWidth(s) / 2, 0.0, 0.0);
        fontRenderer.drawString(s, 0, 0, -1, true);
        GL11.glTranslated((float) fontRenderer.getStringWidth(s) / 2, 0.0, 0.0);
        GL11.glDepthMask(true);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
