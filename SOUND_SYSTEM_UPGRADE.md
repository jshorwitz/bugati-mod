# Bugatti Mod Sound System Upgrade

## Overview
This upgrade transforms the basic car sounds into a comprehensive, realistic sound system with model-specific audio profiles and dynamic sound progression.

## Key Improvements

### 1. Enhanced Sound Registry (ModSounds.java)
- **87 new sound events** replacing the original 9 basic sounds
- **Startup sequence**: engine crank, ignition, startup
- **RPM-based sounds**: idle variations, low/mid/high RPM, redline
- **Turbo/Supercharger**: spool up/down, flutter, blow-off valve, whine
- **Transmission**: gear shifts, clutch engage, transmission whine
- **Braking system**: light/heavy/racing brakes, ABS engage, brake squeal
- **Tire sounds**: light/medium/heavy screech, burnout, tire chirp
- **Model-specific horns**: luxury, aggressive, vintage, racing
- **Environmental effects**: cave echo, underwater muffling, wind noise

### 2. Comprehensive sounds.json
- **1000+ lines** of detailed sound mapping vs original 92 lines
- **Layered audio**: Multiple Minecraft sounds combined for realistic effects
- **Dynamic pitch/volume**: Speed and context-dependent audio characteristics
- **Model-specific sounds**: Each Bugatti has unique engine characteristics

### 3. Advanced Sound System (CarSoundSystem.java)
- **RPM-based engine sounds**: Automatic progression from idle to redline
- **Automatic gear shifting**: Sound effects based on speed changes
- **Environmental adaptation**: Cave echoes, underwater muffling
- **Turbo/supercharger effects**: Model-specific forced induction sounds
- **Dynamic tire screech**: Based on turning speed and braking intensity
- **Model-specific profiles**: Each car has unique audio characteristics

### 4. Model-Specific Sound Profiles

#### Chiron (W16 Luxury)
- Deep, powerful W16 engine sound
- Sophisticated turbo effects
- Refined luxury characteristics
- Lower base pitch (0.4f) for that massive engine feel

#### Veyron (Classic Supercar)
- Classic supercar rumble
- Balanced performance sound
- Medium pitch range for timeless appeal

#### Divo (Track-Focused)
- Aggressive, sharp engine note
- Enhanced gear shift sounds
- Track-oriented audio profile
- Higher pitch for racing aggressiveness

#### Type 35 (Vintage Racing)
- Classic vintage engine sound
- Occasional backfire effects
- Higher-pitched vintage characteristics
- Distinctive old-school racing audio

#### Centodieci (Ultra-Luxury)
- Refined, sophisticated sound
- Luxury-oriented audio profile
- Subtle but powerful engine note

#### Bolide (Raw Track Beast)
- Most aggressive engine sound
- Extreme turbo/afterburner effects
- Raw, unfiltered racing audio
- Highest performance sound profile

### 5. Enhanced Entity System
- **Base class inheritance**: All models extend BugattiCarEntity
- **Model-specific overrides**: Each car has unique performance characteristics
- **Sound integration**: Automatic model detection for appropriate sounds
- **Performance tuning**: Each model has different fuel consumption, acceleration, etc.

## Technical Features

### Dynamic Sound Progression
- **Idle → Low RPM → Mid RPM → High RPM → Redline**
- Smooth transitions based on actual vehicle speed
- Environmental modifications (caves, underwater)

### Interactive Sound Effects
- **Tire screech**: Triggered by high-speed turning or hard braking
- **Gear shifts**: Automatic based on speed thresholds
- **Turbo effects**: For applicable models (Chiron, Bolide, Divo)
- **Brake sounds**: Different intensities based on braking force

### Environmental Adaptation
- **Cave detection**: Adds echo effects in enclosed spaces
- **Underwater**: Muffled sounds with bubble effects
- **High-speed wind**: Wind noise at extreme speeds

## File Changes

### New Files
- `CarSoundSystem.java` - Advanced sound management system
- `BugattiChironEntity.java` - Chiron-specific implementation
- `BugattiDivoEntity.java` - Divo-specific implementation  
- `BugattiCentodieciEntity.java` - Centodieci-specific implementation

### Updated Files
- `ModSounds.java` - Expanded from 9 to 87 sound events
- `sounds.json` - Enhanced from 92 to 1000+ lines
- `BugattiCarEntity.java` - Integrated new sound system
- `BugattiBolideEntity.java` - Updated to use new sound system
- `BugattiType35Entity.java` - Converted to new inheritance system

## Usage
Each car now automatically:
1. Plays appropriate startup sounds when mounted
2. Dynamically adjusts engine sounds based on speed/RPM
3. Triggers tire screech during aggressive turning or braking
4. Plays gear shift sounds during acceleration/deceleration
5. Adapts to environmental conditions (caves, underwater)
6. Uses model-specific horn sounds and audio characteristics

## Build Instructions
Run `./gradlew build` to compile with the new sound system. The enhanced audio will provide a dramatically more immersive and realistic driving experience for each Bugatti model.
