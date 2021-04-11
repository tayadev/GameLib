package one.taya.gamelib.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.utils.AreaFlagUtil;
import one.taya.gamelib.utils.IdUtil;
import one.taya.gamelib.utils.LocationUtil;

public class Arena {
    
    @Getter private String id;
    @Getter @Setter private String name;

    @Getter private Set<Area> areas = new HashSet<Area>();

    // Spawnpoints
    @Getter @Setter private Spawnpoint spectatorSpawnpoint;
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
    public Arena(String id, String name, Set<Area> areas, Spawnpoint spectatorSpawnpoint, Set<Spawnpoint> spawnpoints, boolean daylightCycle, int time, boolean weatherCycle, WeatherType weather, Set<AreaFlag> flags) {

        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");
        
        this.id = id;
        this.name = name;
        this.areas = areas;
        this.spectatorSpawnpoint = spectatorSpawnpoint;
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

        // Update Locations to reference this world
        this.areas.forEach((Area a) -> {
            a.getSections().forEach((Section s) -> {
                s.getCorner1().setWorld(this.world);
                s.getCorner2().setWorld(this.world);
            });
        });
        this.spectatorSpawnpoint.getLocation().setWorld(this.world);
        this.spawnpoints.forEach((Spawnpoint s) -> {
            s.getLocation().setWorld(this.world);
        });

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
        
        LinkedHashSet<Area> sortedAreas = new LinkedHashSet<Area>(
            getAreasForLocation(location)
                .stream()
                .sorted((Area a1, Area a2) -> a1.getPriority() - a2.getPriority())
                .collect(Collectors.toList())
        );
         
        // The areas should now be sorted least to most important
        List<Set<AreaFlag>> listOfFlagSets = new ArrayList<Set<AreaFlag>>();
        listOfFlagSets.add(getFlags());
        for(Area area : sortedAreas) {
            if(area.isSettingsEnabled()) {
                listOfFlagSets.add(area.getFlags());
            }
        }
        
        Set<AreaFlag> flags = new HashSet<AreaFlag>();
        for(Set<AreaFlag> flagSet : listOfFlagSets) {
            for(AreaFlag flag : flagSet) {
                flags.add(flag);
                flags.remove(AreaFlagUtil.getInverseAreaFlag(flag));
            }
        }

        return flags;
    }

}
