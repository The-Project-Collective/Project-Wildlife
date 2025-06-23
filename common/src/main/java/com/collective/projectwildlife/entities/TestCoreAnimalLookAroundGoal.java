package com.collective.projectwildlife.entities;

import com.collective.projectcore.entities.CoreAnimalEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;

public class TestCoreAnimalLookAroundGoal extends LookAroundGoal {

    CoreAnimalEntity animal;

    public TestCoreAnimalLookAroundGoal(CoreAnimalEntity mob) {
        super(mob);
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
