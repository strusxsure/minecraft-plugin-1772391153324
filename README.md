# FireMace Plugin

A Paper plugin for Minecraft 1.21 that adds a custom Fire Mace weapon.

## Features

- Right-click with the Fire Mace (custom model data: 1) to activate:
  - Area-based heat damage over time
  - Lava trap at the target location
  - Spawning fire-related mobs (blazes, magma cubes)
  - A timed flame wall in the direction you're looking

## Requirements

- Minecraft 1.21
- Paper server 1.21 or compatible
- Java 21

## Installation

1. Build the plugin using Maven: `mvn clean package`
2. Place the generated `FireMace-1.0.0.jar` in your server's `plugins` folder.
3. Restart the server.

## Configuration

After first run, a `config.yml` will be generated in the plugin's data folder. You can adjust:

- `duration`: How long the effect lasts (in ticks, 20 ticks = 1 second)
- `damage-interval`: How often to apply damage (in ticks)
- `damage-amount`: Amount of damage per interval
- `damage-radius`: Radius of the damage area (in blocks)
- `mob-spawn-interval`: How often to spawn mobs (in ticks)
- `mob-types`: List of entity types to spawn (e.g., BLAZE, MAGMA_CUBE)
- `flame-wall.enabled`: Enable/disable flame wall
- `flame-wall.length`: Length of the flame wall
- `flame-wall.height`: Height of the flame wall
- `lava-trap.enabled`: Enable/disable lava trap
- `lava-trap.duration`: How long the lava stays (in ticks)

## Usage

Hold the Fire Mace (mace with custom model data 1) and right-click on a block to activate the effects.

## Permissions

- `firemace.reload` - Allows reloading the configuration with `/firemace reload`.

## Notes

- The Fire Mace is identified by custom model data 1. You must use a resource pack to give it that model, or use a command to create the item with that custom model data.
- The plugin does not provide a way to obtain the mace; you must give it via command or creative mode.

## Creating the Fire Mace Item

You can give yourself the mace with: