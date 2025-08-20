package com.yourname.yourmod.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
// import net.minecraft.world.level.material.Material; // Deprecated in newer versions
import net.minecraft.tags.BlockTags;
import net.minecraftforge.registries.RegistryObject;

/**
 * Advanced sound system for Bugatti vehicles
 * Handles RPM-based sounds, environmental effects, and model-specific audio profiles
 */
public class CarSoundSystem {
    
    // Sound timing and management
    private int engineSoundTimer = 0;
    private int turboSoundTimer = 0;
    private int gearShiftCooldown = 0;
    private int lastGear = 1;
    private boolean wasAccelerating = false;
    private double lastSpeed = 0;
    
    // Environmental detection
    private boolean inCave = false;
    private boolean underwater = false;
    private long lastEnvironmentCheck = 0;
    
    public enum CarModel {
        CHIRON, VEYRON, DIVO, TYPE35, CENTODIECI, BOLIDE
    }
    
    /**
     * Play engine sound based on current speed and RPM
     */
    public void playEngineSound(Entity entity, double currentSpeed, double maxSpeed, CarModel model) {
        // Allow sound playing on both client and server sides for proper synchronization
        
        // Debug: Log sound playing attempt
        if (entity.tickCount % 100 == 0) {
            System.out.println("[DEBUG] CarSoundSystem - Attempting to play engine sound. Speed: " + currentSpeed + ", Model: " + model);
        }
        
        engineSoundTimer++;
        if (engineSoundTimer < getEngineSoundInterval(currentSpeed)) return;
        
        engineSoundTimer = 0;
        
        // Calculate RPM ratio (0.0 to 1.0)
        double rpmRatio = Math.abs(currentSpeed) / maxSpeed;
        
        // Update environmental conditions
        updateEnvironmentalConditions(entity);
        
        // Select appropriate engine sound
        RegistryObject<SoundEvent> engineSound = getEngineSound(rpmRatio, model);
        
        // Calculate dynamic volume and pitch
        float volume = calculateEngineVolume(rpmRatio);
        float pitch = calculateEnginePitch(rpmRatio, model);
        
        // Apply environmental effects
        if (underwater) {
            volume *= 0.3f;
            pitch *= 0.6f;
            playEnvironmentalSound(entity, ModSounds.ENGINE_MUFFLED_UNDERWATER);
        } else if (inCave) {
            volume *= 1.2f;
            playEnvironmentalSound(entity, ModSounds.ENGINE_ECHO_CAVE);
        }
        
        // Play the engine sound
        try {
            SoundEvent soundEvent = engineSound.get();
            if (soundEvent != null) {
                System.out.println("[DEBUG] Playing engine sound: " + soundEvent + " at volume " + volume + ", pitch " + pitch);
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        soundEvent, SoundSource.NEUTRAL, volume, pitch);
            } else {
                System.out.println("[ERROR] Engine sound is null for model " + model + " at RPM ratio " + rpmRatio);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to play engine sound: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Handle additional sounds
        handleTurboSounds(entity, rpmRatio, model);
        handleGearShiftSounds(entity, currentSpeed);
        handleHighSpeedEffects(entity, currentSpeed, maxSpeed);
    }
    
    /**
     * Get appropriate engine sound based on RPM and model
     */
    private RegistryObject<SoundEvent> getEngineSound(double rpmRatio, CarModel model) {
        try {
            if (rpmRatio < 0.1) {
                return getIdleSound(model);
            } else if (rpmRatio < 0.4) {
                return getLowRPMSound(model);
            } else if (rpmRatio < 0.7) {
                return getMidRPMSound(model);
            } else if (rpmRatio < 0.9) {
                return getHighRPMSound(model);
            } else {
                return ModSounds.ENGINE_REDLINE;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to get engine sound for model " + model + " at RPM ratio " + rpmRatio + ": " + e.getMessage());
            // Fallback to generic engine sound
            return ModSounds.ENGINE_MID_RPM;
        }
    }
    
    /**
     * Get model-specific idle sound
     */
    private RegistryObject<SoundEvent> getIdleSound(CarModel model) {
        switch (model) {
            case CHIRON: return ModSounds.CHIRON_ENGINE_IDLE;
            case VEYRON: return ModSounds.VEYRON_ENGINE_IDLE;
            case DIVO: return ModSounds.DIVO_ENGINE_IDLE;
            case TYPE35: return ModSounds.TYPE35_ENGINE_IDLE;
            case CENTODIECI: return ModSounds.CENTODIECI_ENGINE_IDLE;
            case BOLIDE: return ModSounds.BOLIDE_ENGINE_IDLE;
            default: return ModSounds.ENGINE_IDLE_MID;
        }
    }
    
    /**
     * Get model-specific low RPM sound
     */
    private RegistryObject<SoundEvent> getLowRPMSound(CarModel model) {
        switch (model) {
            case CHIRON: return ModSounds.CHIRON_ENGINE_LOW;
            case VEYRON: return ModSounds.VEYRON_ENGINE_LOW;
            case DIVO: return ModSounds.DIVO_ENGINE_LOW;
            case TYPE35: return ModSounds.TYPE35_ENGINE_LOW;
            case CENTODIECI: return ModSounds.CENTODIECI_ENGINE_LOW;
            case BOLIDE: return ModSounds.BOLIDE_ENGINE_LOW;
            default: return ModSounds.ENGINE_LOW_RPM;
        }
    }
    
    /**
     * Get model-specific mid RPM sound
     */
    private RegistryObject<SoundEvent> getMidRPMSound(CarModel model) {
        switch (model) {
            case CHIRON: return ModSounds.CHIRON_ENGINE_MID;
            case VEYRON: return ModSounds.VEYRON_ENGINE_MID;
            case DIVO: return ModSounds.DIVO_ENGINE_MID;
            case TYPE35: return ModSounds.TYPE35_ENGINE_MID;
            case CENTODIECI: return ModSounds.CENTODIECI_ENGINE_MID;
            case BOLIDE: return ModSounds.BOLIDE_ENGINE_MID;
            default: return ModSounds.ENGINE_MID_RPM;
        }
    }
    
    /**
     * Get model-specific high RPM sound
     */
    private RegistryObject<SoundEvent> getHighRPMSound(CarModel model) {
        switch (model) {
            case CHIRON: return ModSounds.CHIRON_ENGINE_HIGH;
            case VEYRON: return ModSounds.VEYRON_ENGINE_HIGH;
            case DIVO: return ModSounds.DIVO_ENGINE_HIGH;
            case TYPE35: return ModSounds.TYPE35_ENGINE_HIGH;
            case CENTODIECI: return ModSounds.CENTODIECI_ENGINE_HIGH;
            case BOLIDE: return ModSounds.BOLIDE_ENGINE_HIGH;
            default: return ModSounds.ENGINE_HIGH_RPM;
        }
    }
    
    /**
     * Handle turbo sounds for applicable models
     */
    private void handleTurboSounds(Entity entity, double rpmRatio, CarModel model) {
        // Only certain models have turbo sounds
        if (model != CarModel.CHIRON && model != CarModel.BOLIDE && model != CarModel.DIVO) {
            return;
        }
        
        turboSoundTimer++;
        if (turboSoundTimer < 25) return; // Play every ~1.25 seconds
        
        turboSoundTimer = 0;
        
        if (rpmRatio > 0.6) {
            RegistryObject<SoundEvent> turboSound = getTurboSound(model);
            float volume = 0.4f + (float)(rpmRatio * 0.4f);
            float pitch = 1.0f + (float)(rpmRatio * 0.8f);
            
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    turboSound.get(), SoundSource.NEUTRAL, volume, pitch);
        }
    }
    
    /**
     * Get model-specific turbo sound
     */
    private RegistryObject<SoundEvent> getTurboSound(CarModel model) {
        switch (model) {
            case CHIRON: return ModSounds.CHIRON_TURBO;
            case BOLIDE: return ModSounds.BOLIDE_TURBO_EXTREME;
            case DIVO: return ModSounds.TURBO_SPOOL_UP;
            default: return ModSounds.TURBO_SPOOL_UP;
        }
    }
    
    /**
     * Handle automatic gear shift sounds based on speed changes
     */
    private void handleGearShiftSounds(Entity entity, double currentSpeed) {
        if (gearShiftCooldown > 0) {
            gearShiftCooldown--;
            return;
        }
        
        // Calculate current "gear" based on speed ranges
        int currentGear = calculateGear(currentSpeed);
        
        if (currentGear != lastGear && Math.abs(currentSpeed) > 0.3) {
            boolean shiftingUp = currentGear > lastGear;
            
            RegistryObject<SoundEvent> shiftSound = shiftingUp ? 
                ModSounds.GEAR_SHIFT_UP : ModSounds.GEAR_SHIFT_DOWN;
                
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    shiftSound.get(), SoundSource.NEUTRAL, 0.6f, 
                    shiftingUp ? 1.1f : 0.9f);
            
            lastGear = currentGear;
            gearShiftCooldown = 20; // Prevent rapid shifts
        }
    }
    
