package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.utils.IdUtil;

public class Arena implements ConfigurationSerializable {
    
    @Getter private String id;
    @Getter @Setter private String name;
    @Getter private ArenaSettings settings;

    @Getter private Set<Area> areas = new HashSet<Area>();

    // Spawnpoints
    @Getter @Setter private Spawnpoint spectatorSpawnpoint;
    @Getter @Setter private Spawnpoint lobbySpawnpoint;
    @Getter private Set<Spawnpoint> spawnpoints = new HashSet<Spawnpoint>();

    private World world;

    /**
     * Represents a arena for a Game
     * @param id is used to uniquely identify this arena and to load the world with the same name
     */
    public Arena(String id, String name, ArenaSettings settings, Set<Area> areas, Spawnpoint spectatorSpawnpoint, Spawnpoint lobbySpawnpoint, Set<Spawnpoint> spawnpoints) {

        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");
        
        this.id = id;
        this.name = name;
        this.settings = settings;
        this.areas = areas;
        this.spectatorSpawnpoint = spectatorSpawnpoint;
        this.lobbySpawnpoint = lobbySpawnpoint;
        this.spawnpoints = spawnpoints;

        // Load world if needed
        if(Bukkit.getWorld(id) == null) {
            Bukkit.createWorld(new WorldCreator(id)); //TODO: make this changeable
        }

        this.world = Bukkit.getWorld(id);
        saveDefaultWorld();

    }

    public void addArea(Area area) {
        areas.add(area);
    }

    /**
     * Saves the currently loaded world to the default worlds folder
     */
    public void saveDefaultWorld() {
        // Delete current default if it exists and copy world over to defaults folder
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("settings", settings);
        map.put("areas", areas.stream().map(Area::serialize).collect(Collectors.toSet()));
        map.put("spectatorSpawnpoint", spectatorSpawnpoint);
        map.put("lobbySpawnpoint", lobbySpawnpoint);
        map.put("spawnpoints", spawnpoints.stream().map(Spawnpoint::serialize).collect(Collectors.toSet()));
        return map;
    }

    public static Arena deserialize(Map<String, Object> serialized) {
        return new Arena(
            (String) serialized.get("id"),
            (String) serialized.get("name"),
            (ArenaSettings) serialized.get("settings"),
            Stream.of(serialized.get("areas")).map(Map.class::cast).map(s -> Area.deserialize(s)).collect(Collectors.toSet()),
            (Spawnpoint) serialized.get("spectatorSpawnpoint"),
            (Spawnpoint) serialized.get("lobbySpawnpoint"),
            Stream.of(serialized.get("spawnpoints")).map(Map.class::cast).map(s -> Spawnpoint.deserialize(s)).collect(Collectors.toSet())
        );
    }

}
