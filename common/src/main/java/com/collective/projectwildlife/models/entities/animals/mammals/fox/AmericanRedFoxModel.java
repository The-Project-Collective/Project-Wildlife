package com.collective.projectwildlife.models.entities.animals.mammals.fox;

import com.collective.projectcore.entities.variant.VariantContext;
import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.animals.mammals.fox.AmericanRedFoxEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AmericanRedFoxModel extends GeoModel<AmericanRedFoxEntity> {

    public static final String LOCATION = "entity/animal/mammal/fox/";
    public static final float DEG_TO_RAD = 0.017453292F;

    @Override
    public Identifier getModelResource(AmericanRedFoxEntity animatable, @Nullable GeoRenderer<AmericanRedFoxEntity> renderer) {
        return Identifier.of(ProjectWildlife.MOD_ID, "geo/"+LOCATION+"american_red_fox.geo.json");
    }

    @Override
    public Identifier getTextureResource(AmericanRedFoxEntity object, @Nullable GeoRenderer<AmericanRedFoxEntity> renderer) {
        VariantContext.VariantMorph morph = object.getDirectVariant();
        if (object.isBaby() || object.isChild()) {
            if (morph.name().equals("Albino")) {
                return Identifier.of(ProjectWildlife.MOD_ID, "textures/"+LOCATION+"american_red/baby_albino.png");
            } else if (morph.isLight()) {
                return Identifier.of(ProjectWildlife.MOD_ID, "textures/"+LOCATION+"american_red/baby_light.png");
            } else if (morph.isDark()) {
                return Identifier.of(ProjectWildlife.MOD_ID, "textures/"+LOCATION+"american_red/baby_dark.png");
            } else {
                return Identifier.of(ProjectWildlife.MOD_ID, "textures/"+LOCATION+"american_red/baby.png");
            }
        } else {
            return morph.identifier();
        }
    }

    @Override
    public Identifier getAnimationResource(AmericanRedFoxEntity animatable) {
        return Identifier.of(ProjectWildlife.MOD_ID, "animations/"+LOCATION+"american_red_fox.animation.json");
    }

    @Override
    public void setCustomAnimations(AmericanRedFoxEntity animatable, long instanceId, AnimationState<AmericanRedFoxEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (!animatable.isSleeping()) {
            head.setRotY(extraDataOfType.netHeadYaw() * DEG_TO_RAD / 1.5f);
            if (!animationState.isMoving()) {
                head.setRotX(extraDataOfType.headPitch() * DEG_TO_RAD / 2);
            }
        }
    }
}
