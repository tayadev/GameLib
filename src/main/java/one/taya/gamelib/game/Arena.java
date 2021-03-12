package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.utils.IdUtil;
import one.taya.gamelib.utils.LocationUtil;

public class Arena implements ConfigurationSerializable {
    
    @Getter private String id;
    @Getter @Setter private String name;

    @Getter private Set<Area> areas = new HashSet<Area>();

    // Spawnpoints
    @Getter @Setter private Spawnpoint spectatorSpawnpoint;
    @Getter @Setter private Spawnpoint lobbySpawnpoint;
    @Getter private Set<Spawnpoint> spawnpoints = new HashSet<Spawnpoint>();
    
    // Settings
    @Getter @Setter private boolean daylightCycle;
    @Getter @Setter private int time;
    @Getter @Setter private boolean weatherCycle;
    @Getter @Setter private WeatherType weather;
    @Getter @Setter private Set<AreaFlag> flags = new HashSet<AreaFlag>();

    private World world;

    /**
     * Represents a arena for a Game
     * @param id is used to uniquely identify this arena and to load the world with the same name
     */
    public Arena(String id, String name, Set<Area> areas, Spawnpoint spectatorSpawnpoint, Spawnpoint lobbySpawnpoint, Set<Spawnpoint> spawnpoints, boolean daylightCycle, int time, boolean weatherCycle, WeatherType weather, Set<AreaFlag> flags) {

        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");
        
        this.id = id;
        this.name = name;
        this.areas = areas;
        this.spectatorSpawnpoint = spectatorSpawnpoint;
        this.lobbySpawnpoint = lobbySpawnpoint;
        this.spawnpoints = spawnpoints;
        this.daylightCycle = daylightCycle;
        this.time = time;
        this.weatherCycle = weatherCycle;
        this.weather = weather;
        this.flags = flags;

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

    public Set<Area> getAreasForLocation(Location location) {
        return getAreas()
            .stream()
            .filter((Area area) -> LocationUtil.isInArea(location, area))
            .collect(Collectors.toSet());
    }

    /**
     * Finds the highest priority area flags for the given location (returns a empty set of flags if none are found)
     * @param location
     * @return set of areaflags
     */
    public Set<AreaFlag> getFlagsForLocation(Location location) {
        Area topArea = getAreasForLocation(location)
            .stream()
            .filter((Area area) -> area.isSettingsEnabled())
            .sorted((Area a1, Area a2) -> a1.getPriority() - a2.getPriority()) // TODO: test if this is the right way around
            .findFirst().orElse(null);
        if(topArea != null) {
            return topArea.getFlags();
        } else {
            return new HashSet<AreaFlag>();
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("areas", areas.stream().map(Area::serialize).collect(Collectors.toSet()));
        map.put("spectatorSpawnpoint", spectatorSpawnpoint);
        map.put("lobbySpawnpoint", lobbySpawnpoint);
        map.put("spawnpoints", spawnpoints.stream().map(Spawnpoint::serialize).collect(Collectors.toSet()));
        map.put("daylightCycle", daylightCycle);
        map.put("time", time);
        map.put("weatherCycle", weatherCycle);
        map.put("weather", weather);
        map.put("flags", flags.stream().map(Enum::toString).collect(Collectors.toSet()));
        return map;
    }

    public static Arena deserialize(Map<String, Object> serialized) {
        return new Arena(
            (String) serialized.get("id"),
            (String) serialized.get("name"),
            Stream.of(serialized.get("areas")).map(Map.class::cast).map(s -> Area.deserialize(s)).collect(Collectors.toSet()),
            (Spawnpoint) serialized.get("spectatorSpawnpoint"),
            (Spawnpoint) serialized.get("lobbySpawnpoint"),
            Stream.of(serialized.get("spawnpoints")).map(Map.class::cast).map(s -> Spawnpoint.deserialize(s)).collect(Collectors.toSet()),
            (boolean) serialized.get("daylightCycle"),
            (int) serialized.get("time"),
            (boolean) serialized.get("weatherCycle"),
            (WeatherType) serialized.get("weather"),
            Stream.of(serialized.get("flags")).map(String.class::cast).map(f -> AreaFlag.valueOf(f)).collect(Collectors.toSet())
        );
    }

}
