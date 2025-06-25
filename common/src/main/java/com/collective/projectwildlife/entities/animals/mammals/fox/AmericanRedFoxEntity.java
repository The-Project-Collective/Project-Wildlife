package com.collective.projectwildlife.entities.animals.mammals.fox;

import com.collective.projectcore.entities.CoreAnimalEntity;
import com.collective.projectcore.entities.ai.goals.*;
import com.collective.projectcore.entities.genetics.GeneticContext;
import com.collective.projectcore.groups.tags.CoreTags;
import com.collective.projectcore.util.CoreTextureContext;
import com.collective.projectwildlife.ProjectWildlife;
import com.collective.projectwildlife.entities.*;
import com.collective.projectwildlife.groups.tags.WildlifeTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AmericanRedFoxEntity extends CoreAnimalEntity implements GeoAnimatable {

    private static final String GENETICS_TEXTURES_PATH = "animal/mammal/fox/american_red/";

    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.american_red.idle");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.american_red.run");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.american_red.sleeping");
    public static final RawAnimation REST = RawAnimation.begin().thenLoop("animation.american_red.resting");

    public AmericanRedFoxEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, true, true, true, true, true,
                true, true, true, true, true, true);
        this.lookControl = new AmericanRedFoxLookControl(this);
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

    // For some reason, setting the transitionTickTime to anything above 0 causes random T-posing; needs investigation.
    public <T extends Entity & GeoAnimatable> AnimationController<T> zooWalkRunIdleRestSleepController(T foxEntity) {
        return new AnimationController<>(foxEntity, "walk/run/idle/rest/sleep", 0, (state) -> {
            state.setControllerSpeed(1f);
            RawAnimation anim = IDLE;
            if (state.isMoving()) {
                anim = RUN;
                state.setControllerSpeed(8f);
            } else if (this.isSleeping()) {
                anim = SLEEP;
                state.setControllerSpeed(0.8f);
            } else if (this.isResting()) {
                anim = REST;
                state.setControllerSpeed(0.85f);
            }
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
        this.goalSelector.add(0, new CoreAnimalCheckMotherGoal(this));
        this.goalSelector.add(0, new CoreAnimalMateCheckGoal(this));
        this.goalSelector.add(0, new CoreAnimalGiveBirthGoal(this, 2.0, 24));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new CoreAnimalEatGoal(this, 1.0, 24, 4));
        this.goalSelector.add(1, new CoreAnimalPlayWithEnrichmentGoal(this, 1.0, 24));
        this.goalSelector.add(1, new CoreAnimalCheckGroupGoal(this));
        this.goalSelector.add(1, new CoreAnimalLeaderShrinkGroupGoal(this));
        this.goalSelector.add(1, new CoreAnimalMotherCheckBabiesPackGoal(this));
        this.goalSelector.add(1, new CoreAnimalParentCheckOffspringGoal(this));
        this.goalSelector.add(1, new CoreAnimalLeaderShrinkGroupGoal(this));
        this.goalSelector.add(2, new CoreAnimalLeaderCombineGroupsGoal(this));
        this.goalSelector.add(2, new CoreAnimalCheckGroupLeaderGoal(this));
        this.goalSelector.add(2, new CoreAnimalBabyFollowGoal(this, 0.9f));
        this.goalSelector.add(2, new CoreAnimalFollowLeaderGoal(this, 1.15f));
        this.goalSelector.add(3, new CoreAnimalBreedGoal(this, 2.0));
        this.goalSelector.add(4, new CoreAnimalAvoidEnemyPackGoal<>(this, AmericanRedFoxEntity.class, 24.0F, 1.1, 1.35));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(8, new CoreAnimalWanderFarGoal(this, 1.0));
        this.goalSelector.add(10, new CoreAnimalLookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new CoreAnimalLookAroundGoal(this));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
    }

    // --- Initialization ------------------------------------------------------------------------------------------
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setAgeTicks(this.getAdultDays() * 24000);
        this.setBreedingTicks(6000 + random. nextInt(6000));
        this.setEnrichment(this.getMaxEnrichment());
        this.setGender(random.nextInt(2));
        this.setHunger(this.getMaxFood());
        if (this.getGenome().isEmpty()) {
            if (!spawnReason.equals(SpawnReason.SPAWN_ITEM_USE) && !spawnReason.equals(SpawnReason.SPAWNER)) {
                this.setGenome(this.calculateWildGenome());
                if (random.nextInt(20) == 0) {
                    StringBuilder genome = new StringBuilder(this.getGenome());
                    genome.setCharAt(2, 'b');
                    genome.setCharAt(18, 'b');
                    this.setGenome(genome.toString());
                }
                if (random.nextInt(50) == 0) {
                    StringBuilder genome = new StringBuilder(this.getGenome());
                    genome.setCharAt(3, 'c');
                    genome.setCharAt(19, 'c');
                    this.setGenome(genome.toString());
                }
            } else {
                this.setGenome(this.calculateGenome());
            }
        }
        this.setTirednessTicks(random.nextInt(600) + 2400);
        this.setAttributes(0);
        this.setPack(List.of(this.getUuidAsString()));
        this.calculateDimensions();
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    // --- Ticking ------------------------------------------------------------------------------------------
    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.hasGenetics() && this.getGenome() != null && !this.getGenome().isEmpty()) {
            if (!this.isGeneticallyViable(this.getGenome()) && this.getWorld() instanceof ServerWorld serverWorld) {
                this.damage(serverWorld, this.getDamageSources().genericKill(), this.getMaxHealth() + 10);
            }
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getMainHandStack();
        if (hand == Hand.MAIN_HAND && !player.getWorld().isClient()) {
            if (itemStack.getItem().equals(Items.IRON_NUGGET)) {
                this.setEnrichment(0);
                this.setEnrichmentCooldown(0);
            }
        }
        return super.interactMob(player, hand);
    }

    // === GENETICS =======================================================================================================================================================================

    // --- Appearance Genes ------------------------------------------------------------------------------------------
    public record Red() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Red"; }
        public List<String> alleles() { return List.of("A", "a"); }
        public List<String> wildAlleles() { return List.of("A"); }
        public boolean dominant() { return true; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("base", "base.png");
            put("body", "body.png");
            put("underbelly", "underbelly.png");
            put("points", "points.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("AABB", new Color(166, 78, 42)); // Red
            put("AaBB", new Color(166, 78, 42)); // Red
            put("AABb", new Color(146, 58, 22)); // Gold
            put("AaBb", new Color(146, 58, 22)); // Gold
            put("Smoke", new Color(60, 60, 60));
            put("Black", new Color(40, 40, 40));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Silver() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Silver"; }
        public List<String> alleles() { return List.of("B", "b"); }
        public List<String> wildAlleles() { return List.of("B"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("AAbb", new Color(60, 60, 60)); // Silver
            put("Aabb", new Color(70, 70, 70)); // Sub-Standard Silver
            put("aabb", new Color(50, 50, 50)); // Double Silver
            put("aaBB", new Color(55, 52, 52)); // Alaskan Silver
            put("aaBb", new Color(65, 62, 62)); // Sub-Alaskan Silver
            put("Black", new Color(40, 40, 40));
            put("Pitch", new Color(20, 20, 20));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Albino() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Albino"; }
        public List<String> alleles() { return List.of("C", "c"); }
        public List<String> wildAlleles() { return List.of("C"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return true; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("cc", new Color(255, 245, 245)); // Albino
            put("Pink", new Color(250, 150, 150));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Pastel() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Pastel"; }
        public List<String> alleles() { return List.of( "E", "e"); }
        public List<String> wildAlleles() { return List.of("E"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
            put("points", "points.png");
            put("underbelly", "underbelly.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("ee", new Color(208, 185, 171));
            put("Brown", new Color(70, 49, 28));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record FireFactor() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "FireFactor"; }
        public List<String> alleles() { return List.of("F", "f"); }
        public List<String> wildAlleles() { return List.of("F"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return true; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("Ff", new Color(255, 198, 162)); // Heterozygous
            put("ff", new Color(255, 235, 215)); // Homozygous
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Burgundy() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Burgundy"; }
        public List<String> alleles() { return List.of("G", "g"); }
        public List<String> wildAlleles() { return List.of("G"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("points", "points.png");
            put("underbelly", "underbelly.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("gg", new Color(248, 175, 101));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Pearl() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Pearl"; }
        public List<String> alleles() { return List.of("P", "p"); }
        public List<String> wildAlleles() { return List.of("P"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
            put("points", "points.png");
            put("underbelly", "underbelly.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("pp", new Color(180, 180, 180)); // Pearl
            put("Brown", new Color(130, 59, 38));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record MansfieldPearl() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "MansfieldPearl"; }
        public List<String> alleles() { return List.of("S", "s"); }
        public List<String> wildAlleles() { return List.of("S"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
            put("points", "points.png");
            put("underbelly", "underbelly.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("ss", new Color(190, 167, 170)); // Mansfield Pearl
            put("Brown", new Color(120, 59, 38));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Colicott() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Colicott"; }
        public List<String> alleles() { return List.of("T", "t"); }
        public List<String> wildAlleles() { return List.of("T"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
            put("points", "points.png");
            put("underbelly", "underbelly.png");
            put("nose", "nose.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("tt", new Color(235, 185, 151)); // Colicott
            put("Blue", new Color(66, 123, 131));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record Radium() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "Radium"; }
        public List<String> alleles() { return List.of("R", "r"); }
        public List<String> wildAlleles() { return List.of("R"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("body", "body.png");
            put("points", "points.png");
            put("eyes", "eyes.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("rr", new Color(180, 180, 190)); // Radium
            put("Pink", new Color(195, 108, 72));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    public record WhiteSeries() implements GeneticContext.Gene, CoreTextureContext.BaseGeneTexture {
        public String name() { return "WhiteSeries"; }
        public List<String> alleles() { return List.of("W", "Q", "O", "M", "w"); }
        public List<String> wildAlleles() { return List.of("w"); }
        public boolean dominant() { return false; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return true; }
        public List<String> lethalGenes() { return List.of("WW", "WQ", "WO", "QQ", "QO", "OO"); }
        public String relativeTexturePath() { return GENETICS_TEXTURES_PATH; }
        public HashMap<String, String> textures() { return new HashMap<>() {{
            put("W", "white_mark.png");
            put("Q", "platinum.png");
            put("O", "georgian_white.png");
            put("M", "marble.png");
            put("MM", "white_marble.png");
        }}; }
        public HashMap<String, Color> colours() { return new HashMap<>() {{
            put("White", new Color(255, 255, 255));
        }}; }
        public String modID() { return ProjectWildlife.MOD_ID; }
    }

    // --- Stat Genes ------------------------------------------------------------------------------------------
    public record Size() implements GeneticContext.Gene {
        public String name() { return "Size"; }
        public List<String> alleles() { return List.of("A", "a", "B", "b", "C", "c", "D", "d"); }
        public List<String> wildAlleles() { return List.of("C", "c", "D", "d"); }
        public boolean dominant() { return true; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
    }

    public record Health() implements GeneticContext.Gene {
        public String name() { return "Health"; }
        public List<String> alleles() { return List.of("A", "a", "B", "b", "C", "c", "D", "d"); }
        public List<String> wildAlleles() { return List.of("C", "c", "D", "d"); }
        public boolean dominant() { return true; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
    }

    public record Speed() implements GeneticContext.Gene {
        public String name() { return "Speed"; }
        public List<String> alleles() { return List.of("A", "a", "B", "b", "C", "c", "D", "d"); }
        public List<String> wildAlleles() { return List.of("C", "c", "D", "d"); }
        public boolean dominant() { return true; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
    }

    public record AttackDamage() implements GeneticContext.Gene {
        public String name() { return "AttackDamage"; }
        public List<String> alleles() { return List.of("A", "a", "B", "b", "C", "c", "D", "d"); }
        public List<String> wildAlleles() { return List.of("C", "c", "D", "d"); }
        public boolean dominant() { return true; }
        public boolean partialDominant() { return false; }
        public boolean homozygousLethal() { return false; }
        public List<String> lethalGenes() { return null; }
    }

    // --- Methods ------------------------------------------------------------------------------------------
    public NativeImage colourAmericanRedFox(AmericanRedFoxEntity fox) throws IOException {
        return coreTextureContext.colourEntity(fox);
    }

    public static final GeneticContext geneticsContext = () -> List.of(
            new Red(), new Silver(), new Albino(), new Pastel(), new FireFactor(), new Burgundy(),
            new Pearl(), new MansfieldPearl(), new Colicott(), new Radium(), new WhiteSeries(),
            new Size(), new Health(), new Speed(), new AttackDamage());

    public static final CoreTextureContext coreTextureContext = new CoreTextureContext() {
        public List<BaseGeneTexture> geneTextures() { return List.of(
                    new Red(), new Silver(), new Albino(), new Pastel(), new FireFactor(), new Burgundy(),
                    new Pearl(), new MansfieldPearl(), new Colicott(), new Radium(), new WhiteSeries());
        }
        public String animalName() { return "american_red_fox"; }
        public NativeImage colourSpecificEntities(CoreAnimalEntity coreAnimalEntity, List<BaseGeneTexture> list, String s) {
            NativeImage final_image = getNativeImageFromResourceLocation(Identifier.of(ProjectWildlife.MOD_ID, "textures/entity/"+GENETICS_TEXTURES_PATH+"base.png"));
            if (coreAnimalEntity.getGenome() != null && !coreAnimalEntity.getGenome().isEmpty()) {
                String genome = coreAnimalEntity.getGenome();

                // === Textures ===
                // -- Base Images --
                NativeImage body = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "body.png"));
                NativeImage underbelly = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "underbelly.png"));
                NativeImage points = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "points.png"));
                NativeImage tail_tip = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "tail_tip.png"));
                NativeImage nose = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "nose.png"));
                NativeImage eyes = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "eyes.png"));
                NativeImage statics = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "statics.png"));

                // -- Patterns --
                NativeImage cross_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "cross.png"));
                NativeImage silver_cross_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "silver_cross.png"));
                NativeImage leucistic_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "leucistic.png"));
                NativeImage white_mark_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "white_mark.png"));
                NativeImage platinum_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "platinum.png"));
                NativeImage georgian_white_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "georgian_white.png"));
                NativeImage marble_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "marble.png"));
                NativeImage white_marble_pattern = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "white_marble.png"));


                // === Alleles ===
                String baseAlleles = geneticsContext.getAlleles(genome, 0) + geneticsContext.getAlleles(genome, 1);
                String albinoAlleles = geneticsContext.getAlleles(genome, 2);
                String pastelAlleles = geneticsContext.getAlleles(genome, 3);
                String fireFoxAlleles = geneticsContext.getAlleles(genome, 4);
                String burgundyAlleles = geneticsContext.getAlleles(genome, 5);
                String pearlAlleles = geneticsContext.getAlleles(genome, 6);
                String mansfieldPearlAlleles = geneticsContext.getAlleles(genome, 7);
                String colicottAlleles = geneticsContext.getAlleles(genome, 8);
                String radiumAlleles = geneticsContext.getAlleles(genome, 9);
                String whiteSeriesAlleles = geneticsContext.getAlleles(genome, 10);


                // === Texture Calculations ===
                // -- Base --
                boolean baby_texture = coreAnimalEntity.isBaby() || coreAnimalEntity.isChild();
                boolean albino = false;
                boolean leucistic = false;
                boolean radium = false;
                boolean red = false;
                boolean gold = false;
                boolean cross = false;
                boolean silver_cross = false;
                boolean white_mark = false;
                boolean platinum = false;
                boolean georgian_white = false;
                boolean marble = false;
                boolean white_marble = false;
                stainLayer(underbelly, new Color(255, 255, 255));
                stainLayer(tail_tip, new Color(255, 255, 255));
                // Albino
                if (!albinoAlleles.equals("CC")) {
                    if (albinoAlleles.equals("cc")) {
                        stainLayer(eyes, geneTextures().get(2).colours().get("Pink"));
                        stainLayer(nose, geneTextures().get(2).colours().get("Pink"));
                        albino = true;
                    } else if (albinoAlleles.equals("Cc")){
                        leucistic = true;
                    }
                }
                if (!albino) {
                    // Radium
                    if (geneTextures().get(9).colours().containsKey(radiumAlleles)) {
                        if (baby_texture) {
                            stainLayer(body, new Color(85, 75, 70));
                            stainLayer(underbelly, new Color(85, 75, 70));
                            stainLayer(points, new Color(85, 75, 70));
                            stainLayer(nose, new Color(135, 95, 95));
                        } else {
                            stainLayer(body, geneTextures().get(9).colours().get(radiumAlleles));
                            stainLayer(underbelly, geneTextures().get(9).colours().get(radiumAlleles));
                            stainLayer(points, geneTextures().get(9).colours().get(radiumAlleles));
                            stainLayer(nose, geneTextures().getFirst().colours().get("Black"));
                        }
                        stainLayer(eyes, geneTextures().get(9).colours().get("Pink"));
                        radium = true;
                    }
                    if (!radium) {
                        // Red
                        if (geneTextures().getFirst().colours().containsKey(baseAlleles)) {
                            if (baby_texture) {
                                stainLayer(body, new Color(85, 75, 70));
                                stainLayer(underbelly, new Color(85, 75, 70));
                                stainLayer(points, new Color(85, 75, 70));
                                stainLayer(nose, new Color(135, 95, 95));
                            } else {
                                stainLayer(body, geneTextures().getFirst().colours().get(baseAlleles));
                                stainLayer(points, geneTextures().getFirst().colours().get("Smoke"));
                                stainLayer(nose, geneTextures().getFirst().colours().get("Black"));
                                // Gold
                                if (baseAlleles.equals("AABb") || baseAlleles.equals("AaBb")) {
                                    stainLayer(underbelly, geneTextures().getFirst().colours().get("Smoke"));
                                    gold = true;
                                } else {
                                    red = true;
                                }
                                // Cross
                                if (baseAlleles.equals("AaBB")) {
                                    stainLayer(cross_pattern, geneTextures().getFirst().colours().get("Smoke"));
                                    cross = true;
                                }
                                // Silver Cross
                                if (baseAlleles.equals("AaBb")) {
                                    stainLayer(silver_cross_pattern, geneTextures().getFirst().colours().get("Smoke"));
                                    silver_cross = true;
                                }
                            }
                            stainLayer(eyes, geneTextures().getFirst().colours().get("Black"));
                            // Silver
                        } else if (geneTextures().get(1).colours().containsKey(baseAlleles)) {
                            if (baby_texture) {
                                stainLayer(body, new Color(85, 75, 70));
                                stainLayer(underbelly, new Color(85, 75, 70));
                                stainLayer(points, new Color(85, 75, 70));
                                stainLayer(nose, new Color(135, 95, 95));
                            } else {
                                stainLayer(body, geneTextures().get(1).colours().get(baseAlleles));
                                stainLayer(underbelly, geneTextures().get(1).colours().get(baseAlleles));
                                stainLayer(points, geneTextures().get(1).colours().get(baseAlleles));
                                stainLayer(nose, geneTextures().getFirst().colours().get("Black"));
                            }
                            stainLayer(eyes, geneTextures().get(1).colours().get("Pitch"));
                        }
                        // -- Dilutions --
                        // Pastel
                        if (pastelAlleles.equals("ee")) {
                            if (!baby_texture) {
                                NativeImage pastel_image = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "base.png"));
                                stainLayer(pastel_image, geneTextures().get(3).colours().get(pastelAlleles));
                                if (red || gold) {
                                    softLightImages(points, pastel_image);
                                    if (gold) {
                                        softLightImages(underbelly, pastel_image);
                                    }
                                    if (cross) {
                                        softLightImages(cross_pattern, pastel_image);
                                    }
                                    if (silver_cross) {
                                        softLightImages(silver_cross_pattern, pastel_image);
                                    }
                                } else {
                                    softLightImages(body, pastel_image);
                                    softLightImages(underbelly, pastel_image);
                                    softLightImages(points, pastel_image);
                                }
                            }
                            eyes = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "eyes.png"));
                            stainLayer(eyes, geneTextures().get(3).colours().get("Brown"));
                        }
                        // Fire Factor
                        if (!baby_texture) {
                            if (red && !fireFoxAlleles.equals("FF") || gold & geneTextures().get(4).colours().containsKey(fireFoxAlleles)) {
                                NativeImage fire_fox_image = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "base.png"));
                                if (fireFoxAlleles.equals("Ff")) {
                                    stainLayer(fire_fox_image, geneTextures().get(4).colours().get(fireFoxAlleles));
                                    softLightImages(body, fire_fox_image);
                                } else {
                                    body = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "body.png"));
                                    stainLayer(body, geneTextures().get(4).colours().get(fireFoxAlleles));
                                }
                            }
                        }
                        // Burgundy
                        if (burgundyAlleles.equals("gg")) {
                            if (!baby_texture) {
                                NativeImage burgundy_image = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "base.png"));
                                stainLayer(burgundy_image, geneTextures().get(5).colours().get(burgundyAlleles));
                                if (red || gold) {
                                    softLightImages(points, burgundy_image);
                                    if (gold) {
                                        softLightImages(underbelly, burgundy_image);
                                    }
                                    if (cross) {
                                        softLightImages(cross_pattern, burgundy_image);
                                    }
                                    if (silver_cross) {
                                        softLightImages(silver_cross_pattern, burgundy_image);
                                    }
                                } else {
                                    softLightImages(body, burgundy_image);
                                    softLightImages(underbelly, burgundy_image);
                                    softLightImages(points, burgundy_image);
                                }
                            }
                        }
                        // Pearl
                        if (pearlAlleles.equals("pp")) {
                            if (!baby_texture) {
                                NativeImage pearl_image = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "base.png"));
                                stainLayer(pearl_image, geneTextures().get(6).colours().get(pearlAlleles));
                                if (red || gold) {
                                    softLightImages(points, pearl_image);
                                    if (gold) {
                                        softLightImages(underbelly, pearl_image);
                                    }
                                    if (cross) {
                                        softLightImages(cross_pattern, pearl_image);
                                    }
                                    if (silver_cross) {
                                        softLightImages(silver_cross_pattern, pearl_image);
                                    }
                                } else {
                                    softLightImages(body, pearl_image);
                                    softLightImages(underbelly, pearl_image);
                                    softLightImages(points, pearl_image);
                                }
                            }
                            eyes = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "eyes.png"));
                            stainLayer(eyes, geneTextures().get(6).colours().get("Brown"));
                        }
                        // Mansfield Pearl
                        if (mansfieldPearlAlleles.equals("ss")) {
                            if (!baby_texture) {
                                NativeImage mansfield_pearl_image = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "base.png"));
                                stainLayer(mansfield_pearl_image, geneTextures().get(7).colours().get(mansfieldPearlAlleles));
                                if (red || gold) {
                                    softLightImages(points, mansfield_pearl_image);
                                    if (gold) {
                                        softLightImages(underbelly, mansfield_pearl_image);
                                    }
                                    if (cross) {
                                        softLightImages(cross_pattern, mansfield_pearl_image);
                                    }
                                    if (silver_cross) {
                                        softLightImages(silver_cross_pattern, mansfield_pearl_image);
                                    }
                                } else {
                                    softLightImages(body, mansfield_pearl_image);
                                    softLightImages(underbelly, mansfield_pearl_image);
                                    softLightImages(points, mansfield_pearl_image);
                                }
                            }
                            eyes = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "eyes.png"));
                            stainLayer(eyes, geneTextures().get(7).colours().get("Brown"));
                        }
                        // Colicott
                        if (geneTextures().get(8).colours().containsKey(colicottAlleles)) {
                            if (!baby_texture) {
                                NativeImage colicott_image = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "base.png"));
                                stainLayer(colicott_image, geneTextures().get(8).colours().get(colicottAlleles));
                                if (red || gold) {
                                    softLightImages(points, colicott_image);
                                    if (gold) {
                                        softLightImages(underbelly, colicott_image);
                                    }
                                    if (cross) {
                                        softLightImages(cross_pattern, colicott_image);
                                    }
                                    if (silver_cross) {
                                        softLightImages(silver_cross_pattern, colicott_image);
                                    }
                                } else {
                                    softLightImages(body, colicott_image);
                                    softLightImages(underbelly, colicott_image);
                                    softLightImages(points, colicott_image);
                                }
                            }
                            eyes = getNativeImageFromResourceLocation(Identifier.of(geneTextures().getFirst().identifier() + "eyes.png"));
                            stainLayer(eyes, geneTextures().get(8).colours().get("Blue"));
                        }
                    }
                // -- Patterns --
                    // White Mark
                    if (whiteSeriesAlleles.equals("Ww") || whiteSeriesAlleles.equals("WM")) {
                        white_mark = true;
                        if (whiteSeriesAlleles.equals("WM")) {
                            marble = true;
                        }
                    }
                    // Platinum
                    if (whiteSeriesAlleles.equals("Qw") || whiteSeriesAlleles.equals("QM")) {
                        platinum = true;
                        if (whiteSeriesAlleles.equals("QM")) {
                            marble = true;
                        }
                    }
                    // Georgian White
                    if (whiteSeriesAlleles.equals("Ow") || whiteSeriesAlleles.equals("OM")) {
                        georgian_white = true;
                        if (whiteSeriesAlleles.equals("OM")) {
                            marble = true;
                        }
                    }
                    // Marble
                    if (whiteSeriesAlleles.equals("Mw")) {
                        marble = true;
                    }
                    // White Marble
                    if (whiteSeriesAlleles.equals("MM")) {
                        white_marble = true;
                    }
                }
                stainLayer(final_image, new Color(255, 255, 255));
                if (albino || baby_texture) {
                    combineWeightedImages(final_image, body);
                    combineWeightedImages(final_image, points);
                    combineWeightedImages(final_image, underbelly);
                } else {
                    multiplyImages(final_image, body, 1f);
                    multiplyImages(final_image, points, 1f);
                    if (red) {
                        softLightImages(final_image, underbelly);
                    } else {
                        multiplyImages(final_image, underbelly, 1.3f);
                    }
                }
                if (cross) {
                    combineWeightedImages(final_image, cross_pattern);
                }
                if (silver_cross) {
                    combineWeightedImages(final_image, silver_cross_pattern);
                }
                if (white_mark) {
                    combineWeightedImages(final_image, white_mark_pattern);
                }
                if (platinum) {
                    combineWeightedImages(final_image, platinum_pattern);
                }
                if (georgian_white) {
                    combineWeightedImages(final_image, georgian_white_pattern);
                }
                if (marble) {
                    combineWeightedImages(final_image, marble_pattern);
                }
                if (white_marble) {
                    combineWeightedImages(final_image, white_marble_pattern);
                }
                if (leucistic) {
                    combineWeightedImages(final_image, leucistic_pattern);
                }
                softLightImages(final_image, tail_tip);
                combineImages(final_image, nose);
                combineImages(final_image, eyes);
                combineImages(final_image, statics);
            }
            return final_image;
        }
    };

    // === OVERRIDES =======================================================================================================================================================================

    // --- Age ------------------------------------------------------------------------------------------
    @Override
    public int getAdultDays() {
        return 4;
    }

    // --- Attributes ------------------------------------------------------------------------------------------
    @Override
    public void updateAttributes(int age) {
        if (this.hasGenetics() && this.getGenome() != null && !this.getGenome().isEmpty()) {
            if (age == 0) { // Adult
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(this.calculateStats(
                        0.35D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MOVEMENT_SPEED), this.getAttributeCoeff(EntityAttributes.MOVEMENT_SPEED)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(this.calculateStats(
                        6.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.ATTACK_DAMAGE), this.getAttributeCoeff(EntityAttributes.ATTACK_DAMAGE)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(this.calculateStats(
                        8.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MAX_HEALTH), this.getAttributeCoeff(EntityAttributes.MAX_HEALTH)));
                this.setBreedingTicks(this.random.nextInt(6000) + 6000);
            } else if (age == 1) { // Juvenile
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(this.calculateStats(
                        0.30D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MOVEMENT_SPEED), this.getAttributeCoeff(EntityAttributes.MOVEMENT_SPEED)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(this.calculateStats(
                        4.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.ATTACK_DAMAGE), this.getAttributeCoeff(EntityAttributes.ATTACK_DAMAGE)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(this.calculateStats(
                        6.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MAX_HEALTH), this.getAttributeCoeff(EntityAttributes.MAX_HEALTH)));
            } else if (age == 2) { // Child
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(this.calculateStats(
                        0.25D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MOVEMENT_SPEED), this.getAttributeCoeff(EntityAttributes.MOVEMENT_SPEED)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(this.calculateStats(
                        2.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.ATTACK_DAMAGE), this.getAttributeCoeff(EntityAttributes.ATTACK_DAMAGE)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(this.calculateStats(
                        4.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MAX_HEALTH), this.getAttributeCoeff(EntityAttributes.MAX_HEALTH)));
            } else { // Baby
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)).setBaseValue(this.calculateStats(
                        0.2D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MOVEMENT_SPEED), this.getAttributeCoeff(EntityAttributes.MOVEMENT_SPEED)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)).setBaseValue(this.calculateStats(
                        1.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.ATTACK_DAMAGE), this.getAttributeCoeff(EntityAttributes.ATTACK_DAMAGE)));
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).setBaseValue(this.calculateStats(
                        2.0D, this.getGenome(), this.getAttributeGeneIndex(EntityAttributes.MAX_HEALTH), this.getAttributeCoeff(EntityAttributes.MAX_HEALTH)));
            }
        } else {
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

    @Override
    public boolean isMonogamous() {
        return true;
    }

    @Override
    public boolean willParent() {
        return true;
    }

    // --- Enrichment ------------------------------------------------------------------------------------------
    @Override
    public int getMaxEnrichment() {
        return 100;
    }

    @Override
    public TagKey<Block> getAllowedEnrichment() {
        return WildlifeTags.AMERICAN_RED_FOX_ENRICHMENT;
    }

    // --- General ------------------------------------------------------------------------------------------
    @Override
    public int getLimitPerChunk() {
        return 8;
    }

    // --- Genome ------------------------------------------------------------------------------------------
    public String calculateGenome() {
        return geneticsContext.setRandomGenes(false);
    }

    @Override
    public String calculateInheritedGenome(String parent1, String parent2) {
        return geneticsContext.calculateGenes(parent1, parent2);
    }

    @Override
    public String calculateWildGenome() {
        return geneticsContext.setRandomGenes(true);
    }

    @Override
    public boolean isGeneticallyViable(String genome) {
        return !geneticsContext.isHomozygousLethal(genome);
    }

    @Override
    public double calculateStats(double statValue, String genome, int geneIndex, float coefficient) {
        return geneticsContext.calculateStats(statValue, genome, geneIndex, coefficient);
    }

    @Override
    public int getSizeGeneIndex() {
        return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 1;
    }

    @Override
    public float getSizeCoeff() {
        return 0.05f;
    }

    @Override
    public int getAttributeGeneIndex(RegistryEntry<EntityAttribute> attribute) {
        if (attribute.equals(EntityAttributes.MAX_HEALTH)) {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 2;
        } else if (attribute.equals(EntityAttributes.MOVEMENT_SPEED)) {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 3;
        } else if (attribute.equals(EntityAttributes.ATTACK_DAMAGE)) {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 4;
        } else {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 1;
        }
    }

    @Override
    public float getAttributeCoeff(RegistryEntry<EntityAttribute> attribute) {
        if (attribute.equals(EntityAttributes.MAX_HEALTH)) {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 2;
        } else if (attribute.equals(EntityAttributes.MOVEMENT_SPEED)) {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 3;
        } else if (attribute.equals(EntityAttributes.ATTACK_DAMAGE)) {
            return coreTextureContext.geneTextures().indexOf(coreTextureContext.geneTextures().getLast()) + 4;
        } else {
            return 1;
        }
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

    // --- Pack Mechanics ------------------------------------------------------------------------------------------
    @Override
    public int getMaxGroupSize() {
        return 8;
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
                return 12;
            } else if (this.isParent()) {
                return 8;
            }
        } else {
            if (this.isParent()) {
                return 12;
            }
        }
        return 20;
    }

    // --- Sleeping ------------------------------------------------------------------------------------------
    @Override
    public List<String> getSleepSchedules() {
        return List.of("nocturnal", "crepuscular");
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

    @Override
    public float getMinHeight() {
        return 0.5f;
    }

    @Override
    public float getMinWidth() {
        return 0.4f;
    }

    @Override
    public float getMaxHeight() {
        return 1f;
    }

    @Override
    public float getMaxWidth() {
        return 0.9f;
    }

    @Override
    public float getHeightDifference() {
        return 0.2f;
    }

    @Override
    public float getWidthDifference() {
        return 0.1f;
    }

    // --- Sounds ------------------------------------------------------------------------------------------
    @Override
    public void playAmbientSound() {
        if (!this.isSleeping()) {
            super.playAmbientSound();
        }
    }

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
            List<PlayerEntity> list = this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(16.0, 16.0, 16.0), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
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

    private static class AmericanRedFoxLookControl extends LookControl {

        protected final AmericanRedFoxEntity entity;
        protected float maxYawChange;
        protected float maxPitchChange;
        protected int lookAtTimer;
        protected double x;
        protected double y;
        protected double z;

        public AmericanRedFoxLookControl(AmericanRedFoxEntity entity) {
            super(entity);
            this.entity = entity;
        }

        public void lookAt(Vec3d direction) {
            this.lookAt(direction.x, direction.y, direction.z);
        }

        public void lookAt(Entity entity) {
            this.lookAt(entity.getX(), getLookingHeightFor(entity), entity.getZ());
        }

        public void lookAt(Entity entity, float maxYawChange, float maxPitchChange) {
            this.lookAt(entity.getX(), getLookingHeightFor(entity), entity.getZ(), maxYawChange, maxPitchChange);
        }

        public void lookAt(double x, double y, double z) {
            this.lookAt(x, y, z, (float)this.entity.getMaxLookYawChange(), (float)this.entity.getMaxLookPitchChange());
        }

        public void lookAt(double x, double y, double z, float maxYawChange, float maxPitchChange) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.maxYawChange = maxYawChange;
            this.maxPitchChange = maxPitchChange;
            this.lookAtTimer = 2;
        }

        public void tick() {
            if (this.shouldStayHorizontal()) {
                this.entity.setPitch(0.0F);
            }

            if (this.lookAtTimer > 0) {
                --this.lookAtTimer;
                this.getTargetYaw().ifPresent((yaw) -> {
                    this.entity.headYaw = this.changeAngle(this.entity.headYaw, yaw, this.maxYawChange);
                });
                this.getTargetPitch().ifPresent((pitch) -> {
                    this.entity.setPitch(this.changeAngle(this.entity.getPitch(), pitch, this.maxPitchChange));
                });
            } else {
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, 10.0F);
            }

            this.clampHeadYaw();
        }

        protected void clampHeadYaw() {
            if (!this.entity.getNavigation().isIdle()) {
                this.entity.headYaw = MathHelper.clampAngle(this.entity.headYaw, this.entity.bodyYaw, (float)this.entity.getMaxHeadRotation());
            }

        }

        protected boolean shouldStayHorizontal() {
            return true;
        }

        public boolean isLookingAtSpecificPosition() {
            return this.lookAtTimer > 0;
        }

        public double getLookX() {
            return this.x;
        }

        public double getLookY() {
            return this.y;
        }

        public double getLookZ() {
            return this.z;
        }

        protected Optional<Float> getTargetPitch() {
            double d = this.x - this.entity.getX();
            double e = this.y - this.entity.getEyeY();
            double f = this.z - this.entity.getZ();
            double g = Math.sqrt(d * d + f * f);
            return !(Math.abs(e) > 9.999999747378752E-6) && !(Math.abs(g) > 9.999999747378752E-6) ? Optional.empty() : Optional.of((float)(-(MathHelper.atan2(e, g) * 57.2957763671875)));
        }

        protected Optional<Float> getTargetYaw() {
            double d = this.x - this.entity.getX();
            double e = this.z - this.entity.getZ();
            return !(Math.abs(e) > 9.999999747378752E-6) && !(Math.abs(d) > 9.999999747378752E-6) ? Optional.empty() : Optional.of((float)(MathHelper.atan2(e, d) * 57.2957763671875) - 90.0F);
        }

        private static double getLookingHeightFor(Entity entity) {
            return entity instanceof LivingEntity ? entity.getEyeY() : (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0;
        }
    }

}
