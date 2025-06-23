package com.collective.projectwildlife.entities;

import com.collective.projectcore.entities.CoreAnimalEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;

public class TestCoreAnimalLookAtEntityGoal extends LookAtEntityGoal {

    CoreAnimalEntity animal;

    public TestCoreAnimalLookAtEntityGoal(CoreAnimalEntity mob, Class<? extends LivingEntity> targetType, float range) {
        super(mob, targetType, range);
        this.animal = mob;
    }

    @Override
    public boolean canStart() {
        if (this.animal.isSleeping()) {
            return false;
        }
        return super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        if (this.animal.isSleeping()) {
            return false;
        }
        return super.shouldContinue();
    }
}
