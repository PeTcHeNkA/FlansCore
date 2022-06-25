package ru.mrpetchenka.flanscore.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDummy extends ModelBiped {
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedEars;
    public ModelRenderer bipedCloak;

    public ModelDummy(float size, float rad) {
        this(size, rad, 64, 32);
    }

    public ModelDummy(float size, float rad, int textureWidth, int textureHeight) {
        super(size, rad, textureWidth, textureHeight);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        //Head
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, size);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + rad, 0.0F);
        //Body
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, size);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + rad, 0.0F);
        //Right arm
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, size);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + rad, 0.0F);
        this.bipedRightArm.rotateAngleZ = ((float)Math.PI / 2F);
        //Left arm
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, size);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + rad, 0.0F);
        this.bipedLeftArm.rotateAngleZ = -((float)Math.PI / 2F);
        //Right leg
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + rad, 0.0F);
        //Left leg
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + rad, 0.0F);
    }

    public void render(Entity entity, float x, float y, float z, float pitchX, float pitchY, float ticks) {
        this.setRotationAngles(x, y, z, pitchX, pitchY, ticks, entity);
        this.bipedHead.render(ticks);
        this.bipedBody.render(ticks);
        this.bipedRightArm.render(ticks);
        this.bipedLeftArm.render(ticks);
        this.bipedRightLeg.render(ticks);
        this.bipedLeftLeg.render(ticks);
    }

    public void setRotationAngles(float x, float y, float z, float pitchX, float pitchY, float ticks, Entity entity) {
    }
}

