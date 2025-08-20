package com.yourname.yourmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yourname.yourmod.YourMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;

public class SimpleCarRenderer extends EntityRenderer<Animal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YourMod.MOD_ID, "textures/entity/bugatti_car.png");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YourMod.MOD_ID, "simple_car"), "main");
    private final CarModel model;
    
    public SimpleCarRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CarModel(context.bakeLayer(LAYER_LOCATION));
        this.shadowRadius = 1.5F;
    }
    
    @Override
    public void render(Animal entity, float entityYaw, float partialTicks, PoseStack poseStack, 
                      MultiBufferSource buffer, int packedLight) {
        
        poseStack.pushPose();
        
        // Position and orient the car properly
        poseStack.translate(0, 1.5, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-entityYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        
        // Render the textured 3D model
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    
    public static class CarModel extends EntityModel<Animal> {
        private final ModelPart root;
        private final ModelPart body;
        private final ModelPart cockpit;
        private final ModelPart spoiler;
        private final ModelPart wheels;
        
        public CarModel(ModelPart root) {
            this.root = root;
            this.body = root.getChild("body");
            this.cockpit = root.getChild("cockpit");
            this.spoiler = root.getChild("spoiler");
            this.wheels = root.getChild("wheels");
        }
        
        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            
            // Modern race car body design
            PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                // Main chassis (lower, sleeker)
                .texOffs(0, 0).addBox(-7.0F, -2.0F, -15.0F, 14.0F, 4.0F, 30.0F, new CubeDeformation(0.0F))
                
                // Front nose cone (aerodynamic)
                .texOffs(0, 34).addBox(-5.0F, -1.5F, -17.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                
                // Rear diffuser/spoiler area
                .texOffs(0, 39).addBox(-6.0F, -1.0F, 15.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                
                // Side air intakes
                .texOffs(58, 0).addBox(-8.0F, -1.0F, -5.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(58, 10).addBox(7.0F, -1.0F, -5.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                
                // Underbody aerodynamics
                .texOffs(76, 0).addBox(-6.0F, 1.5F, -10.0F, 12.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F));
            
            // Cockpit/cabin (much lower and sleeker)
            PartDefinition cockpit = partdefinition.addOrReplaceChild("cockpit", CubeListBuilder.create()
                // Main cockpit
                .texOffs(30, 34).addBox(-4.0F, -5.0F, -6.0F, 8.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                
                // Windshield (angled)
                .texOffs(30, 49).addBox(-3.5F, -5.5F, -7.0F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                
                // Side windows
                .texOffs(46, 49).addBox(-4.5F, -5.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(62, 49).addBox(3.5F, -5.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                
                // Roll cage bars
                .texOffs(78, 49).addBox(-0.5F, -6.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F));
            
            // Racing spoiler (prominent)
            PartDefinition spoiler = partdefinition.addOrReplaceChild("spoiler", CubeListBuilder.create()
                // Main spoiler wing
                .texOffs(100, 0).addBox(-6.0F, -8.0F, 14.0F, 12.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                
                // Spoiler supports
                .texOffs(100, 5).addBox(-4.0F, -7.0F, 15.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(100, 9).addBox(3.0F, -7.0F, 15.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                
                // End plates
                .texOffs(100, 13).addBox(-6.5F, -8.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(100, 15).addBox(5.5F, -8.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F));
            
            // Performance wheels (wider)
            PartDefinition wheels = partdefinition.addOrReplaceChild("wheels", CubeListBuilder.create(), 
                PartPose.offset(0.0F, 18.0F, 0.0F));
            
            // Front wheels (racing tires)
            wheels.addOrReplaceChild("front_left_wheel", CubeListBuilder.create()
                .texOffs(80, 0).addBox(-2.5F, -2.5F, -1.5F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                // Brake disc
                .texOffs(96, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-7.5F, 4.0F, -11.0F));
                
            wheels.addOrReplaceChild("front_right_wheel", CubeListBuilder.create()
                .texOffs(80, 8).addBox(-2.5F, -2.5F, -1.5F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(96, 5).addBox(-2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(7.5F, 4.0F, -11.0F));
                
            // Rear wheels (wider racing slicks)  
            wheels.addOrReplaceChild("rear_left_wheel", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 10).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-7.5F, 4.0F, 12.0F));
                
            wheels.addOrReplaceChild("rear_right_wheel", CubeListBuilder.create()
                .texOffs(80, 26).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 16).addBox(-2.5F, -2.5F, 1.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(7.5F, 4.0F, 12.0F));
            
            return LayerDefinition.create(meshdefinition, 128, 64);
        }
        
        @Override
        public void setupAnim(Animal entity, float limbSwing, float limbSwingAmount, 
                             float ageInTicks, float netHeadYaw, float headPitch) {
            // Animate wheels when moving
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
    
    @Override
    public ResourceLocation getTextureLocation(Animal entity) {
        return TEXTURE;
    }
}
