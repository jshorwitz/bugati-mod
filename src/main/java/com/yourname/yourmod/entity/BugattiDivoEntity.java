package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.CarSoundSystem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

/**
 * Bugatti Divo - Track-focused aggressive machine
 * Sharp, aggressive engine sound with racing characteristics
 */
public class BugattiDivoEntity extends BugattiCarEntity {
    
    // Divo specs - Track-focused performance
    private static final int DIVO_MAX_FUEL = 1000;
    private static final double DIVO_MAX_SPEED = 2.1;
    private static final double DIVO_ACCELERATION = 0.14;
    private static final double DIVO_DECELERATION = 0.94;
    private static final double DIVO_BRAKING_FORCE = 0.82;
    private static final double DIVO_TURN_SPEED = 3.2F; // Best handling
    
    public BugattiDivoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.3F; // Track car agility
    }
    
    @Override
    protected int getMaxFuel() {
        return DIVO_MAX_FUEL;
    }
    
    @Override
    protected double getMaxSpeed() {
        return DIVO_MAX_SPEED;
    }
    
    @Override
    protected double getAcceleration() {
        return DIVO_ACCELERATION;
    }
    
    @Override
    protected double getDeceleration() {
        return DIVO_DECELERATION;
    }
    
    @Override
    protected double getBrakingForce() {
        return DIVO_BRAKING_FORCE;
    }
    
    @Override
    protected double getTurnSpeed() {
        return DIVO_TURN_SPEED;
    }
    
    @Override
    protected CarSoundSystem.CarModel getCarModel() {
        return CarSoundSystem.CarModel.DIVO;
    }
    
    @Override
    protected int getConsumptionRate() {
        return 15; // Every 0.75 seconds - aggressive fuel consumption
    }
}
