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

public class BugattiBolideEntity extends Animal {
    private static final EntityDataAccessor<Integer> FUEL_LEVEL = SynchedEntityData.defineId(BugattiBolideEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ENGINE_RUNNING = SynchedEntityData.defineId(BugattiBolideEntity.class, EntityDataSerializers.BOOLEAN);
    
    // Bolide specs - absolute fastest, track-only monster
    private static final int MAX_FUEL = 700; // Racing fuel tank
    private static final double MAX_SPEED = 2.5; // FASTEST CAR
    private static final double ACCELERATION = 0.18; // Insane acceleration
    
    private double currentSpeed = 0;
    private int engineSoundTimer = 0;
    private int boostCooldown = 0;
    
    public BugattiBolideEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.5F; // Can jump over blocks
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
        
        // Track car - only accepts racing fuel (blaze powder) or rocket fuel (fire charges)
        if (itemStack.is(Items.BLAZE_POWDER) || itemStack.is(Items.FIRE_CHARGE) || itemStack.is(Items.GUNPOWDER)) {
            if (getFuelLevel() < MAX_FUEL) {
                int fuelToAdd;
                if (itemStack.is(Items.FIRE_CHARGE)) {
                    fuelToAdd = 200; // Rocket fuel!
                } else if (itemStack.is(Items.BLAZE_POWDER)) {
                    fuelToAdd = 150;
                } else {
                    fuelToAdd = 100; // Gunpowder
                }
                
                setFuelLevel(Math.min(MAX_FUEL, getFuelLevel() + fuelToAdd));
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
        
        // Mount/dismount
        if (!this.isVehicle() && !player.isShiftKeyDown()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
                setEngineRunning(true);
                
                // Aggressive startup
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.3F, 2.0F);
                
                // Startup explosion effects
                if (this.level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 15; i++) {
                        serverLevel.sendParticles(ParticleTypes.LAVA,
                            this.getX() + (this.random.nextDouble() - 0.5) * 3,
                            this.getY() + this.random.nextDouble() * 2,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 3,
                            1, 0, 0.3, 0, 0.1);
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
            
            // High fuel consumption (race car)
            boolean isCreativeMode = player.isCreative();
            if (currentSpeed > 0 && getFuelLevel() > 0 && !isCreativeMode) {
                if (this.tickCount % 10 == 0) { // Every 0.5 seconds (highest consumption)
                    setFuelLevel(getFuelLevel() - 1);
                    
                    // Extra consumption at high speeds
                    if (currentSpeed > 2.0 && this.tickCount % 5 == 0) {
                        setFuelLevel(getFuelLevel() - 1);
                    }
                }
            }
            
            if (getFuelLevel() <= 0 && !isCreativeMode) {
                setEngineRunning(false);
                currentSpeed *= 0.8; // Quick stop
            }
            
            // Aggressive racing sounds
            engineSoundTimer++;
            if (engineSoundTimer > 20 && isEngineRunning()) {
                float pitch = 1.2F + (float)(currentSpeed / MAX_SPEED * 0.8F);
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.2F, pitch);
                engineSoundTimer = 0;
            }
            
            // Insane exhaust effects
            if (isEngineRunning() && currentSpeed > 0.05) {
                Vec3 exhaustPos = this.position().add(-Math.sin(Math.toRadians(this.getYRot())) * 1.8, 0.1, Math.cos(Math.toRadians(this.getYRot())) * 1.8);
                if (this.level instanceof ServerLevel serverLevel) {
                    // Multiple exhaust pipes
                    serverLevel.sendParticles(ParticleTypes.FLAME,
                        exhaustPos.x + 0.3, exhaustPos.y, exhaustPos.z, 
                        2, 0.1, 0.1, 0.1, 0.05);
                    serverLevel.sendParticles(ParticleTypes.FLAME,
                        exhaustPos.x - 0.3, exhaustPos.y, exhaustPos.z, 
                        2, 0.1, 0.1, 0.1, 0.05);
                    
                    if (currentSpeed > 2.0) { // Afterburner at high speed
                        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                            exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                            3, 0.2, 0.1, 0.2, 0.1);
                    }
                    
                    if (this.tickCount % 3 == 0) {
                        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                            exhaustPos.x, exhaustPos.y + 0.2, exhaustPos.z, 
                            4, 0.3, 0.2, 0.3, 0.05);
                    }
                }
            }
            
            // Speed boost effects at maximum velocity
            if (currentSpeed > 2.3) {
                if (this.level instanceof ServerLevel serverLevel && this.tickCount % 5 == 0) {
                    // Speed lines effect
                    for (int i = 0; i < 5; i++) {
                        Vec3 sidePos = this.position().add(
                            (this.random.nextDouble() - 0.5) * 4,
                            this.random.nextDouble() * 2,
                            (this.random.nextDouble() - 0.5) * 4);
                        serverLevel.sendParticles(ParticleTypes.CRIT,
                            sidePos.x, sidePos.y, sidePos.z,
                            1, 0, 0, 0, 0.3);
                    }
                }
            }
        } else {
            setEngineRunning(false);
            currentSpeed *= 0.9; // Quick deceleration when not driven
        }
        
