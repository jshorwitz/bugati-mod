package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.ModSounds;
import com.yourname.yourmod.sound.CarSoundSystem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Bugatti Bolide - Pure racing track beast
 * Raw, aggressive engine sound with extreme turbo and afterburner effects
 */
public class BugattiBolideEntity extends BugattiCarEntity {
    
    // Bolide specs - absolute fastest, track-only monster
    private static final int BOLIDE_MAX_FUEL = 700; // Racing fuel tank
    private static final double BOLIDE_MAX_SPEED = 2.5; // FASTEST CAR
    private static final double BOLIDE_ACCELERATION = 0.18; // Insane acceleration
    private static final double BOLIDE_DECELERATION = 0.92;
    private static final double BOLIDE_BRAKING_FORCE = 0.78;
    private static final double BOLIDE_TURN_SPEED = 3.5F;
    
    private int boostCooldown = 0;
    
    public BugattiBolideEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.5F; // Can jump over blocks
    }
    
    @Override
    protected int getMaxFuel() {
        return BOLIDE_MAX_FUEL;
    }
    
    @Override
    protected double getMaxSpeed() {
        return BOLIDE_MAX_SPEED;
    }
    
    @Override
    protected double getAcceleration() {
        return BOLIDE_ACCELERATION;
    }
    
    @Override
    protected double getDeceleration() {
        return BOLIDE_DECELERATION;
    }
    
    @Override
    protected double getBrakingForce() {
        return BOLIDE_BRAKING_FORCE;
    }
    
    @Override
    protected double getTurnSpeed() {
        return BOLIDE_TURN_SPEED;
    }
    
    @Override
    protected CarSoundSystem.CarModel getCarModel() {
        return CarSoundSystem.CarModel.BOLIDE;
    }
    
    @Override
    protected int getConsumptionRate() {
        return 10; // Every 0.5 seconds - extreme fuel consumption
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // Track car - only accepts racing fuel (blaze powder) or rocket fuel (fire charges)
        if (itemStack.is(Items.BLAZE_POWDER) || itemStack.is(Items.FIRE_CHARGE) || itemStack.is(Items.GUNPOWDER)) {
            if (getFuelLevel() < getMaxFuel()) {
                int fuelToAdd;
                if (itemStack.is(Items.FIRE_CHARGE)) {
                    fuelToAdd = 200; // Rocket fuel!
                } else if (itemStack.is(Items.BLAZE_POWDER)) {
                    fuelToAdd = 150;
                } else {
                    fuelToAdd = 100; // Gunpowder
                }
                
                setFuelLevel(Math.min(getMaxFuel(), getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                
                // Racing refuel sounds
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.8F, 1.8F);
                
                // Explosive refuel effects
                if (this.level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 8; i++) {
                        serverLevel.sendParticles(ParticleTypes.FLAME,
                            this.getX() + (this.random.nextDouble() - 0.5) * 2,
                            this.getY() + 0.5 + this.random.nextDouble(),
                            this.getZ() + (this.random.nextDouble() - 0.5) * 2,
                            1, 0, 0.2, 0, 0.05);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        } else if (itemStack.is(Items.COAL) || itemStack.is(Items.DIAMOND)) {
            // Track car doesn't accept regular fuel
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                "§4This track monster needs racing fuel! (Blaze Powder, Fire Charges, or Gunpowder)"), true);
            return InteractionResult.FAIL;
        }
        
        // Use base class for mounting
        return super.mobInteract(player, hand);
    }
    
    @Override
    public void tick() {
        super.tick(); // Use enhanced base tick
        
        // Bolide-specific boost system
        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            handleBoostSystem(player);
            
            // Extra fuel consumption at high speeds
            if (getCurrentSpeed() > 2.0 && getFuelLevel() > 0 && !player.isCreative()) {
                if (this.tickCount % 5 == 0) {
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
        }
        
        if (boostCooldown > 0) boostCooldown--;
    }
    
    private void handleBoostSystem(Player player) {
        // Boost mode when shift is held (if available)
        if (player.isShiftKeyDown() && boostCooldown == 0 && getCurrentSpeed() > 1.5) {
            // Apply boost (this would modify the base class's speed)
            boostCooldown = 60; // 3-second cooldown
            
            // Boost effects
            if (this.level instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 10; i++) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                        this.getX() + (this.random.nextDouble() - 0.5) * 2,
                        this.getY() + this.random.nextDouble(),
                        this.getZ() + (this.random.nextDouble() - 0.5) * 2,
                        1, 0, 0, 0, 0.1);
                }
            }
            
            // Special boost sound
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                ModSounds.BOLIDE_AFTERBURNER.get(), SoundSource.NEUTRAL, 0.8f, 2.0F);
        }
    }
    
    @Override
    protected void handleExhaustEffects() {
        super.handleExhaustEffects(); // Use base exhaust effects
        
        // Additional Bolide-specific exhaust effects
        if (isEngineRunning() && getCurrentSpeed() > 0.05) {
            Vec3 exhaustPos = this.position().add(
                -Math.sin(Math.toRadians(this.getYRot())) * 1.8, 
                0.1, 
                Math.cos(Math.toRadians(this.getYRot())) * 1.8
            );
            
            if (this.level instanceof ServerLevel serverLevel) {
                // Multiple exhaust pipes with flames
                serverLevel.sendParticles(ParticleTypes.FLAME,
                    exhaustPos.x + 0.3, exhaustPos.y, exhaustPos.z, 
                    2, 0.1, 0.1, 0.1, 0.05);
                serverLevel.sendParticles(ParticleTypes.FLAME,
                    exhaustPos.x - 0.3, exhaustPos.y, exhaustPos.z, 
                    2, 0.1, 0.1, 0.1, 0.05);
                
                // Afterburner at high speed
                if (getCurrentSpeed() > 2.0) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                        3, 0.2, 0.1, 0.2, 0.1);
                }
            }
        }
    }
}
