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

public class BugattiCentodieciEntity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiCentodieciEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiCentodieciEntity.class, EntityDataSerializers.BOOLEAN);
    
    // Centodieci specs - ultra-rare, high performance, exclusive
    private static final int MAX_FUEL = 1100;
    private static final double MAX_SPEED = 2.1; // Very fast
    private static final double ACCELERATION = 0.11; // Strong acceleration
    
    private double currentSpeed = 0;
    private int engineSoundTimer = 0;
    private int specialEffectTimer = 0;
    
    public BugattiCentodieciEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.1F;
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
        
        // Only accepts premium fuel (blaze powder) or diamonds (ultra luxury)
        if (itemStack.is(Items.BLAZE_POWDER) || itemStack.is(Items.DIAMOND)) {
            if (getFuelLevel() < MAX_FUEL) {
                int fuelToAdd = itemStack.is(Items.DIAMOND) ? 300 : 220; // Diamonds give lots of fuel!
                setFuelLevel(Math.min(MAX_FUEL, getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                
                // Luxury refuel sound and effects
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 1.0F, 1.5F);
                
                // Sparkle effects when refueling
                if (this.level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 5; i++) {
                        serverLevel.sendParticles(ParticleTypes.END_ROD,
                            this.getX() + (this.random.nextDouble() - 0.5) * 2,
                            this.getY() + 1 + this.random.nextDouble(),
                            this.getZ() + (this.random.nextDouble() - 0.5) * 2,
                            1, 0, 0.1, 0, 0.02);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        } else if (itemStack.is(Items.COAL)) {
            // Regular coal doesn't work - too common for this exclusive car
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                "§cThis exclusive vehicle only accepts premium fuel (Blaze Powder or Diamonds)"), true);
            return InteractionResult.FAIL;
        }
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                
                // Exclusive startup sound and effects
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.BEACON_ACTIVATE, SoundSource.NEUTRAL, 0.5F, 1.8F);
                
                // Startup particle effects
                if (this.level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 10; i++) {
                        serverLevel.sendParticles(ParticleTypes.ENCHANT,
                            this.getX() + (this.random.nextDouble() - 0.5) * 3,
                            this.getY() + this.random.nextDouble() * 2,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 3,
                            1, 0, 0.1, 0, 0.05);
                    }
                }
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
            
            // Moderate fuel consumption (luxury efficiency)
            boolean isCreativeMode = player.isCreative();
            if (currentSpeed > 0 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % 25 == 0) { // Every 1.25 seconds
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
            
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                currentSpeed *= 0.9;
            }
            
            // Exclusive engine sounds
            engineSoundTimer++;
            if (engineSoundTimer > 35 && isEngineRunning()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.BEACON_AMBIENT, SoundSource.NEUTRAL, 
                    0.3F, 1.0F + (float)(currentSpeed / MAX_SPEED * 0.5F));
                engineSoundTimer = 0;
            }
            
            // Special luxury effects while driving
            specialEffectTimer++;
            if (specialEffectTimer > 30 && isEngineRunning() && currentSpeed > 0.3) {
                if (this.level instanceof ServerLevel serverLevel) {
                    // Glitter trail
                    Vec3 trailPos = this.position().add(
                        -Math.sin(Math.toRadians(this.getYRot())) * 2.0,
                        0.1,
                        Math.cos(Math.toRadians(this.getYRot())) * 2.0);
                    serverLevel.sendParticles(ParticleTypes.FIREWORK,
                        trailPos.x, trailPos.y, trailPos.z,
                        2, 0.5, 0.1, 0.5, 0.01);
                }
                specialEffectTimer = 0;
            }
            
            // Premium exhaust effects
            if (isEngineRunning() && currentSpeed > 0.1) {
                Vec3 exhaustPos = this.position().add(-Math.sin(Math.toRadians(this.getYRot())) * 1.5, 0.2, Math.cos(Math.toRadians(this.getYRot())) * 1.5);
                if (this.level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, // Special blue flames
                        exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                        1, 0.05, 0.05, 0.05, 0.02);
                    if (this.tickCount % 8 == 0) {
                        serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH,
                            exhaustPos.x, exhaustPos.y + 0.1, exhaustPos.z, 
                            1, 0.1, 0.1, 0.1, 0.02);
                    }
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
        if (getFuelLevel() <= 0 && !player.isCreative()) return;
        
        // Centodieci has smooth, powerful performance
        if (player.zza > 0 && currentSpeed < MAX_SPEED) {
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION);
        } else if (player.zza < 0 && currentSpeed > -MAX_SPEED * 0.5) {
            currentSpeed = Math.max(-MAX_SPEED * 0.5, currentSpeed - ACCELERATION);
        } else {
            currentSpeed *= 0.95; // Smooth deceleration
        }
        
        // Precise luxury handling
        if (Math.abs(currentSpeed) > 0.05) {
            float turnAmount = 2.2F * (float)(currentSpeed / MAX_SPEED);
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
            this.spawnAtLocation(ModItems.BUGATTI_CENTODIECI.get());
            
            // Special destruction effects
            if (this.level instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 20; i++) {
                    serverLevel.sendParticles(ParticleTypes.FIREWORK,
                        this.getX() + (this.random.nextDouble() - 0.5) * 4,
                        this.getY() + this.random.nextDouble() * 3,
                        this.getZ() + (this.random.nextDouble() - 0.5) * 4,
                        1, 0, 0.2, 0, 0.1);
                }
            }
        }
        
        return super.hurt(damageSource, amount);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BEACON_DEACTIVATE;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_BREAK_BLOCK;
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
