# Bugatti Car Entity Driving Improvements

## Overview
This update significantly improves the driving mechanics for all Bugatti car entities in the mod. The improvements focus on three main areas: input detection, physics, and movement quality.

## Key Improvements

### 1. Enhanced Input Detection
- **Problem Fixed**: Replaced unreliable `player.zza` and `player.xxa` direct access with stabilized input detection
- **Solution**: Implemented input stability checking that prevents jittery movement from inconsistent input values
- **Benefits**: 
  - Smoother WASD key detection
  - Eliminates input ghosting and lag
  - More responsive controls

### 2. Improved Physics System
- **Smooth Acceleration/Deceleration**: Uses target speed interpolation for natural speed changes
- **Better Gravity Handling**: Enhanced gravity with speed-dependent force for realistic movement
- **Terrain Collision**: Proper collision detection with bounce-back effects and collision sounds
- **Momentum System**: Realistic momentum and inertia for car-like physics
- **Slope Resistance**: Cars slow down appropriately on inclines

### 3. Enhanced Movement Mechanics
- **Speed-Dependent Steering**: 
  - Slower, more stable turning at low speeds
  - Increased responsiveness at high speeds
  - Prevents unrealistic sharp turns at standstill
- **Improved Reverse**: Better reverse mechanics with appropriate speed limits
- **Terrain Handling**: Cars handle uneven terrain more realistically
- **Ground Friction**: Enhanced friction system for better surface interaction

## Technical Implementation

### Base Class Architecture
- `BugattiCarEntity` now serves as an improved base class with virtual methods
- All car variants inherit the improved mechanics while maintaining unique characteristics
- Cleaner, more maintainable code structure

### Virtual Methods for Customization
Each car variant can override:
- `getMaxSpeed()` - Top speed characteristics
- `getAcceleration()` - Acceleration profile
- `getDeceleration()` - Braking and coasting behavior
- `getTurnSpeed()` / `getMinTurnSpeed()` - Handling characteristics
- `getMaxFuel()` - Fuel capacity
- `getConsumptionRate()` - Fuel efficiency
- `handleExhaustEffects()` - Visual effects

### Car-Specific Characteristics

#### Bugatti Divo (Track-Focused)
- Higher acceleration (0.12)
- Superior handling (3.2 turn speed)
- Higher fuel consumption (15-tick rate)
- Enhanced exhaust effects with flame particles
- Better off-road capability (1.2F step height)

#### Bugatti Veyron (Luxury Touring)
- Smooth acceleration (0.06)
- Excellent fuel efficiency (30-tick rate)
- Large fuel capacity (1200)
- Minimal, clean exhaust effects
- Controlled, stable handling

## Usage
All improvements are automatically applied when using any Bugatti car entity. Players will notice:
- More responsive and predictable controls
- Smoother acceleration and braking
- Better handling on different terrain types
- More realistic car-like physics
- Unique feel for different car models

## Building
Run `./gradlew build` to compile the mod with the improved driving mechanics.

## Testing
The improvements can be tested by:
1. Spawning different Bugatti car entities
2. Testing acceleration, braking, and steering responsiveness
3. Driving on various terrain types (flat, slopes, obstacles)
4. Comparing the unique characteristics of different car models
