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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import com.yourname.yourmod.item.ModItems;

public class BugattiCarEntity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiCarEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiCarEntity.class, EntityDataSerializers.BOOLEAN);
    
    private static final int MAX_FUEL = 1000;
    private static final double MAX_SPEED = 2.0;
    private static final double ACCELERATION = 0.1;
    
    private double currentSpeed = 0;
    private int engineSoundTimer = 0;
    
    public BugattiCarEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0F;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FUEL_LEVEL, MAX_FUEL);
        this.entityData.define(ENGINE_RUNNING, false);
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // Refuel with coal
        if (itemStack.is(Items.COAL) || itemStack.is(Items.BLAZE_POWDER)) {
            if (getFuelLevel() < MAX_FUEL) {
                int fuelToAdd = itemStack.is(Items.BLAZE_POWDER) ? 200 : 100;
                setFuelLevel(Math.min(MAX_FUEL, getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 1.0F, 0.8F);
            }
            return InteractionResult.SUCCESS;
        }
        
        return super.mobInteract(player, hand);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            handleMovement(player);
            
            // Consume fuel (unless in creative mode)
            boolean isCreativeMode = player.isCreative();
            if (currentSpeed > 0 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % 20 == 0) { // Every second
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
            
            // Stop engine if no fuel (unless creative mode)
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                currentSpeed *= 0.9; // Slow down
            }
            
            // Engine sounds
            engineSoundTimer++;
            if (engineSoundTimer > 40 && isEngineRunning()) { // Every 2 seconds
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 
                    0.5F, 0.5F + (float)(currentSpeed / MAX_SPEED));
                engineSoundTimer = 0;
            }
            
            // Exhaust particles
            if (isEngineRunning() && currentSpeed > 0.1) {
                Vec3 exhaustPos = this.position().add(-Math.sin(Math.toRadians(this.getYRot())) * 1.5, 0.2, Math.cos(Math.toRadians(this.getYRot())) * 1.5);
                if (this.level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, 
                        exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                        2, 0.1, 0.1, 0.1, 0.02);
                }
            }
        } else {
            // Engine off when not being driven
            setEngineRunning(false);
            currentSpeed *= 0.95; // Coast to a stop
        }
        
        // Apply movement
        if (currentSpeed > 0.01) {
            Vec3 movement = Vec3.directionFromRotation(0, this.getYRot()).scale(currentSpeed);
            this.setDeltaMovement(movement.x, this.getDeltaMovement().y, movement.z);
        }
    }
    
    private void handleMovement(Player player) {
        // Skip fuel check in creative mode
        if (getFuelLevel() <= 0 && !player.isCreative()) return;
        
        // Simple movement based on look direction
        Vec3 lookVector = player.getLookAngle();
        
        // Use player's movement keys - we'll implement a key handler later
        // For now, just use forward movement when W is "pressed"
        // This is a simplified version - proper input handling requires client-side code
        
        // Acceleration/Deceleration (simplified - player moves forward)
        if (player.zza > 0 && currentSpeed < MAX_SPEED) { // W key approximation
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION);
        } else if (player.zza < 0 && currentSpeed > -MAX_SPEED * 0.5) { // S key approximation
            currentSpeed = Math.max(-MAX_SPEED * 0.5, currentSpeed - ACCELERATION);
        } else {
            currentSpeed *= 0.95; // Natural deceleration
        }
        
        // Steering (only when moving) - simplified
        if (Math.abs(currentSpeed) > 0.1) {
            float turnAmount = 2.0F * (float)(currentSpeed / MAX_SPEED);
            if (player.xxa > 0) { // D key approximation
                this.setYRot(this.getYRot() + turnAmount);
            } else if (player.xxa < 0) { // A key approximation
                this.setYRot(this.getYRot() - turnAmount);
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
