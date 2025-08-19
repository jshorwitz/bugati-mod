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

public class BugattiVeyronEntity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiVeyronEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiVeyronEntity.class, EntityDataSerializers.BOOLEAN);
    
    // Veyron specs - more fuel efficient, moderate speed
    private static final int MAX_FUEL = 1200; // More fuel capacity
    private static final double MAX_SPEED = 1.6; // Slower than Chiron
    private static final double ACCELERATION = 0.08; // Smoother acceleration
    
    private double currentSpeed = 0;
    private int engineSoundTimer = 0;
    
    public BugattiVeyronEntity(EntityType<? extends Animal> entityType, Level level) {
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
        
        // Refuel with coal (more efficient than Chiron)
        if (itemStack.is(Items.COAL) || itemStack.is(Items.BLAZE_POWDER)) {
            if (getFuelLevel() < MAX_FUEL) {
                int fuelToAdd = itemStack.is(Items.BLAZE_POWDER) ? 250 : 150; // More fuel per item
                setFuelLevel(Math.min(MAX_FUEL, getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 0.8F); // Lower pitch
                return InteractionResult.SUCCESS;
            }
        }
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 0.8F, 0.6F); // Deeper sound
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
            
            // Consume fuel more efficiently (every 1.5 seconds instead of 1)
            boolean isCreativeMode = player.isCreative();
            if (currentSpeed > 0 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % 30 == 0) { // Every 1.5 seconds (more efficient)
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
            
            // Stop engine if no fuel (unless creative mode)
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                currentSpeed *= 0.9;
            }
            
            // Engine sounds (deeper than Chiron)
            engineSoundTimer++;
            if (engineSoundTimer > 50 && isEngineRunning()) { // Slower sound interval
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 
                    0.4F, 0.4F + (float)(currentSpeed / MAX_SPEED * 0.3F)); // Deeper sounds
                engineSoundTimer = 0;
            }
            
            // Less exhaust particles (more efficient)
            if (isEngineRunning() && currentSpeed > 0.1) {
                Vec3 exhaustPos = this.position().add(-Math.sin(Math.toRadians(this.getYRot())) * 1.5, 0.2, Math.cos(Math.toRadians(this.getYRot())) * 1.5);
                if (this.level instanceof ServerLevel serverLevel && this.tickCount % 10 == 0) { // Less frequent
                    serverLevel.sendParticles(ParticleTypes.SMOKE, 
                        exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                        1, 0.1, 0.1, 0.1, 0.01); // Smaller exhaust
                }
            }
        } else {
            setEngineRunning(false);
            currentSpeed *= 0.95;
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
        
        // Veyron has smoother, more controlled movement
        if (player.zza > 0 && currentSpeed < MAX_SPEED) {
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION);
        } else if (player.zza < 0 && currentSpeed > -MAX_SPEED * 0.4) { // Less reverse speed
            currentSpeed = Math.max(-MAX_SPEED * 0.4, currentSpeed - ACCELERATION);
        } else {
            currentSpeed *= 0.96; // Slightly better coasting
        }
        
        // Smoother steering
        if (Math.abs(currentSpeed) > 0.05) {
            float turnAmount = 1.8F * (float)(currentSpeed / MAX_SPEED); // More controlled turning
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
            this.spawnAtLocation(ModItems.BUGATTI_VEYRON.get());
        }
        
        return super.hurt(damageSource, amount);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
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