        if (boostCooldown > 0) boostCooldown--;
        
        // Apply movement with enhanced physics
        if (currentSpeed > 0.01) {
            Vec3 movement = Vec3.directionFromRotation(0, this.getYRot()).scale(currentSpeed);
            this.setDeltaMovement(movement.x, this.getDeltaMovement().y * 0.9, movement.z); // Better ground contact
        }
    }
    
    private void handleMovement(Player player) {
        if (getFuelLevel() <= 0 && !player.isCreative()) return;
        
        // Bolide has explosive, extreme performance
        if (player.zza > 0 && currentSpeed < MAX_SPEED) {
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION);
            
            // Boost mode when shift is held (if available)
            if (player.isShiftKeyDown() && boostCooldown == 0 && currentSpeed > 1.5) {
                currentSpeed = Math.min(MAX_SPEED, currentSpeed + ACCELERATION * 2);
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
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.NEUTRAL, 0.5F, 2.0F);
            }
        } else if (player.zza < 0 && currentSpeed > -MAX_SPEED * 0.7) {
            currentSpeed = Math.max(-MAX_SPEED * 0.7, currentSpeed - ACCELERATION * 1.2); // Strong braking
        } else {
            currentSpeed *= 0.92; // Racing deceleration
        }
        
        // Extreme steering response
        if (Math.abs(currentSpeed) > 0.1) {
            float turnAmount = 2.8F * (float)(currentSpeed / MAX_SPEED);
            if (player.xxa > 0) {
                this.setYRot(this.getYRot() + turnAmount);
                // Tire screech at high speed
                if (currentSpeed > 1.8 && this.tickCount % 15 == 0) {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                        SoundEvents.HORSE_GALLOP, SoundSource.NEUTRAL, 0.5F, 2.0F);
                }
            } else if (player.xxa < 0) {
                this.setYRot(this.getYRot() - turnAmount);
                if (currentSpeed > 1.8 && this.tickCount % 15 == 0) {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                        SoundEvents.HORSE_GALLOP, SoundSource.NEUTRAL, 0.5F, 2.0F);
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
            this.spawnAtLocation(ModItems.BUGATTI_BOLIDE.get());
            
            // Explosive destruction - just remove the explosion for now to make it build
            // this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, false);
        }
        
        return super.hurt(damageSource, amount);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.GENERIC_EXPLODE;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_EXPLODE;
    }
    
    public boolean canBeRiddenInWater(Entity rider) {
        return false; // Definitely not designed for water
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
        compound.putInt("BoostCooldown", boostCooldown);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setFuelLevel(compound.getInt("FuelLevel"));
        setEngineRunning(compound.getBoolean("EngineRunning"));
        boostCooldown = compound.getInt("BoostCooldown");
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
