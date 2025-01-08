package com.collective.projectwildlife.entities.animals.mammals.fox;

import com.collective.projectcore.entities.base.CoreAnimalEntity;
import com.collective.projectcore.entities.variant.VariantContext;
import com.collective.projectcore.groups.tags.CoreTags;
import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.WildlifeEntities;
import com.collective.projectwildlife.groups.tags.WildlifeTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AmericanRedFoxEntity extends CoreAnimalEntity implements GeoAnimatable {

    private static final String VARIANTS_PATH = "animal/mammal/fox/american_red/";

    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.american_red.idle");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.american_red.run");

    public AmericanRedFoxEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, true, true, true, true, true, true, true);
    }

    // === MAIN METHODS =======================================================================================================================================================================

    // --- Animations ------------------------------------------------------------------------------------------
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                zooWalkRunIdleRestSleepController(this)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object object) {
        return RenderUtil.getCurrentTick();
    }

    public <T extends Entity & GeoAnimatable> AnimationController<T> zooWalkRunIdleRestSleepController(T foxEntity) {
        return new AnimationController<>(foxEntity, "walk/run/idle/rest/sleep", 0, (state) -> {
            state.setControllerSpeed(1f);
            RawAnimation anim = IDLE;
            if (state.isMoving()) {
                anim = RUN;
                state.setControllerSpeed(8f);
            }
            /*if (state.isMoving()) {
                if (this.getIsSprinting()) {
                    anim = RUN;
                    state.setControllerSpeed(8);
                } else {
                    anim = WALK;
                }
            } else if (this.shouldSleep() && this.canSleep() && !this.isResting() && this.isGoingToSleep() && !this.isSleeping() && this.isAtHome()) {
                anim = GO_TO_SLEEP;
                state.setControllerSpeed(0.85f);
            } else if (this.shouldSleep() && this.canSleep() && this.isSleeping()) {
                anim = SLEEP;
                state.setControllerSpeed(0.8f);
            } else if (this.isSleeping() && !this.shouldSleep()) {
                anim = WAKE_UP;
                state.setControllerSpeed(0.8f);
            } else if (this.isResting()) {
                anim = REST;
                state.setControllerSpeed(0.85f);
            }*/
            return state.setAndContinue(anim);
        });
    }

    // --- Attributes ------------------------------------------------------------------------------------------
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.MAX_HEALTH, 8.0)
                .add(EntityAttributes.ATTACK_DAMAGE, 4.0);
    }

    // --- Goals ------------------------------------------------------------------------------------------
    @Override
    protected void initGoals() {
        //this.goalSelector.add(0, new WildlifeCheckMotherGoal(this));
        //this.goalSelector.add(0, new WildlifeGiveBirthGoal(this, 2.0, 24));
        this.goalSelector.add(1, new SwimGoal(this));
        //this.goalSelector.add(2, new WildlifeBabyFollowGoal(this, 1));
        //this.goalSelector.add(3, new WildlifeBreedGoal(this, 2.0));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
        //this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        //this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
    }

    // --- Initialization ------------------------------------------------------------------------------------------
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setAgeTicks(this.getAdultDays() * 24000);
        this.setBreedingTicks(6000 + random. nextInt(6000));
        this.setGender(random.nextInt(2));
        this.setHunger(this.getMaxFood());
        if (this.getVariant().isEmpty()) {
            this.setVariant(this.calculateWildVariant());
        }
        this.setAttributes(0);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }



    // === VARIANT CONTEXT =======================================================================================================================================================================
    public final VariantContext americanRedFoxVariants = new VariantContext() {
        @Override
        public List<VariantMorph> morphs() {
            return List.of(
                    new Morph("Albino", 1, List.of(2), 0, true, false),
                    new Morph("W_Leucistic", 1, List.of(2, 3, 4), 1, true, false),
                    new Morph("Leucistic", 2, List.of(2, 3, 4, 5, 6), 2, true, false),
                    new Morph("W_Piebald", 3, List.of(4, 5, 6, 7, 8), 1, true, false),
                    new Morph("Piebald", 4, List.of(6, 7, 8, 9, 10), 2, true, false),
                    new Morph("Sandy", 4, List.of(6, 7, 8, 9, 10), 3, true, false),
                    new Morph("Blonde", 5, List.of(8, 9, 10, 11, 12), 2, false, false),
                    new Morph("W_Grey", 6, List.of(10, 11, 12, 13, 14), 4, false, false),
                    new Morph("W_Red", 6, List.of(10, 11, 12, 13, 14), 4, false, false),
                    new Morph("Y_Grey", 6, List.of(10, 11, 12, 13, 14), 3, false, false),
                    new Morph("Spotted", 7, List.of(12, 13, 14, 15, 16), 2, false, false),
                    new Morph("Red", 8, List.of(14, 15, 16, 17, 18), 5, false, false),
                    new Morph("Grey", 8, List.of(14, 15, 16, 17, 18), 4, false, false),
                    new Morph("B_Red", 9, List.of(16, 17, 18, 19, 20), 4, false, false),
                    new Morph("B_Grey", 9, List.of(16, 17, 18, 19, 20), 3, false, false),
                    new Morph("S_Red", 10, List.of(18, 19, 20, 21, 22), 2, false, false),
                    new Morph("S_Grey", 10, List.of(18, 19, 20, 21, 22), 2, false, false),
                    new Morph("Cross", 11, List.of(20, 21, 22, 23, 24), 2, false, true),
                    new Morph("Silver", 12, List.of(22, 23, 24, 25, 26), 3, false, true),
                    new Morph("G_Cross", 13, List.of(24, 25, 26, 27, 28), 1, false, true),
                    new Morph("B_Silver", 14, List.of(26, 27, 28, 29, 30), 2, false, true),
                    new Morph("V_Cross", 15, List.of(28, 29, 30, 31, 32), 1, false, true),
                    new Morph("Black", 16, List.of(30, 31, 32), 0, false, true)
            );
        }

        @Override
        public String calculateWildFunc() {
            System.out.println("===============");
            System.out.println("Calculating Wild Variant!");
            Random random = new Random();
            int silverCheck = random.nextInt(20); // 5%
            int albinoCheck = random.nextInt(100); // 1%
            int chance = random.nextInt(10); // 10%
            for (VariantMorph morph : morphs()) {
                String morphName = morph.name();
                if (morphName.equals("Silver") && silverCheck == 0) { return morphName; }
                if (morphName.equals("Albino") && albinoCheck == 0) {
                    System.out.println("Albino");
                    return morphName; }
                if (morphName.equals("Red")) {
                    if (chance == 0 || chance == 1 || chance == 2 || chance == 3 || chance == 4) {
                        return morphName;
                    }
                }
                if (morphName.equals("W_Red")) {
                    if (chance == 5 || chance == 6 || chance == 7) {
                        return morphName;
                    }
                }
                if (morphName.equals("Grey")) {
                    if (chance == 8 || chance == 9) {
                        return morphName;
                    }
                }
            }
            return "Red";
        }
    };



    // === OVERRIDES =======================================================================================================================================================================

    // --- Age ------------------------------------------------------------------------------------------
    @Override
    public int getAdultDays() {
        return 3;
    }

    // --- Attributes ------------------------------------------------------------------------------------------
    @Override
    public void updateAttributes(int age) {
        if (age == 0) { // Adult
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(0.35D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(6.0D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(8.0D);
            this.setBreedingTicks(this.random.nextInt(6000) + 6000);
        } else if (age == 1) { // Juvenile
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(0.30D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(4.0D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(6.0D);
        } else if (age == 2) { // Child
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(0.25D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(2.0D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(4.0D);
        } else { // Baby
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(0.2D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(1.0D);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(2.0D);
        }
    }

    // --- Breeding ------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return WildlifeEntities.AMERICAN_RED_FOX_ENTITY.get().create(world, SpawnReason.BREEDING);
    }

    @Override
    public int getMaxOffspring() {
        return 8;
    }

    @Override
    public int getMinOffspring() {
        return 2;
    }

    @Override
    public boolean rareOffspring() {
        return false;
    }

    // --- General ------------------------------------------------------------------------------------------
    @Override
    public int getLimitPerChunk() {
        return 8;
    }

    // --- Home Pos ------------------------------------------------------------------------------------------
    @Override
    public boolean isMigratory() {
        return false;
    }

    @Override
    public Block getHomeBlockType() {
        return Blocks.HAY_BLOCK;
    }

    // --- Hunger ------------------------------------------------------------------------------------------
    @Override
    public int getMaxFood() {
        return 40;
    }

    @Override
    public TagKey<Item> getGeneralDiet() {
        return CoreTags.OMNIVORE_FOODS;
    }

    @Override
    public TagKey<Item> getSpecificDiet() {
        return WildlifeTags.AMERICAN_RED_FOX_FOODS;
    }

    // --- Leash ------------------------------------------------------------------------------------------
    @Override
    public boolean canBeLeashed() {
        return !this.hasAngerTime();
    }

    // --- Pregnancy ------------------------------------------------------------------------------------------
    @Override
    public int getGestationTicks() {
        return 5800;
    }

    // --- Roaming ------------------------------------------------------------------------------------------
    @Override
    public int getMaxRoamDistance() {
        if (this.isBaby()) {
            return 1;
        } else if (this.isChild()) {
            return 4;
        } else if (this.isJuvenile()) {
            return 12;
        } else if (this.getGender() == 1) {
            if (this.isPregnant()) {
                return 8;
            } else if (this.isBabyMother()) {
                return 4;
            } else if (this.isChildMother()) {
                return 6;
            }
        } else if (this.getGender() == 0) {
            if (this.isFather()) {
                return 8;
            }
        }
        return 20;
    }

    // --- Size ------------------------------------------------------------------------------------------
    @Override
    public float getMinSize() {
        return 0.25f;
    }

    @Override
    public float getMaleMaxSize() {
        return 1.1f;
    }

    @Override
    public float getFemaleMaxSize() {
        return 0.9f;
    }

    // --- Sounds ------------------------------------------------------------------------------------------
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.hasAngerTime()) {
            return SoundEvents.ENTITY_FOX_AGGRO;
        }
        if (!this.getWorld().isDay() && this.random.nextFloat() < 0.1F) {
            List<PlayerEntity> list = this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(16.0, 16.0, 16.0), EntityPredicates.EXCEPT_SPECTATOR);
            if (list.isEmpty()) {
                return SoundEvents.ENTITY_FOX_SCREECH;
            }
        }
        return SoundEvents.ENTITY_FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FOX_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0f;
    }

    // --- Variants ------------------------------------------------------------------------------------------
    @Override
    public String calculateInheritedVariant(String parent1, String parent2) {
        return americanRedFoxVariants.calculateVariant(parent1, parent2);
    }

    @Override
    public String calculateWildVariant() {
        return americanRedFoxVariants.calculateWildFunc();
    }

    public VariantContext.VariantMorph getDirectVariant() {
        for (VariantContext.VariantMorph morph : americanRedFoxVariants.morphs()) {
            if (morph.name().equals(this.getVariant())) {
                return morph;
            }
        }
        return americanRedFoxVariants.morphs().getFirst();
    }

    public record Morph(String name, int lightValue, List<Integer> possibleLightLevels, int rarity, boolean isLight, boolean isDark) implements VariantContext.VariantMorph {
        public String name() { return name; }
        public int lightValue() { return lightValue; }
        public List<Integer> possibleLightLevels() { return possibleLightLevels; }
        public int rarity() { return rarity; }
        public boolean isLight() { return isLight; }
        public boolean isDark() { return isDark; }
        public String relativeTexturePath() { return VARIANTS_PATH+name().toLowerCase()+".png"; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

}
