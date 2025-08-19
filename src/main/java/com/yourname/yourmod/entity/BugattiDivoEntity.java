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

public class BugattiDivoEntity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiDivoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiDivoEntity.class, EntityDataSerializers.BOOLEAN);
    
    // Divo specs - track-focused, best handling, higher fuel consumption
    private static final int MAX_FUEL = 800; // Less fuel capacity (track car)
    private static final double MAX_SPEED = 1.9; // Fast but not fastest
    private static final double ACCELERATION = 0.12; // Quick acceleration
    
    private double currentSpeed = 0;
    private int engineSoundTimer = 0;
    
    public BugattiDivoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.2F; // Better off-road capability
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
        
        // Refuel - needs premium fuel (blaze powder preferred)
        if (itemStack.is(Items.COAL) || itemStack.is(Items.BLAZE_POWDER)) {
            if (getFuelLevel() < MAX_FUEL) {
                int fuelToAdd = itemStack.is(Items.BLAZE_POWDER) ? 180 : 80; // Prefers blaze powder
                setFuelLevel(Math.min(MAX_FUEL, getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.2F, 1.2F); // Higher pitch
                return InteractionResult.SUCCESS;
            }
        }
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 1.2F, 1.0F); // Aggressive sound
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
            
            // Higher fuel consumption (track car)
            boolean isCreativeMode = player.isCreative();
            if (currentSpeed > 0 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % 15 == 0) { // Every 0.75 seconds (high consumption)
                    setFuelLevel(getFuelLevel() - 1);
                }
            }
            
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                currentSpeed *= 0.85; // Stops faster when no fuel
            }
            
            // Aggressive engine sounds
            engineSoundTimer++;
            if (engineSoundTimer > 30 && isEngineRunning()) { // Frequent sounds
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, 
                    0.6F, 0.7F + (float)(currentSpeed / MAX_SPEED * 0.5F)); // Higher pitch
                engineSoundTimer = 0;
            }
            
            // More exhaust particles (performance car)
            if (isEngineRunning() && currentSpeed > 0.05) {
                Vec3 exhaustPos = this.position().add(-Math.sin(Math.toRadians(this.getYRot())) * 1.3, 0.1, Math.cos(Math.toRadians(this.getYRot())) * 1.3);
                if (this.level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, // Flame particles for track car
                        exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                        1, 0.05, 0.05, 0.05, 0.01);
                    if (this.tickCount % 5 == 0) {
                        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, 
                            exhaustPos.x, exhaustPos.y + 0.1, exhaustPos.z, 
                            2, 0.1, 0.1, 0.1, 0.03);
                    }
                }
            }
        } else {
            setEngineRunning(false);
            currentSpeed *= 0.92; // Quick stop when not driven
        }
        
        // Apply movement with better grip
        if (currentSpeed > 0.01) {
            Vec3 movement = Vec3.directionFromRotation(0, this.getYRot()).scale(currentSpeed);
            this.setDeltaMovement(movement.x, this.getDeltaMovement().y * 0.95, movement.z); // Better ground grip
        }
    }
    
    private void handleMovement(Player player) {
        if (getFuelLevel() <= 0 && !player.isCreative()) return;
        
        // Divo has aggressive, precise handling
        if (player.zza > 0 && currentSpeed < MAX_SPEED) {
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION);
        } else if (player.zza < 0 && currentSpeed > -MAX_SPEED * 0.6) { // Good reverse
            currentSpeed = Math.max(-MAX_SPEED * 0.6, currentSpeed - ACCELERATION);
        } else {
            currentSpeed *= 0.93; // Quick deceleration
        }
        
        // Superior handling - sharp turns
        if (Math.abs(currentSpeed) > 0.05) {
            float turnAmount = 2.5F * (float)(currentSpeed / MAX_SPEED); // Sharp turning
            if (player.xxa > 0) {
                this.setYRot(this.getYRot() + turnAmount);
                // Tire screech effect at high speed turns
                if (Math.abs(currentSpeed) > 1.0 && this.tickCount % 20 == 0) {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                        SoundEvents.HORSE_GALLOP, SoundSource.NEUTRAL, 0.3F, 1.5F);
                }
            } else if (player.xxa < 0) {
                this.setYRot(this.getYRot() - turnAmount);
                if (Math.abs(currentSpeed) > 1.0 && this.tickCount % 20 == 0) {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                        SoundEvents.HORSE_GALLOP, SoundSource.NEUTRAL, 0.3F, 1.5F);
                }
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        
        if (!this.level.isClientSide && this.getHealth() - amount <= 0) {
            this.spawnAtLocation(ModItems.BUGATTI_DIVO.get());
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
