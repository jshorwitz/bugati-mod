package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.CarSoundSystem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

/**
 * Bugatti Type 35 - Classic vintage racing car
 * Distinctive vintage engine sound with occasional backfires
 */
public class BugattiType35Entity extends BugattiCarEntity {
    
    // Type 35 specs - vintage, nimble, small, efficient
    private static final int TYPE35_MAX_FUEL = 600; // Smaller fuel tank
    private static final double TYPE35_MAX_SPEED = 1.4; // Moderate speed but nimble
    private static final double TYPE35_ACCELERATION = 0.15; // Very quick acceleration
    private static final double TYPE35_DECELERATION = 0.93;
    private static final double TYPE35_BRAKING_FORCE = 0.80;
    private static final double TYPE35_TURN_SPEED = 2.8F;
    
    public BugattiType35Entity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 0.8F; // Lower profile, less step height
    }
    
    @Override
    protected int getMaxFuel() {
        return TYPE35_MAX_FUEL;
    }
    
    @Override
    protected double getMaxSpeed() {
        return TYPE35_MAX_SPEED;
    }
    
    @Override
    protected double getAcceleration() {
        return TYPE35_ACCELERATION;
    }
    
    @Override
    protected double getDeceleration() {
        return TYPE35_DECELERATION;
    }
    
    @Override
    protected double getBrakingForce() {
        return TYPE35_BRAKING_FORCE;
    }
    
    @Override
    protected double getTurnSpeed() {
        return TYPE35_TURN_SPEED;
    }
    
    @Override
    protected CarSoundSystem.CarModel getCarModel() {
        return CarSoundSystem.CarModel.TYPE35;
    }
    
    @Override
    protected int getConsumptionRate() {
        return 30; // Every 1.5 seconds - very efficient vintage engine
    }
}
