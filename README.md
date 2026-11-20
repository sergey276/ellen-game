# Project Ellen

A 2D top-down action/puzzle game in Java inspired by *Alien*. You play Ripley and have to escape from a space station: collect items, repair and switch devices, open locked doors and fight aliens.

Built for the Object-Oriented Programming course at TU Košice on top of the course's `gamelib` framework.

## What is implemented

- **Characters**: `Ripley` (player, health, backpack, gun), `Alien`, `MotherAlien`, behaviours such as `RandomlyMoving` and `Observing`.
- **Items**: wrench, hammer, Mjolnir, fire extinguisher, ammo, energy, access card; a `Backpack` and `Keeper` / `Collectible` / `Usable` abstractions.
- **Devices**: reactor (heating, cooling, repair), coolers, lights, computer, ventilator, power switch, teleport, time and chain bombs, helicopter.
- **Doors and locks**: `Door`, `LockedDoor`, `Locker`.
- **Weapons**: `Gun` / `Firearm`, `Bullet`.
- **Scenarios** (Tiled `.tmx` maps): training gameplay, first steps, escape room, mission impossible.
- Design: actors and actions on a `Scene`, interchangeable `Behaviour`s for enemies, and a `Factory` that creates actors from the map.

## Requirements

- JDK 11+
- Access to the course Maven repository for `gamelib` (`repo.kpi.fei.tuke.sk`) — this library is **not public**, so the project can only be built from the university network or with a local copy of the library.

## Run

```bash
./gradlew run
```

## Assets

Sprites and tilesets come from the course's assignment materials and are included for the project to be runnable; they are not covered by the MIT license below.

## License

Source code: MIT, see [LICENSE](LICENSE).
