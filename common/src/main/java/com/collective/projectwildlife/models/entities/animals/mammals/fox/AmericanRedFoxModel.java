package com.collective.projectwildlife.models.entities.animals.mammals.fox;

import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.animals.mammals.fox.AmericanRedFoxEntity;
import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.io.IOException;
import java.util.Map;

public class AmericanRedFoxModel extends GeoModel<AmericanRedFoxEntity> {

    private static final Map<String, Identifier> LOCATION_CACHE = Maps.newHashMap();
    public static final String LOCATION = "entity/animal/mammal/fox/";
    public static final float DEG_TO_RAD = 0.017453292F;

    @Override
    public Identifier getModelResource(AmericanRedFoxEntity animatable, @Nullable GeoRenderer<AmericanRedFoxEntity> renderer) {
        return Identifier.of(ProjectWildlife.MOD_ID, "geo/"+LOCATION+"american_red_fox.geo.json");
    }

    @Override
    public Identifier getTextureResource(AmericanRedFoxEntity object, @Nullable GeoRenderer<AmericanRedFoxEntity> renderer) {
        boolean baby_texture = object.isBaby() || object.isChild();
        String texture_key = object.getUuidAsString().toLowerCase()+"_"+baby_texture;
        if (LOCATION_CACHE.containsKey(texture_key)) {
            return LOCATION_CACHE.get(texture_key);
        } else {
            try {
                NativeImage texture = object.colourAmericanRedFox(object);
                Identifier location = Identifier.of(ProjectWildlife.MOD_ID, "american_red_fox_" +texture_key);
                MinecraftClient.getInstance().getTextureManager().registerTexture(location, new NativeImageBackedTexture(texture));
                LOCATION_CACHE.put(texture_key, location);
                return location;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Texture not working!!!");
            return Identifier.of(ProjectWildlife.MOD_ID, "textures/"+LOCATION+"american_red/base.png");
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
