# GameLib 2.0

## GameManager
- Manages Games
- Keeps track of scores and stats
- Has settings

```
- games: List<Game>				list of games
- players: List<GamePlayer>		list of players
- teams: List<Teams>			list of teams
```

## Game
- Represents a single game
- Has one or multiple arenas
- Has settings

```
- id: String			unique id of tha game, single word, all lowercase, used in commands
- name: String			name of the game used in messages, can contain formatting and colors
- arenas: List<Arena>	list of arenas this game can use
- status
- settings
```

## Arena
- Represents a arena of a game

```
- spawnpoints: List<Spawnpoint>		list of spawnpoints in this arena
```

## Spawnpoint
- Represents a location for people to spawn at

```
- location: org.bukkit.Location		the location of the spawnpoint
```

### TeamSpawnpoint
- Extends Spawnpoint to allow specification of a team

```
extend Spawnpoint
- teamId: String	the id of the team this spawnpoint is for //TODO: do this in a better way
```

## Team
- Represents a team

```
- id: String
- name: String
- color: ChatColor?
- players: List<GamePlayer>
```

## GamePlayer
- Represents a GamePlayer

```
- player: OfflinePlayer/Player
- team: Team
- game: Game
- type: GamePlayerType
```


NEXTUP:

=> Add commands to change settings, a command to save/load config & implement settings
=> Command permissions
=> World handling (loading worlds, saving worlds, resetting worlds, teleporting between worlds)
=> gameflow
    -> joining & rejoining (tp to spawnpoint)
    -> event handling
=> scores and stats
=> name prefixes & colors
=> modules
    -> loot chests
    -> cinematics
=> create minigames