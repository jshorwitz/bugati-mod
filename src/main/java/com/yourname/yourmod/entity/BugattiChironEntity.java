package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.CarSoundSystem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

/**
 * Bugatti Chiron - The flagship W16 luxury hypercar
 * Deep, powerful engine sound with sophisticated refinement
 */
public class BugattiChironEntity extends BugattiCarEntity {
    
    // Chiron specs - Ultimate luxury and performance
    private static final int CHIRON_MAX_FUEL = 1500;
    private static final double CHIRON_MAX_SPEED = 2.2;
    private static final double CHIRON_ACCELERATION = 0.12;
    private static final double CHIRON_DECELERATION = 0.96;
    private static final double CHIRON_BRAKING_FORCE = 0.88;
    private static final double CHIRON_TURN_SPEED = 2.2F;
    
    public BugattiChironEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.2F; // Slightly better step height
    }
    
    @Override
    protected int getMaxFuel() {
        return CHIRON_MAX_FUEL;
    }
    
    @Override
    protected double getMaxSpeed() {
        return CHIRON_MAX_SPEED;
    }
    
    @Override
    protected double getAcceleration() {
        return CHIRON_ACCELERATION;
    }
    
    @Override
    protected double getDeceleration() {
        return CHIRON_DECELERATION;
    }
    
    @Override
    protected double getBrakingForce() {
        return CHIRON_BRAKING_FORCE;
    }
    
    @Override
    protected double getTurnSpeed() {
        return CHIRON_TURN_SPEED;
    }
    
    @Override
    protected CarSoundSystem.CarModel getCarModel() {
        return CarSoundSystem.CarModel.CHIRON;
    }
    
    @Override
    protected int getConsumptionRate() {
        return 25; // Every 1.25 seconds - more efficient than base
    }
}