    /**
     * Calculate current gear based on speed
     */
    private int calculateGear(double speed) {
        double absSpeed = Math.abs(speed);
        if (absSpeed < 0.3) return 1;
        if (absSpeed < 0.7) return 2;
        if (absSpeed < 1.2) return 3;
        if (absSpeed < 1.7) return 4;
        if (absSpeed < 2.2) return 5;
        return 6; // Max gear
    }
    
    /**
     * Handle high-speed wind noise and effects
     */
    private void handleHighSpeedEffects(Entity entity, double currentSpeed, double maxSpeed) {
        double speedRatio = Math.abs(currentSpeed) / maxSpeed;
        
        // Wind noise at high speeds
        if (speedRatio > 0.7 && entity.tickCount % 40 == 0) {
            float windVolume = (float)(speedRatio * 0.3f);
            float windPitch = 1.0f + (float)(speedRatio * 0.5f);
            
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    ModSounds.WIND_NOISE_HIGH_SPEED.get(), SoundSource.NEUTRAL, 
                    windVolume, windPitch);
        }
    }
    
    /**
     * Play tire screech based on turning and braking
     */
    public void playTireScreech(Entity entity, double speed, boolean isTurning, boolean isBraking) {
        if (speed < 0.5) return;
        
        double speedRatio = Math.min(speed / 2.0, 1.0);
        RegistryObject<SoundEvent> screechSound;
        
        if (speedRatio > 0.8) {
            screechSound = ModSounds.TIRE_SCREECH_HEAVY;
        } else if (speedRatio > 0.5) {
            screechSound = ModSounds.TIRE_SCREECH_MEDIUM;
        } else {
            screechSound = ModSounds.TIRE_SCREECH_LIGHT;
        }
        
        float volume = (float)(0.4 + speedRatio * 0.4);
        float pitch = (float)(1.0 + speedRatio * 0.5);
        
        if (isBraking) {
            volume *= 1.3f;
        }
        
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                screechSound.get(), SoundSource.NEUTRAL, volume, pitch);
    }
    
    /**
     * Play brake sounds
     */
    public void playBrakeSound(Entity entity, double speed, boolean heavyBraking) {
        if (speed < 0.2) return;
        
        RegistryObject<SoundEvent> brakeSound = heavyBraking ? 
            ModSounds.BRAKE_HEAVY : ModSounds.BRAKE_LIGHT;
            
        float volume = heavyBraking ? 0.8f : 0.4f;
        float pitch = (float)(0.8 + Math.random() * 0.4);
        
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                brakeSound.get(), SoundSource.NEUTRAL, volume, pitch);
    }
    
    /**
     * Play model-specific horn sound
     */
    public void playHorn(Entity entity, CarModel model) {
        
        RegistryObject<SoundEvent> hornSound = getHornSound(model);
        
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                hornSound.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
    }
    
    /**
     * Get model-specific horn sound
     */
    private RegistryObject<SoundEvent> getHornSound(CarModel model) {
        switch (model) {
            case CHIRON:
            case CENTODIECI:
                return ModSounds.HORN_LUXURY;
            case VEYRON:
                return ModSounds.HORN_LUXURY;
            case DIVO:
            case BOLIDE:
                return ModSounds.HORN_RACING;
            case TYPE35:
                return ModSounds.HORN_VINTAGE;
            default:
                return ModSounds.HORN_LUXURY;
        }
    }
    
    /**
     * Play engine startup sequence
     */
    public void playEngineStartup(Entity entity, CarModel model) {
        
        // Play startup sequence
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                ModSounds.ENGINE_STARTUP_SEQUENCE.get(), SoundSource.NEUTRAL, 0.8f, 0.8f);
        
        // Special startup sounds for specific models
        if (model == CarModel.BOLIDE) {
            // Bolide has explosive startup
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    ModSounds.BOLIDE_AFTERBURNER.get(), SoundSource.NEUTRAL, 0.5f, 1.5f);
        } else if (model == CarModel.TYPE35) {
            // Type 35 has vintage backfire
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    ModSounds.TYPE35_VINTAGE_BACKFIRE.get(), SoundSource.NEUTRAL, 0.3f, 1.0f);
        }
    }
    
    /**
     * Play engine shutdown
     */
    public void playEngineShutdown(Entity entity) {
        
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                ModSounds.ENGINE_SHUTDOWN.get(), SoundSource.NEUTRAL, 0.6f, 0.8f);
    }
    
    /**
     * Update environmental conditions for sound effects
     */
    private void updateEnvironmentalConditions(Entity entity) {
        // Only check every 20 ticks (1 second) for performance
        if (entity.tickCount - lastEnvironmentCheck < 20) return;
        lastEnvironmentCheck = entity.tickCount;
        
        Level level = entity.level;
        BlockPos pos = entity.blockPosition();
        
        // Check if underwater
        underwater = entity.isInWater();
        
        // Check if in cave (surrounded by blocks)
        inCave = isSurroundedByBlocks(level, pos);
    }
    
    /**
     * Check if entity is in a cave-like environment
     */
    private boolean isSurroundedByBlocks(Level level, BlockPos pos) {
        int solidBlocks = 0;
        int totalChecked = 0;
        
        // Check blocks in a 3x3x3 area around the entity
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip center
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(checkPos);
                    
                    // Use a different method to check if block is solid - getMaterial() is deprecated
                    if (!blockState.isAir() && blockState.isSolidRender(level, checkPos)) {
                        solidBlocks++;
                    }
                    totalChecked++;
                }
            }
        }
        
        // Consider it a cave if more than 60% of surrounding blocks are solid
        return solidBlocks > (totalChecked * 0.6);
    }
    
    /**
     * Play environmental sound effect
     */
    private void playEnvironmentalSound(Entity entity, RegistryObject<SoundEvent> sound) {
        if (entity.tickCount % 60 == 0) { // Play every 3 seconds
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    sound.get(), SoundSource.NEUTRAL, 0.3f, 1.0f);
        }
    }
    
    /**
     * Calculate dynamic engine volume based on RPM
     */
    private float calculateEngineVolume(double rpmRatio) {
        return (float)(0.3 + rpmRatio * 0.5);
    }
    
    /**
     * Calculate dynamic engine pitch based on RPM and model
     */
    private float calculateEnginePitch(double rpmRatio, CarModel model) {
        float basePitch = getModelBasePitch(model);
        return basePitch + (float)(rpmRatio * 0.6);
    }
    
    /**
     * Get model-specific base pitch
     */
    private float getModelBasePitch(CarModel model) {
        switch (model) {
            case CHIRON: return 0.4f; // Deep W16 sound
            case VEYRON: return 0.5f; // Classic supercar
            case DIVO: return 0.6f; // Aggressive track sound
            case TYPE35: return 0.8f; // Higher vintage engine
            case CENTODIECI: return 0.45f; // Refined luxury
            case BOLIDE: return 0.7f; // Raw racing sound
            default: return 0.5f;
        }
    }
    
    /**
     * Get engine sound interval based on current speed (faster at higher RPM)
     */
    private int getEngineSoundInterval(double currentSpeed) {
        if (currentSpeed < 0.5) return 35; // Slower idle
        if (currentSpeed < 1.0) return 25; // Medium RPM
        if (currentSpeed < 1.5) return 20; // High RPM
        return 15; // Redline
    }
}
