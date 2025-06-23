package com.collective.projectwildlife.entities;

import com.collective.projectcore.entities.CoreAnimalEntity;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;

public class TestCoreAnimalWanderFarGoal extends WanderAroundFarGoal {

    CoreAnimalEntity animal;

    public TestCoreAnimalWanderFarGoal(CoreAnimalEntity animal, double d) {
        super(animal, d);
        this.animal = animal;
    }

    @Override
    public boolean canStart() {
        if (this.animal.isSleeping() || this.animal.isResting()) {
            return false;
        }
        return super.canStart();

    }

    @Override
    public boolean shouldContinue() {
        if (this.animal.isSleeping() || this.animal.isResting()) {
            return false;
        }
        return super.shouldContinue();
    }
}
