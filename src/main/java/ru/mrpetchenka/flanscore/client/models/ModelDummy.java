package ru.mrpetchenka.flanscore.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.mrpetchenka.flanscore.common.entity.EntityDummy;

public class ModelDummy extends ModelBiped {
    public final ModelRenderer standPlate;

    public ModelDummy(float size, float xz) {
        this(size, xz, 64, 64);
    }

    public ModelDummy(float size, float xz, int xw, int yw) {
        super(size, xz, xw, yw);
        //RightArm
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.01f, 1.0f, -2.01f, 4, 8, 4, size);
        this.bipedRightArm.setRotationPoint(-2.5f, 2.0f + xz, 0.0f);
        //LeftArm
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.01f, 1.0f, -2.01f, 4, 8, 4, size);
        this.bipedLeftArm.setRotationPoint(2.5f, 2.0f + xz, 0.0f);
        //LeftLeg
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.addBox(-2.0f, -12.0f, -2.0f, 4, 12, 4, size);
        this.bipedLeftLeg.setRotationPoint(0.0f, 24.0f + xz, 0.0f);
        //RightLeg
        this.bipedRightLeg = new ModelRenderer(this, 0, 0);
        //StandPlate
        this.standPlate = new ModelRenderer(this, 0, 32);
        this.standPlate.addBox(-8.0f, 11.5f, -8.0f, 16, 1, 16, size);
        this.standPlate.setRotationPoint(0.0f, 12.0f + xz, 0.0f);
        //Body
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0f, -24.0f, -2.0f, 8, 12, 4, size);
        this.bipedBody.setRotationPoint(0.0f, 24.0f + xz, 0.0f);
    }

    public void render(Entity entity, float x, float y, float z, float pitchX, float pitchY, float pitchList) {
        this.setRotationAngles(x, y, z, pitchX, pitchY, pitchList, entity);
        if (this.isChild) {
            float f6 = 2.0f;
            GL11.glPushMatrix();
            GL11.glScalef(1.5f / f6, 1.5f / f6, 1.5f / f6);
            GL11.glTranslatef(0.0f, 16.0f * pitchList, 0.0f);
            this.bipedHead.render(pitchList);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0f / f6, 1.0f / f6, 1.0f / f6);
            GL11.glTranslatef(0.0f, 24.0f * pitchList, 0.0f);
            this.bipedBody.render(pitchList);
            this.bipedRightArm.render(pitchList);
            this.bipedLeftArm.render(pitchList);
            this.standPlate.render(pitchList);
            this.bipedLeftLeg.render(pitchList);
            GL11.glPopMatrix();
        } else {
            this.bipedHead.render(pitchList);
            this.bipedBody.render(pitchList);
            this.bipedRightArm.render(pitchList);
            this.bipedLeftArm.render(pitchList);
            this.standPlate.render(pitchList);
            this.bipedLeftLeg.render(pitchList);
        }
    }

    public void setRotationAngles(float x, float y, float z, float pitchX, float pitchY, float pitchList, Entity entity) {
        this.bipedHead.rotateAngleY = pitchX / 57.295776f;
        this.bipedHead.rotateAngleX = pitchY / 57.295776f;
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleY = 0.0f;
        this.bipedLeftArm.rotateAngleY = 0.0f;
        this.bipedHead.rotationPointY = 0.0f;
        this.bipedHeadwear.rotationPointY = 0.0f;
        this.bipedRightArm.rotateAngleZ = 1.5707964f;
        this.bipedLeftArm.rotateAngleZ = -1.5707964f;
        float phase = ((EntityDummy) entity).shakeAnimation;
        float shake = ((EntityDummy) entity).shake;
        float r = 0.0f;
        float r2 = 0.0f;
        if (shake > 0.0f) {
            r = (float) (-(MathHelper.sin(phase) * 3.141592653589793 / 100.0 * shake));
            r2 = (float) (MathHelper.cos(phase) * 3.141592653589793 / 20.0);
        }
        this.bipedLeftLeg.rotateAngleX = r / 2.0f;
        this.bipedBody.rotateAngleX = r / 2.0f;
        this.bipedLeftArm.rotateAngleX = r * 1.1f;
        this.bipedRightArm.rotateAngleX = r * 1.1f;
        this.bipedHead.rotateAngleX = -r;
        this.bipedHead.rotateAngleZ = r2;
    }
}

