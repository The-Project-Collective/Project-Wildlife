package com.collective.projectwildlife.renderers.entities.animals.mammals.fox;

import com.collective.projectwildlife.entities.animals.mammals.fox.AmericanRedFoxEntity;
import com.collective.projectwildlife.models.entities.animals.mammals.fox.AmericanRedFoxModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmericanRedFoxRenderer extends GeoEntityRenderer<AmericanRedFoxEntity> {

    public AmericanRedFoxRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AmericanRedFoxModel());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, AmericanRedFoxEntity animatable, BakedGeoModel model, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        float scale = animatable.getAgeScaleData();
        poseStack.scale(scale, scale, scale);
        this.shadowRadius = scale * 0.45F;
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public float getMotionAnimThreshold(AmericanRedFoxEntity animatable) {
        return 0.007f;
    }
}
