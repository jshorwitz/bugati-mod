package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.CarSoundSystem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

/**
 * Bugatti Centodieci - Ultra-luxury limited edition
 * Refined, sophisticated engine sound with luxury characteristics
 */
public class BugattiCentodieciEntity extends BugattiCarEntity {
    
    // Centodieci specs - Ultimate luxury with refined power
    private static final int CENTODIECI_MAX_FUEL = 1400;
    private static final double CENTODIECI_MAX_SPEED = 2.0;
    private static final double CENTODIECI_ACCELERATION = 0.10;
    private static final double CENTODIECI_DECELERATION = 0.97;
    private static final double CENTODIECI_BRAKING_FORCE = 0.90;
    private static final double CENTODIECI_TURN_SPEED = 2.0F;
    
    public BugattiCentodieciEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.1F; // Luxury comfort
    }
    
    @Override
    protected int getMaxFuel() {
        return CENTODIECI_MAX_FUEL;
    }
    
    @Override
    protected double getMaxSpeed() {
        return CENTODIECI_MAX_SPEED;
    }
    
    @Override
    protected double getAcceleration() {
        return CENTODIECI_ACCELERATION;
    }
    
    @Override
    protected double getDeceleration() {
        return CENTODIECI_DECELERATION;
    }
    
    @Override
    protected double getBrakingForce() {
        return CENTODIECI_BRAKING_FORCE;
    }
    
    @Override
    protected double getTurnSpeed() {
        return CENTODIECI_TURN_SPEED;
    }
    
    @Override
    protected CarSoundSystem.CarModel getCarModel() {
        return CarSoundSystem.CarModel.CENTODIECI;
    }
    
    @Override
    protected int getConsumptionRate() {
        return 28; // Every 1.4 seconds - refined efficiency
    }
}
