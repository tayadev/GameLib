package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.GameStatus;
import one.taya.gamelib.utils.IdUtil;

public class Game implements ConfigurationSerializable {

    @Getter private String id;
    @Getter @Setter private String name;

    @Getter @Setter private GameStatus status;
    @Getter private GameSettings settings;

    @Getter private Set<Arena> arenas = new HashSet<Arena>();

    public Game(String id, String name, GameSettings settings, Set<Arena> arenas) {
        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.name = name;
        this.settings = settings;
        this.arenas = arenas;

        this.status = GameStatus.WAITING;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("settings", settings);
        map.put("arenas", arenas.stream().map(Arena::serialize).collect(Collectors.toSet()));
        return map;
    }

    public static Game deserialize(Map<String, Object> serialized) {
        return new Game(
            (String) serialized.get("id"),
            (String) serialized.get("name"),
            (GameSettings) serialized.get("settings"),
            Stream.of(serialized.get("arenas")).map(Map.class::cast).map(s -> Arena.deserialize(s)).collect(Collectors.toSet())
        );
    }

}
