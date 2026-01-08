# CS434 - Platform Runner Game

A 2D platform runner game built with LibGDX, demonstrating software design patterns.

## Features

- 7 playable levels with increasing difficulty
- Two weapon types: Bow (ranged) and Sword (melee)
- Multiple enemy types: Patrolling, Chasing, and Boss enemies
- Save/Load game functionality
- Health and score tracking
- Pause menu system

## Design Patterns Implemented

### Behavioral Patterns
1. **Strategy Pattern** - Player weapon system (BowStrategy, SwordStrategy)
2. **Command Pattern** - Player input handling (MoveLeft, MoveRight, Jump, Stop commands)
3. **Observer Pattern** - HUD updates when player health changes
4. **State Pattern** - Player states (Idle, Running, Jumping, Falling) and game states (Playing, Paused)
5. **Memento Pattern** - Save/load game state

### Structural Patterns
6. **Decorator Pattern** - Enhanced weapon abilities (DoubleShot, RapidFire)
7. **Facade Pattern** - ResourceManager simplifies asset loading

### Creational Patterns
8. **Factory Pattern** - Enemy creation (EnemyFactory)
9. **Singleton Pattern** - Global managers (GameManager, ResourceManager, PoolManager)

## How to Run

```bash
./gradlew lwjgl3:run
```

Or on Windows:
```bash
gradlew.bat lwjgl3:run
```

## Controls

- **A/D** or **Arrow Keys** - Move left/right
- **W** or **Space** - Jump
- **E** - Attack
- **Q** - Switch weapon
- **ESC** - Pause game
- **S** - Save game (during gameplay)
- **L** - Load saved game (during gameplay)

## Project Structure

```
core/src/main/java/com/ozu/platformrunner/
├── patterns/          # Design pattern implementations
│   ├── command/       # Command pattern
│   ├── decorator/     # Decorator pattern
│   ├── factory/       # Factory pattern
│   ├── memento/       # Memento pattern
│   ├── observer/      # Observer pattern
│   ├── state/         # State pattern
│   └── strategy/      # Strategy pattern
├── entities/          # Game entities (Player, Enemy, Bullet, etc.)
├── managers/          # Game managers (Singleton, Facade)
└── ui/               # User interface components
```

## Technologies Used

- Java
- LibGDX Framework
- Gradle Build System
