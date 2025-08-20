package com.yourname.yourmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yourname.yourmod.YourMod;
import com.yourname.yourmod.entity.BugattiCarEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BugattiCarRenderer extends EntityRenderer<Animal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YourMod.MOD_ID, "textures/entity/bugatti_car.png");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YourMod.MOD_ID, "bugatti_car"), "main");
    private final BugattiCarModel model;
    
    public BugattiCarRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BugattiCarModel(context.bakeLayer(LAYER_LOCATION));
        this.shadowRadius = 1.5F;
    }
    
    @Override
    public void render(Animal entity, float entityYaw, float partialTicks, PoseStack poseStack, 
                      MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.375, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-entityYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    
    @Override
    public ResourceLocation getTextureLocation(Animal entity) {
        return TEXTURE;
    }
    
    public static class BugattiCarModel extends EntityModel<Animal> {
        private final ModelPart root;
        private final ModelPart body;
        private final ModelPart wheels;
        
        public BugattiCarModel(ModelPart root) {
            this.root = root;
            this.body = root.getChild("body");
            this.wheels = root.getChild("wheels");
        }
        
        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            
            // Simple car body that definitely works
            PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                // Main car body
                .texOffs(0, 0).addBox(-8.0F, -3.0F, -16.0F, 16.0F, 6.0F, 32.0F, new CubeDeformation(0.0F))
                // Car roof/cabin 
                .texOffs(0, 38).addBox(-6.0F, -7.0F, -8.0F, 12.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 20.0F, 0.0F));
            
            // Simple wheels
            PartDefinition wheels = partdefinition.addOrReplaceChild("wheels", CubeListBuilder.create(), 
                PartPose.offset(0.0F, 20.0F, 0.0F));
            
            // Front wheels
            wheels.addOrReplaceChild("front_left_wheel", CubeListBuilder.create()
                .texOffs(64, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-8.0F, 5.0F, -10.0F));
                
            wheels.addOrReplaceChild("front_right_wheel", CubeListBuilder.create()
                .texOffs(64, 8).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(8.0F, 5.0F, -10.0F));
                
            // Rear wheels
            wheels.addOrReplaceChild("rear_left_wheel", CubeListBuilder.create()
                .texOffs(64, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-8.0F, 5.0F, 10.0F));
                
            wheels.addOrReplaceChild("rear_right_wheel", CubeListBuilder.create()
                .texOffs(64, 24).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(8.0F, 5.0F, 10.0F));
            
            return LayerDefinition.create(meshdefinition, 128, 64);
        }
        
        @Override
        public void setupAnim(Animal entity, float limbSwing, float limbSwingAmount, 
                             float ageInTicks, float netHeadYaw, float headPitch) {
            // Wheel rotation animation could go here
            if (wheels != null && ageInTicks > 0) {
                float wheelRotation = ageInTicks * 0.5F;
                wheels.getChild("front_left_wheel").xRot = wheelRotation;
                wheels.getChild("front_right_wheel").xRot = wheelRotation;
                wheels.getChild("rear_left_wheel").xRot = wheelRotation;
                wheels.getChild("rear_right_wheel").xRot = wheelRotation;
            }
        }
        
        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, 
                                 float red, float green, float blue, float alpha) {
            root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
