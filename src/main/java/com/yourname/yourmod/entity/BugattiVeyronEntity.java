package com.yourname.yourmod.entity;

import com.yourname.yourmod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BugattiVeyronEntity extends BugattiCarEntity {
    // Veyron specs - luxury touring car with efficiency focus
    private static final int VEYRON_MAX_FUEL = 1200; // More fuel capacity
    private static final double VEYRON_MAX_SPEED = 1.6; // Moderate speed
    private static final double VEYRON_ACCELERATION = 0.06; // Smoother acceleration
    private static final double VEYRON_DECELERATION = 0.96; // Better coasting
    private static final double VEYRON_TURN_SPEED = 1.8F; // Controlled turning
    private static final double VEYRON_MIN_TURN_SPEED = 0.2; // Stable at low speeds
    
    public BugattiVeyronEntity(EntityType<? extends BugattiCarEntity> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0F; // Standard step height
    }
    
    @Override
    protected int getMaxFuel() {
        return VEYRON_MAX_FUEL;
    }
    
    @Override
    protected double getMaxSpeed() {
        return VEYRON_MAX_SPEED;
    }
    
    @Override
    protected double getAcceleration() {
        return VEYRON_ACCELERATION;
    }
    
    @Override
    protected double getDeceleration() {
        return VEYRON_DECELERATION;
    }
    
    @Override
    protected double getTurnSpeed() {
        return VEYRON_TURN_SPEED;
    }
    
    @Override
    protected double getMinTurnSpeed() {
        return VEYRON_MIN_TURN_SPEED;
    }
    
    @Override
    protected int getConsumptionRate() {
        return 30; // Very efficient fuel consumption (every 1.5 seconds)
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // Refuel - very fuel efficient
        if (itemStack.is(Items.COAL) || itemStack.is(Items.BLAZE_POWDER)) {
            if (getFuelLevel() < getMaxFuel()) {
                int fuelToAdd = itemStack.is(Items.BLAZE_POWDER) ? 250 : 150; // More fuel per item
                setFuelLevel(Math.min(getMaxFuel(), getFuelLevel() + fuelToAdd));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 0.8F); // Lower pitch
                return InteractionResult.SUCCESS;
            }
        }
        
        // Mount/dismount with luxury car sounds
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
    protected void handleExhaustEffects() {
        // Minimal exhaust effects for the efficient Veyron
        if (isEngineRunning() && Math.abs(getCurrentSpeed()) > 0.1 && this.tickCount % 10 == 0) {
            Vec3 exhaustPos = this.position().add(
                -Math.sin(Math.toRadians(this.getYRot())) * 1.5, 
                0.2, 
                Math.cos(Math.toRadians(this.getYRot())) * 1.5
            );
            
            if (this.level instanceof ServerLevel serverLevel) {
                // Clean, minimal exhaust (luxury efficiency)
                serverLevel.sendParticles(ParticleTypes.SMOKE, 
                    exhaustPos.x, exhaustPos.y, exhaustPos.z, 
                    1, 0.05, 0.05, 0.05, 0.005);
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
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ANVIL_HIT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_EXPLODE;
    }
}
