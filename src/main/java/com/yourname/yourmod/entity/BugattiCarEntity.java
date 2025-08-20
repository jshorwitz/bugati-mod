package com.yourname.yourmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.ModSounds;
import com.yourname.yourmod.sound.CarSoundSystem;

public class BugattiCarEntity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiCarEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiCarEntity.class, EntityDataSerializers.BOOLEAN);
    
    // Vehicle specifications - can be overridden by subclasses
    private static final int BASE_MAX_FUEL = 1000;
    private static final double BASE_MAX_SPEED = 2.0;
    private static final double BASE_ACCELERATION = 0.15; // Increased from 0.08 for more responsive acceleration
    private static final double BASE_DECELERATION = 0.92; // Slightly faster deceleration from 0.95
    private static final double BASE_BRAKING_FORCE = 0.80; // Slightly more aggressive braking from 0.85
    private static final double BASE_TURN_SPEED = 3.0F; // Increased from 2.5 for more responsive steering
    private static final double BASE_MIN_TURN_SPEED = 0.5; // Increased from 0.3
    
    // Physics and movement variables
    private double currentSpeed = 0;
    private double targetSpeed = 0;
    private double currentYaw = 0;
    private double targetYaw = 0;
    private boolean onSlope = false;
    private double momentum = 0;
    
    // Enhanced sound system
    private CarSoundSystem soundSystem = new CarSoundSystem();
    private boolean lastTurningState = false;
    private boolean lastBrakingState = false;
    
    // Input detection improvement with separate counters
    private boolean wasMovingForward = false;
    private boolean wasMovingBackward = false;
    private boolean wasTurningLeft = false;
    private boolean wasTurningRight = false;
    private int forwardStabilityCounter = 0;
    private int backwardStabilityCounter = 0;
    private int leftStabilityCounter = 0;
    private int rightStabilityCounter = 0;
    
    // Debug logging
    private int debugLogCounter = 0;
    
    // Test mode for immediate input response (disable all stability checks)
    private static final boolean IMMEDIATE_INPUT_MODE = true;
    
    public BugattiCarEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0F;
        // DEBUG: Entity created successfully
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FLYING_SPEED, 0.0D);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FUEL_LEVEL, getMaxFuel());
        this.entityData.define(ENGINE_RUNNING, false);
    }
    
    // Virtual methods that can be overridden by subclasses
    protected int getMaxFuel() {
        return BASE_MAX_FUEL;
    }
    
    protected double getMaxSpeed() {
        return BASE_MAX_SPEED;
    }
    
    protected double getAcceleration() {
        return BASE_ACCELERATION;
    }
    
    protected double getDeceleration() {
        return BASE_DECELERATION;
    }
    
    protected double getBrakingForce() {
        return BASE_BRAKING_FORCE;
    }
    
    protected double getTurnSpeed() {
        return BASE_TURN_SPEED;
    }
    
    protected double getMinTurnSpeed() {
        return BASE_MIN_TURN_SPEED;
    }
    
    // Getter for current speed (for subclasses)
    protected double getCurrentSpeed() {
        return currentSpeed;
    }
    
    // Get the car model for sound system (to be overridden by subclasses)
    protected CarSoundSystem.CarModel getCarModel() {
        return CarSoundSystem.CarModel.VEYRON; // Default model
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Test horn sound when shift-right clicking
        if (player.isShiftKeyDown() && this.isVehicle()) {
            System.out.println("[DEBUG] Playing horn sound test");
            soundSystem.playHorn(this, getCarModel());
            // Also play a basic sound for testing
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                SoundEvents.NOTE_BLOCK_BELL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        ItemStack itemStack = player.getItemInHand(hand);
        
        // Refuel with coal
        if (itemStack.is(Items.COAL) || itemStack.is(Items.BLAZE_POWDER)) {
            if (getFuelLevel() < getMaxFuel()) {
                int fuelToAdd = itemStack.is(Items.BLAZE_POWDER) ? 200 : 100;
                setFuelLevel(Math.min(getMaxFuel(), getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 0.8F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                
                System.out.println("[DEBUG] BugattiCarEntity - Starting engine, playing startup sound");
                
                // Test basic Minecraft sound first
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.ANVIL_HIT, SoundSource.NEUTRAL, 1.0F, 1.0F);
                
                // Use enhanced sound system for startup
                soundSystem.playEngineStartup(this, getCarModel());
            }
            return InteractionResult.SUCCESS;
        }
        
        return super.mobInteract(player, hand);
    }
    
    @Override
    public void tick() {
        // Custom tick to avoid gravity issues with Animal class
        this.baseTick();
        
        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            // Increment debug counter
            debugLogCounter++;
            
            handleMovement(player);
            updatePhysics();
            
            // Consume fuel (unless in creative mode)
            boolean isCreativeMode = player.isCreative();
            if (Math.abs(currentSpeed) > 0.05 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % getConsumptionRate() == 0) {
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
            
            // Stop engine if no fuel (unless creative mode)
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                targetSpeed = 0;
                currentSpeed *= 0.9; // Slow down
            }
            
            // Enhanced engine sounds using new sound system
            handleEnhancedEngineSounds();
            
            // Exhaust particles with improved effects
            handleExhaustEffects();
        } else {
            // Engine off when not being driven
            if (isEngineRunning()) {
                setEngineRunning(false);
                soundSystem.playEngineShutdown(this);
            }
            targetSpeed = 0;
            currentSpeed *= getDeceleration(); // Coast to a stop
        }
        
        // Apply improved physics and movement
        applyMovement();
        handleTerrainCollision();
    }
    
    protected int getConsumptionRate() {
        return 20; // Every second - can be overridden by subclasses
    }
    
    private void updatePhysics() {
        // More responsive speed interpolation
        if (Math.abs(targetSpeed - currentSpeed) > 0.01) {
            double speedDiff = targetSpeed - currentSpeed;
            // Ultra-responsive in immediate mode, normal responsiveness otherwise
            double responsiveness = IMMEDIATE_INPUT_MODE ? 0.8 : 0.5;
            currentSpeed += speedDiff * responsiveness;
        }
        
        // Update momentum for physics
        momentum = currentSpeed * 0.8 + momentum * 0.2;
        
        // Check if on slope
        BlockPos belowPos = this.blockPosition().below();
        BlockState belowBlock = this.level.getBlockState(belowPos);
        onSlope = !belowBlock.isAir() && !belowBlock.getCollisionShape(this.level, belowPos).isEmpty();
        
        // Debug physics logging
        if (debugLogCounter % 20 == 0 && !this.level.isClientSide) {
            System.out.println("[DEBUG] Physics - Current Speed: " + String.format("%.3f", currentSpeed) + 
                             ", Target Speed: " + String.format("%.3f", targetSpeed) +
                             ", On Slope: " + onSlope);
        }
    }
    
    private void handleEnhancedEngineSounds() {
        if (isEngineRunning()) {
            // Debug logging for sound system calls
            if (debugLogCounter % 100 == 0 && !this.level.isClientSide) {
                System.out.println("[DEBUG] BugattiCarEntity - Engine running, calling sound system. Speed: " + currentSpeed + ", Model: " + getCarModel());
            }
            
            // Simple fallback sound system - play basic engine sounds every 20 ticks
            if (this.tickCount % 30 == 0) {
                playSimpleEngineSound();
            }
            
            // Use the enhanced sound system
            soundSystem.playEngineSound(this, currentSpeed, getMaxSpeed(), getCarModel());
        } else {
            // Debug: Log when engine is not running
            if (debugLogCounter % 100 == 0 && !this.level.isClientSide) {
                System.out.println("[DEBUG] BugattiCarEntity - Engine not running, skipping sounds");
            }
        }
    }
    
    private void playSimpleEngineSound() {
        try {
            double speedRatio = Math.abs(currentSpeed) / getMaxSpeed();
            SoundEvent engineSound;
            float volume = 0.5f;
            float pitch = 0.8f;
            
            if (speedRatio < 0.1) {
                engineSound = ModSounds.ENGINE_IDLE_MID.get();
                volume = 0.3f;
                pitch = 0.6f;
            } else if (speedRatio < 0.4) {
                engineSound = ModSounds.ENGINE_LOW_RPM.get();
                volume = 0.4f;
                pitch = 0.7f;
            } else if (speedRatio < 0.7) {
                engineSound = ModSounds.ENGINE_MID_RPM.get();
                volume = 0.6f;
                pitch = 0.9f;
            } else {
                engineSound = ModSounds.ENGINE_HIGH_RPM.get();
                volume = 0.8f;
                pitch = 1.2f;
            }
            
            if (engineSound != null) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(),
                        engineSound, SoundSource.NEUTRAL, volume, pitch);
                System.out.println("[DEBUG] Playing simple engine sound: " + engineSound + " (speed ratio: " + speedRatio + ")");
            } else {
                System.out.println("[ERROR] Simple engine sound is null!");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to play simple engine sound: " + e.getMessage());
        }
    }
    
    protected void handleExhaustEffects() {
        if (isEngineRunning() && Math.abs(currentSpeed) > 0.05) {
            Vec3 exhaustPos = this.position().add(
                -Math.sin(Math.toRadians(this.getYRot())) * 1.3, 
                0.15, 
                Math.cos(Math.toRadians(this.getYRot())) * 1.3
            );
            
            if (this.level instanceof ServerLevel serverLevel) {
                int particles = Math.abs(currentSpeed) > 1.0 ? 3 : 1;
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, 
                    exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                    particles, 0.05, 0.05, 0.05, 0.01);
                
                // Extra effects at high speed
                if (Math.abs(currentSpeed) > 1.5) {
                    serverLevel.sendParticles(ParticleTypes.CLOUD, 
                        exhaustPos.x, exhaustPos.y - 0.1, exhaustPos.z, 
                        1, 0.1, 0.1, 0.1, 0.02);
                }
            }
        }
    }
    
    private void applyMovement() {
        if (Math.abs(currentSpeed) > 0.005) {
            Vec3 movement = Vec3.directionFromRotation(0, this.getYRot()).scale(currentSpeed);
            
            // Apply slope resistance
            if (onSlope && currentSpeed > 0) {
                movement = movement.multiply(0.85, 1.0, 0.85);
            }
            
            this.setDeltaMovement(movement.x, this.getDeltaMovement().y, movement.z);
        }
        
        // Improved gravity handling
        Vec3 currentMovement = this.getDeltaMovement();
        if (!this.onGround && !this.isInWater()) {
            // Apply gravity with car weight consideration
            double gravityForce = -0.08 - (Math.abs(currentSpeed) * 0.02);
            this.setDeltaMovement(currentMovement.add(0, gravityForce, 0));
        } else if (this.onGround) {
            // Ground friction and stability
            if (currentMovement.y < 0) {
                this.setDeltaMovement(currentMovement.x, Math.max(currentMovement.y * 0.5, -0.1), currentMovement.z);
            }
        }
        
        // Apply the movement with collision detection
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
        
        // Enhanced drag/friction
        double friction = this.onGround ? 0.96 : 0.99;
        this.setDeltaMovement(this.getDeltaMovement().multiply(friction, 0.98, friction));
    }
    
    private void handleTerrainCollision() {
        // Check for walls and obstacles
        if (this.horizontalCollision && Math.abs(currentSpeed) > 0.1) {
            // Bounce back from collision
            currentSpeed *= -0.3;
            targetSpeed = 0;
            
            // Play collision sound
            if (Math.abs(currentSpeed) > 0.5) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 0.5F, 1.2F);
            }
        }
    }
    
    private void handleMovement(Player player) {
        // Skip fuel check in creative mode
        if (getFuelLevel() <= 0 && !player.isCreative()) return;
        
        // Try multiple input detection methods for maximum compatibility
        boolean isAccelerating = detectForwardInput(player) || detectAlternativeForwardInput(player);
        boolean isBraking = detectBackwardInput(player) || detectAlternativeBackwardInput(player);  
        boolean isTurningLeft = detectLeftInput(player) || detectAlternativeLeftInput(player);
        boolean isTurningRight = detectRightInput(player) || detectAlternativeRightInput(player);
        
        // Debug logging for overall input state
        if (debugLogCounter % 20 == 0 && !this.level.isClientSide) {
            System.out.println("[DEBUG] Overall Input State - Forward: " + isAccelerating + 
                             ", Backward: " + isBraking + ", Left: " + isTurningLeft + 
                             ", Right: " + isTurningRight);
            System.out.println("[DEBUG] Current Speed: " + currentSpeed + ", Target Speed: " + targetSpeed);
            System.out.println("[DEBUG] Engine Running: " + isEngineRunning() + ", Fuel Level: " + getFuelLevel());
        }
        
        // Handle acceleration and deceleration with smooth curves
        handleSpeedControl(isAccelerating, isBraking);
        
        // Handle steering with speed-dependent sensitivity
        handleSteering(isTurningLeft, isTurningRight);
    }
    
    private boolean detectForwardInput(Player player) {
        // Simplified and more responsive input detection
        boolean rawInput = player.zza > 0.05F;
        
        // Immediate mode bypasses all stability checking
        if (IMMEDIATE_INPUT_MODE) {
            wasMovingForward = rawInput;
        } else {
            // Basic stability check with separate counter
            if (rawInput != wasMovingForward) {
                forwardStabilityCounter = 0;
            } else {
                forwardStabilityCounter++;
            }
            
            // Much more responsive - only need 1 tick of stability
            if (forwardStabilityCounter >= 1) {
                wasMovingForward = rawInput;
            }
        }
        
        // Debug logging every 20 ticks (once per second)
        if (debugLogCounter % 20 == 0 && !this.level.isClientSide) {
            System.out.println("[DEBUG] Forward Input - Raw: " + String.format("%.3f", player.zza) + 
                             ", Detected: " + wasMovingForward + ", Immediate Mode: " + IMMEDIATE_INPUT_MODE);
        }
        
        return wasMovingForward;
    }
    
    private boolean detectBackwardInput(Player player) {
        boolean rawInput = player.zza < -0.05F;
        
        if (IMMEDIATE_INPUT_MODE) {
            wasMovingBackward = rawInput;
        } else {
            if (rawInput != wasMovingBackward) {
                backwardStabilityCounter = 0;
            } else {
                backwardStabilityCounter++;
            }
            
            if (backwardStabilityCounter >= 1) {
                wasMovingBackward = rawInput;
            }
        }
        
        if (debugLogCounter % 20 == 0 && !this.level.isClientSide) {
            System.out.println("[DEBUG] Backward Input - Raw: " + String.format("%.3f", player.zza) + 
                             ", Detected: " + wasMovingBackward);
        }
        
        return wasMovingBackward;
    }
    
    private boolean detectLeftInput(Player player) {
        boolean rawInput = player.xxa < -0.05F;
        
        if (IMMEDIATE_INPUT_MODE) {
            wasTurningLeft = rawInput;
        } else {
            if (rawInput != wasTurningLeft) {
                leftStabilityCounter = 0;
            } else {
                leftStabilityCounter++;
            }
            
            if (leftStabilityCounter >= 1) {
                wasTurningLeft = rawInput;
            }
        }
        
        if (debugLogCounter % 20 == 0 && !this.level.isClientSide) {
            System.out.println("[DEBUG] Left Input - Raw: " + String.format("%.3f", player.xxa) + 
                             ", Detected: " + wasTurningLeft);
        }
        
        return wasTurningLeft;
    }
    
    private boolean detectRightInput(Player player) {
        boolean rawInput = player.xxa > 0.05F;
        
        if (IMMEDIATE_INPUT_MODE) {
            wasTurningRight = rawInput;
        } else {
            if (rawInput != wasTurningRight) {
                rightStabilityCounter = 0;
            } else {
                rightStabilityCounter++;
            }
            
            if (rightStabilityCounter >= 1) {
                wasTurningRight = rawInput;
            }
        }
        
        if (debugLogCounter % 20 == 0 && !this.level.isClientSide) {
            System.out.println("[DEBUG] Right Input - Raw: " + String.format("%.3f", player.xxa) + 
                             ", Detected: " + wasTurningRight);
        }
        
        return wasTurningRight;
    }
    
    // Alternative input detection methods for maximum compatibility
    private boolean detectAlternativeForwardInput(Player player) {
        // Direct approach - any positive movement counts
        return player.zza > 0.001F;
    }
    
    private boolean detectAlternativeBackwardInput(Player player) {
        return player.zza < -0.001F;
    }
    
    private boolean detectAlternativeLeftInput(Player player) {
        return player.xxa < -0.001F;
    }
    
    private boolean detectAlternativeRightInput(Player player) {
        return player.xxa > 0.001F;
    }
    
    private void handleSpeedControl(boolean isAccelerating, boolean isBraking) {
        if (isAccelerating) {
            // Smooth acceleration curve
            double speedRatio = Math.abs(targetSpeed) / getMaxSpeed();
            double adjustedAccel = getAcceleration() * (1.0 - speedRatio * 0.3); // Less acceleration at high speeds
            targetSpeed = Math.min(getMaxSpeed(), targetSpeed + adjustedAccel);
        } else if (isBraking) {
            // Play brake sounds
            boolean heavyBraking = Math.abs(currentSpeed) > 1.5;
            if (!lastBrakingState || this.tickCount % 20 == 0) {
                System.out.println("[DEBUG] Playing brake sound - Speed: " + Math.abs(currentSpeed) + ", Heavy: " + heavyBraking);
                soundSystem.playBrakeSound(this, Math.abs(currentSpeed), heavyBraking);
                if (heavyBraking) {
                    soundSystem.playTireScreech(this, Math.abs(currentSpeed), false, true);
                }
            }
            lastBrakingState = true;
            
            // Effective braking with reverse capability
            if (currentSpeed > 0) {
                targetSpeed = Math.max(targetSpeed - getAcceleration() * 2, -getMaxSpeed() * 0.7);
            } else {
                targetSpeed = Math.max(-getMaxSpeed() * 0.7, targetSpeed - getAcceleration());
            }
        } else {
            lastBrakingState = false;
            // Natural deceleration when no input
            if (Math.abs(targetSpeed) > 0.05) {
                targetSpeed *= getDeceleration();
            } else {
                targetSpeed = 0;
            }
        }
    }
    
    private void handleSteering(boolean isTurningLeft, boolean isTurningRight) {
        if (Math.abs(currentSpeed) > 0.05) {
            boolean currentlyTurning = isTurningLeft || isTurningRight;
            
            // Play tire screech sounds when turning at high speed
            if (currentlyTurning && Math.abs(currentSpeed) > 1.0) {
                if (!lastTurningState || this.tickCount % 30 == 0) {
                    soundSystem.playTireScreech(this, Math.abs(currentSpeed), true, false);
                }
            }
            lastTurningState = currentlyTurning;
            
            // Speed-dependent turning - slower turning at low speeds, more responsive at high speeds
            double speedRatio = Math.abs(currentSpeed) / getMaxSpeed();
            double baseTurnSpeed = Math.max(getMinTurnSpeed(), getTurnSpeed() * speedRatio);
            
            // Additional handling improvements for different speeds
            double turnMultiplier = 1.0;
            if (speedRatio < 0.3) {
                turnMultiplier = 0.6; // Reduce turning at very low speeds
            } else if (speedRatio > 0.8) {
                turnMultiplier = 1.3; // Increase responsiveness at high speeds
            }
            
            double finalTurnSpeed = baseTurnSpeed * turnMultiplier;
            
            if (isTurningRight) {
                this.setYRot((float)(this.getYRot() + finalTurnSpeed));
            } else if (isTurningLeft) {
                this.setYRot((float)(this.getYRot() - finalTurnSpeed));
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        
        // Drop the car item when destroyed
        if (!this.level.isClientSide && this.getHealth() - amount <= 0) {
            this.spawnAtLocation(ModItems.BUGATTI_CAR.get());
        }
        
        return super.hurt(damageSource, amount);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return null; // No ambient sounds
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ANVIL_HIT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_EXPLODE;
    }
    
    public boolean canBeRiddenInWater(Entity rider) {
        return false; // Cars don't work underwater
    }
    
    // Fuel methods
    public int getFuelLevel() {
        return this.entityData.get(FUEL_LEVEL);
    }
    
    public void setFuelLevel(int fuel) {
        this.entityData.set(FUEL_LEVEL, fuel);
    }
    
    public boolean isEngineRunning() {
        return this.entityData.get(ENGINE_RUNNING);
    }
    
    public void setEngineRunning(boolean running) {
        this.entityData.set(ENGINE_RUNNING, running);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FuelLevel", getFuelLevel());
        compound.putBoolean("EngineRunning", isEngineRunning());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setFuelLevel(compound.getInt("FuelLevel"));
        setEngineRunning(compound.getBoolean("EngineRunning"));
    }
    
    // Required for Animal class
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob animal) {
        return null; // Cars don't breed
    }
    
    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }
}
