# FireMace Plugin v1.1.0 - Now with Ice Mace!

A Paper plugin for Minecraft 1.21 that adds two custom mace weapons with special area-based effects.

## Features

### 🔥 Fire Mace (Custom Model Data: 1)
Right-click on a block to activate:
- **Area-based heat damage** over time
- **Lava trap** at target location (temporary)
- **Fire mob spawn zones** (Blazes, Magma Cubes, Zombified Piglins)
- **Timed flame wall** in facing direction

### ❄️ Ice Mace (Custom Model Data: 2)
Right-click on a block to activate:
- **Slippery floor effect**: Turns ground blocks to ice in a radius
- **Random falling floor blocks**: Randomly removes ice blocks causing terrain instability
- **Snow blindness effect**: Applies blindness to nearby players
- **Freeze debuff**: Stacking slow effect (up to 5 stacks) that intensifies with time in area

## Developer Requirements Implemented

### Fire Mace:
- ✅ Area-based heat damage system
- ✅ Trigger blocks (lava trap)
- ✅ Configurable damage rate
- ✅ Mob spawn control

### Ice Mace:
- ✅ **Custom movement modifier**: Ice blocks create slippery surface
- ✅ **Temporary block removal system**: Falling blocks mechanic removes ice randomly
- ✅ **Vision reduction system**: Blindness potion effect
- ✅ **Freeze stacking mechanic**: Slow effect stacks up to 5 times (amplifier 0-4)

## Requirements

- Minecraft 1.21
- Paper server 1.21 or compatible fork
- Java 21

## Installation

1. Build the plugin using Maven: `mvn clean package`
2. Place the generated `FireMace-1.0.0.jar` in your server's `plugins/` folder
3. Restart the server
4. Configure `plugins/FireMace/config.yml` as needed

## Configuration

After first run, a `config.yml` will be generated with separate sections for each mace:

### Fire Mace Settings (`fire-mace`):
- `duration`: Effect duration in ticks (default: 200 = 10 seconds)
- `damage-interval`: Damage application interval (default: 20 ticks)
- `damage-amount`: Damage per interval (default: 2.0)
- `damage-radius`: Area radius in blocks (default: 5.0)
- `mob-spawn-interval`: Mob spawn frequency (default: 40 ticks)
- `mob-types`: List of entity types to spawn
- `flame-wall.enabled`: Toggle flame wall
- `flame-wall.length/dimensions`: Wall size
- `lava-trap.enabled`: Toggle lava trap
- `lava-trap.duration`: How long lava lasts

### Ice Mace Settings (`ice-mace`):
- `duration`: Effect duration in ticks (default: 200)
- `slippery-floor-radius`: Ice floor radius (default: 5.0)
- `falling-block-interval`: How often blocks fall (default: 40 ticks)
- `blindness-duration`: Blindness effect duration (default: 100 ticks)
- `freeze-max-stacks`: Maximum freeze stacks (default: 5)
- `freeze-stack-interval`: How often stacks increase (default: 20 ticks)

## Usage

### Obtaining the Maces
Use these commands (requires operator permissions):

**Fire Mace (Custom Model Data 1):**