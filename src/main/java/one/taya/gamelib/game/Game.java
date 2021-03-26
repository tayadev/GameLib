package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.GameFlag;
import one.taya.gamelib.enums.GameStatus;
import one.taya.gamelib.utils.IdUtil;

public class Game implements ConfigurationSerializable {

    @Getter private String id;
    @Getter @Setter private String name;

    // The plugin registering this game
    @Getter private Plugin plugin;

    @Getter @Setter private GameStatus status;

    @Getter @Setter private int health;
    @Getter @Setter private int food;
    @Getter @Setter private GameMode gameMode;
    @Getter @Setter private Difficulty difficulty;

    @Getter @Setter private Set<GameFlag> flags = new HashSet<GameFlag>();

    @Getter private Set<Arena> arenas = new HashSet<Arena>();

    public Game(String id, String name, int health, int food, GameMode gameMode, Difficulty difficulty, Set<GameFlag> flags, Set<Arena> arenas, Plugin plugin) {
        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.name = name;

        this.plugin = plugin;

        this.health = health;
        this.food = food;
        this.gameMode = gameMode;
        this.difficulty = difficulty;

        this.flags = flags;

        this.arenas = arenas;

        this.status = GameStatus.WAITING;

        // Register game
        GameLib.addGame(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("health", health);
        map.put("food", food);
        map.put("gameMode", gameMode.toString());
        map.put("difficulty", difficulty.toString());
        map.put("flags", flags.stream().map(Enum::toString).collect(Collectors.toSet()));
        map.put("arenas", arenas.stream().map(Arena::serialize).collect(Collectors.toSet()));
        map.put("plugin", plugin);
        return map;
    }

    public static Game deserialize(Map<String, Object> serialized) {
        return new Game(
            (String) serialized.get("id"),
            (String) serialized.get("name"),
            (int) serialized.get("health"),
            (int) serialized.get("food"),
            GameMode.valueOf((String) serialized.get("gameMode")),
            Difficulty.valueOf((String) serialized.get("difficulty")),
            Stream.of(serialized.get("flags")).map(String.class::cast).map(f -> GameFlag.valueOf(f)).collect(Collectors.toSet()),
            Stream.of(serialized.get("arenas")).map(Map.class::cast).map(s -> Arena.deserialize(s)).collect(Collectors.toSet()),
            (Plugin) serialized.get("plugin")
        );
    }

}
