package com.yourname.yourmod.sound;

import com.yourname.yourmod.YourMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, YourMod.MOD_ID);

    // === ENGINE SOUNDS ===
    // Startup sequence
    public static final RegistryObject<SoundEvent> ENGINE_STARTUP_SEQUENCE = registerSoundEvent("engine_startup_sequence");
    public static final RegistryObject<SoundEvent> ENGINE_CRANK = registerSoundEvent("engine_crank");
    public static final RegistryObject<SoundEvent> ENGINE_IGNITION = registerSoundEvent("engine_ignition");
    
    // Idle variations
    public static final RegistryObject<SoundEvent> ENGINE_IDLE_LOW = registerSoundEvent("engine_idle_low");
    public static final RegistryObject<SoundEvent> ENGINE_IDLE_MID = registerSoundEvent("engine_idle_mid");
    public static final RegistryObject<SoundEvent> ENGINE_IDLE_HIGH = registerSoundEvent("engine_idle_high");
    public static final RegistryObject<SoundEvent> ENGINE_IDLE_LUXURY = registerSoundEvent("engine_idle_luxury");
    public static final RegistryObject<SoundEvent> ENGINE_IDLE_VINTAGE = registerSoundEvent("engine_idle_vintage");
    
    // RPM-based engine sounds
    public static final RegistryObject<SoundEvent> ENGINE_LOW_RPM = registerSoundEvent("engine_low_rpm");
    public static final RegistryObject<SoundEvent> ENGINE_MID_RPM = registerSoundEvent("engine_mid_rpm");
    public static final RegistryObject<SoundEvent> ENGINE_HIGH_RPM = registerSoundEvent("engine_high_rpm");
    public static final RegistryObject<SoundEvent> ENGINE_REDLINE = registerSoundEvent("engine_redline");
    
    // Shutdown
    public static final RegistryObject<SoundEvent> ENGINE_SHUTDOWN = registerSoundEvent("engine_shutdown");
    public static final RegistryObject<SoundEvent> ENGINE_COOL_DOWN = registerSoundEvent("engine_cool_down");
    
    // === TURBO & SUPERCHARGER SOUNDS ===
    public static final RegistryObject<SoundEvent> TURBO_SPOOL_UP = registerSoundEvent("turbo_spool_up");
    public static final RegistryObject<SoundEvent> TURBO_SPOOL_DOWN = registerSoundEvent("turbo_spool_down");
    public static final RegistryObject<SoundEvent> TURBO_FLUTTER = registerSoundEvent("turbo_flutter");
    public static final RegistryObject<SoundEvent> TURBO_BLOW_OFF = registerSoundEvent("turbo_blow_off");
    public static final RegistryObject<SoundEvent> SUPERCHARGER_WHINE = registerSoundEvent("supercharger_whine");
    
    // === TRANSMISSION SOUNDS ===
    public static final RegistryObject<SoundEvent> GEAR_SHIFT_UP = registerSoundEvent("gear_shift_up");
    public static final RegistryObject<SoundEvent> GEAR_SHIFT_DOWN = registerSoundEvent("gear_shift_down");
    public static final RegistryObject<SoundEvent> CLUTCH_ENGAGE = registerSoundEvent("clutch_engage");
    public static final RegistryObject<SoundEvent> TRANSMISSION_WHINE = registerSoundEvent("transmission_whine");
    
    // === BRAKING SOUNDS ===
    public static final RegistryObject<SoundEvent> BRAKE_LIGHT = registerSoundEvent("brake_light");
    public static final RegistryObject<SoundEvent> BRAKE_HEAVY = registerSoundEvent("brake_heavy");
    public static final RegistryObject<SoundEvent> BRAKE_RACING = registerSoundEvent("brake_racing");
    public static final RegistryObject<SoundEvent> BRAKE_SQUEAL = registerSoundEvent("brake_squeal");
    public static final RegistryObject<SoundEvent> ABS_ENGAGE = registerSoundEvent("abs_engage");
    
    // === TIRE SOUNDS ===
    public static final RegistryObject<SoundEvent> TIRE_SCREECH_LIGHT = registerSoundEvent("tire_screech_light");
    public static final RegistryObject<SoundEvent> TIRE_SCREECH_MEDIUM = registerSoundEvent("tire_screech_medium");
    public static final RegistryObject<SoundEvent> TIRE_SCREECH_HEAVY = registerSoundEvent("tire_screech_heavy");
    public static final RegistryObject<SoundEvent> TIRE_BURNOUT = registerSoundEvent("tire_burnout");
    public static final RegistryObject<SoundEvent> TIRE_CHIRP = registerSoundEvent("tire_chirp");
    
    // === HORN SOUNDS ===
    public static final RegistryObject<SoundEvent> HORN_LUXURY = registerSoundEvent("horn_luxury");
    public static final RegistryObject<SoundEvent> HORN_AGGRESSIVE = registerSoundEvent("horn_aggressive");
    public static final RegistryObject<SoundEvent> HORN_VINTAGE = registerSoundEvent("horn_vintage");
    public static final RegistryObject<SoundEvent> HORN_RACING = registerSoundEvent("horn_racing");
    
    // === DOOR SOUNDS ===
    public static final RegistryObject<SoundEvent> DOOR_LUXURY_OPEN = registerSoundEvent("door_luxury_open");
    public static final RegistryObject<SoundEvent> DOOR_LUXURY_CLOSE = registerSoundEvent("door_luxury_close");
    public static final RegistryObject<SoundEvent> DOOR_RACING_OPEN = registerSoundEvent("door_racing_open");
    public static final RegistryObject<SoundEvent> DOOR_RACING_CLOSE = registerSoundEvent("door_racing_close");
    
    // === MODEL-SPECIFIC ENGINE SOUNDS ===
    // Chiron W16 Engine
    public static final RegistryObject<SoundEvent> CHIRON_ENGINE_IDLE = registerSoundEvent("chiron_engine_idle");
    public static final RegistryObject<SoundEvent> CHIRON_ENGINE_LOW = registerSoundEvent("chiron_engine_low");
    public static final RegistryObject<SoundEvent> CHIRON_ENGINE_MID = registerSoundEvent("chiron_engine_mid");
    public static final RegistryObject<SoundEvent> CHIRON_ENGINE_HIGH = registerSoundEvent("chiron_engine_high");
    public static final RegistryObject<SoundEvent> CHIRON_TURBO = registerSoundEvent("chiron_turbo");
    
    // Veyron Engine
    public static final RegistryObject<SoundEvent> VEYRON_ENGINE_IDLE = registerSoundEvent("veyron_engine_idle");
    public static final RegistryObject<SoundEvent> VEYRON_ENGINE_LOW = registerSoundEvent("veyron_engine_low");
    public static final RegistryObject<SoundEvent> VEYRON_ENGINE_MID = registerSoundEvent("veyron_engine_mid");
    public static final RegistryObject<SoundEvent> VEYRON_ENGINE_HIGH = registerSoundEvent("veyron_engine_high");
    
    // Divo Track-Focused
    public static final RegistryObject<SoundEvent> DIVO_ENGINE_IDLE = registerSoundEvent("divo_engine_idle");
    public static final RegistryObject<SoundEvent> DIVO_ENGINE_LOW = registerSoundEvent("divo_engine_low");
    public static final RegistryObject<SoundEvent> DIVO_ENGINE_MID = registerSoundEvent("divo_engine_mid");
    public static final RegistryObject<SoundEvent> DIVO_ENGINE_HIGH = registerSoundEvent("divo_engine_high");
    public static final RegistryObject<SoundEvent> DIVO_AGGRESSIVE_REV = registerSoundEvent("divo_aggressive_rev");
    
    // Type 35 Vintage
    public static final RegistryObject<SoundEvent> TYPE35_ENGINE_IDLE = registerSoundEvent("type35_engine_idle");
    public static final RegistryObject<SoundEvent> TYPE35_ENGINE_LOW = registerSoundEvent("type35_engine_low");
    public static final RegistryObject<SoundEvent> TYPE35_ENGINE_MID = registerSoundEvent("type35_engine_mid");
    public static final RegistryObject<SoundEvent> TYPE35_ENGINE_HIGH = registerSoundEvent("type35_engine_high");
    public static final RegistryObject<SoundEvent> TYPE35_VINTAGE_BACKFIRE = registerSoundEvent("type35_vintage_backfire");
    
    // Centodieci Luxury
    public static final RegistryObject<SoundEvent> CENTODIECI_ENGINE_IDLE = registerSoundEvent("centodieci_engine_idle");
    public static final RegistryObject<SoundEvent> CENTODIECI_ENGINE_LOW = registerSoundEvent("centodieci_engine_low");
    public static final RegistryObject<SoundEvent> CENTODIECI_ENGINE_MID = registerSoundEvent("centodieci_engine_mid");
    public static final RegistryObject<SoundEvent> CENTODIECI_ENGINE_HIGH = registerSoundEvent("centodieci_engine_high");
    
    // Bolide Track Beast
    public static final RegistryObject<SoundEvent> BOLIDE_ENGINE_IDLE = registerSoundEvent("bolide_engine_idle");
    public static final RegistryObject<SoundEvent> BOLIDE_ENGINE_LOW = registerSoundEvent("bolide_engine_low");
    public static final RegistryObject<SoundEvent> BOLIDE_ENGINE_MID = registerSoundEvent("bolide_engine_mid");
    public static final RegistryObject<SoundEvent> BOLIDE_ENGINE_HIGH = registerSoundEvent("bolide_engine_high");
    public static final RegistryObject<SoundEvent> BOLIDE_TURBO_EXTREME = registerSoundEvent("bolide_turbo_extreme");
    public static final RegistryObject<SoundEvent> BOLIDE_AFTERBURNER = registerSoundEvent("bolide_afterburner");
    
    // === ENVIRONMENTAL & EFFECTS ===
    public static final RegistryObject<SoundEvent> ENGINE_ECHO_CAVE = registerSoundEvent("engine_echo_cave");
    public static final RegistryObject<SoundEvent> ENGINE_MUFFLED_UNDERWATER = registerSoundEvent("engine_muffled_underwater");
    public static final RegistryObject<SoundEvent> WIND_NOISE_HIGH_SPEED = registerSoundEvent("wind_noise_high_speed");
    public static final RegistryObject<SoundEvent> COLLISION_IMPACT = registerSoundEvent("collision_impact");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(YourMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
