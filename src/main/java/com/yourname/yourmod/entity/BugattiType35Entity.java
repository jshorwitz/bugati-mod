package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
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

public class BugattiType35Entity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiType35Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiType35Entity.class, EntityDataSerializers.BOOLEAN);
    
    // Type 35 specs - vintage, nimble, small, efficient
    private static final int MAX_FUEL = 600; // Smaller fuel tank
    private static final double MAX_SPEED = 1.4; // Moderate speed but nimble
    private static final double ACCELERATION = 0.15; // Very quick acceleration
    
    private double currentSpeed = 0;
    private int engineSoundTimer = 0;
    
    public BugattiType35Entity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 0.8F; // Lower profile, less step height
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
        
        // Vintage fuel - coal works great, but also accepts wood (classic fuel)
        if (itemStack.is(Items.COAL) || itemStack.is(Items.BLAZE_POWDER) || 
            itemStack.is(Items.CHARCOAL) || itemStack.getItem().toString().contains("log")) {
            if (getFuelLevel() < MAX_FUEL) {
                int fuelToAdd;
                if (itemStack.is(Items.BLAZE_POWDER)) {
                    fuelToAdd = 120;
                } else if (itemStack.is(Items.COAL) || itemStack.is(Items.CHARCOAL)) {
                    fuelToAdd = 80;
                } else {
                    fuelToAdd = 40; // Wood/logs give less fuel
                }
                
                setFuelLevel(Math.min(MAX_FUEL, getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 0.8F, 1.4F); // Vintage sound
                return InteractionResult.SUCCESS;
            }
        }
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                // Vintage engine start sound
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 0.6F, 1.2F);
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
            
            // Very fuel efficient (vintage car)
            boolean isCreativeMode = player.isCreative();
            if (currentSpeed > 0 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % 40 == 0) { // Every 2 seconds (very efficient)
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
            
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                currentSpeed *= 0.88;
            }
            
            // Vintage engine sounds (lower pitch, less frequent)
            engineSoundTimer++;
            if (engineSoundTimer > 60 && isEngineRunning()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 
                    0.3F, 0.6F + (float)(currentSpeed / MAX_SPEED * 0.4F)); // Vintage rumble
                engineSoundTimer = 0;
            }
            
            // Vintage exhaust - less particles, more smoke
            if (isEngineRunning() && currentSpeed > 0.1) {
                Vec3 exhaustPos = this.position().add(-Math.sin(Math.toRadians(this.getYRot())) * 1.0, 0.3, Math.cos(Math.toRadians(this.getYRot())) * 1.0);
                if (this.level instanceof ServerLevel serverLevel && this.tickCount % 15 == 0) {
                    serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, // Vintage smoke
                        exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                        1, 0.15, 0.15, 0.15, 0.02);
                }
            }
        } else {
            setEngineRunning(false);
            currentSpeed *= 0.94;
        }
        
        // Apply movement (nimble handling)
        if (currentSpeed > 0.01) {
            Vec3 movement = Vec3.directionFromRotation(0, this.getYRot()).scale(currentSpeed);
            this.setDeltaMovement(movement.x, this.getDeltaMovement().y, movement.z);
        }
    }
    
    private void handleMovement(Player player) {
        if (getFuelLevel() <= 0 && !player.isCreative()) return;
        
        // Type 35 has nimble, responsive handling
        if (player.zza > 0 && currentSpeed < MAX_SPEED) {
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION);
        } else if (player.zza < 0 && currentSpeed > -MAX_SPEED * 0.3) { // Limited reverse (vintage)
            currentSpeed = Math.max(-MAX_SPEED * 0.3, currentSpeed - ACCELERATION * 0.7);
        } else {
            currentSpeed *= 0.94; // Good coasting
        }
        
        // Nimble steering - very responsive
        if (Math.abs(currentSpeed) > 0.02) {
            float turnAmount = 3.2F * (float)(currentSpeed / MAX_SPEED); // Very responsive
            if (player.xxa > 0) {
                this.setYRot(this.getYRot() + turnAmount);
            } else if (player.xxa < 0) {
                this.setYRot(this.getYRot() - turnAmount);
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        
        if (!this.level.isClientSide && this.getHealth() - amount <= 0) {
            this.spawnAtLocation(ModItems.BUGATTI_TYPE35.get());
        }
        
        return super.hurt(damageSource, amount);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WOOD_HIT; // Vintage car sound
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOOD_BREAK; // Vintage breakdown
    }
    
    public boolean canBeRiddenInWater(Entity rider) {
        return false;
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
    
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob animal) {
        return null;
    }
    
    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }
}
