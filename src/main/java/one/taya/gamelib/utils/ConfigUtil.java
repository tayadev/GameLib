package one.taya.gamelib.utils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.enums.GameFlag;
import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.Section;
import one.taya.gamelib.game.Spawnpoint;

// TODO: spawnpoints, plugin | test it! | saving to config

public class ConfigUtil {
    public Game loadGame(Configuration config, String gameId) {

        ConfigurationSection section = config.getConfigurationSection(gameId);
        String name = (String) section.get("name");
        int health = (int) section.get("health");
        int food = (int) section.get("food");
        GameMode gameMode = deserializeGameMode(config.get("gamemode"));
        Difficulty difficulty = deserializeDifficulty(config.get("difficulty"));
        Set<GameFlag> flags = deserializeGameFlags(config.get("flags"));
        Set<Arena> arenas = deserializeArenas(config.getConfigurationSection("arenas"));
        
        return new Game(gameId, name, health, food, gameMode, difficulty, flags, arenas, plugin);
    }

    static Arena deserializeArena(ConfigurationSection configurationSection) {
        String id = configurationSection.getName();
        String name = (String) configurationSection.get("name");
        Set<Area> areas = deserializeAreas(configurationSection.getConfigurationSection("areas"), id);
        Spawnpoint spectatorSpawnpoint = new Spawnpoint(deserializeLocation(configurationSection.get("spectatorSpawnpoint"), id));
        Set<Spawnpoint> spawnpoints = deserializeSpawnpoints(configurationSection.getConfigurationSection("spawnpoints"), id);
        Boolean daylightCycle = (Boolean) configurationSection.get("daylightCycle");
        int time = (int) configurationSection.get("time");
        Boolean weatherCycle = (Boolean) configurationSection.get("weatherCycle");
        WeatherType weather = deserializeWeather(configurationSection.get("weather"));
        Set<AreaFlag> flags = deserializeAreaFlags(configurationSection.get("flags"));

        return new Arena(id, name, areas, spectatorSpawnpoint, spawnpoints, daylightCycle, time,
                weatherCycle, weather, flags);
    }

    static Area deserializeArea(ConfigurationSection configurationSection, String worldId) {

        String id = configurationSection.getName();
        Set<Section> sections = deserializeSections(configurationSection.getConfigurationSection("sections"), worldId);
        Boolean settingsEnabled = (Boolean) configurationSection.get("settingsEnabled");
        int priority = (int) configurationSection.get("priority");
        Set<AreaFlag> flags = deserializeAreaFlags(configurationSection.get("flags"));

        return new Area(id, sections, settingsEnabled, priority, flags);
    }

    
    static Section deserializeSection(ConfigurationSection configurationSection, String worldId) {
        String id = configurationSection.getName();
        Location corner1 = deserializeLocation(configurationSection.get("corner1"), worldId);
        Location corner2 = deserializeLocation(configurationSection.get("corner2"), worldId);
        return new Section(id, corner1, corner2);
    }

    static Location deserializeLocation(Object serialized, String worldId) {
        String value = (String) serialized;
        String[] split = value.split(" ");

        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);
        float yaw = split.length > 3 ? Float.parseFloat(split[3]) : 0.0f;
        float pitch = split.length > 3 ? Float.parseFloat(split[4]) : 0.0f;

        return new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
    }

    static GameFlag deserializeGameFlag(Object serialized) {
        return GameFlag.valueOf((String) serialized);
    }

    static Set<GameFlag> deserializeGameFlags(Object serialized) {
        return Stream.of(serialized).map(ConfigUtil::deserializeGameFlag).collect(Collectors.toSet());
    }

    static AreaFlag deserializeAreaFlag(Object serialized) {
        return AreaFlag.valueOf((String) serialized);
    }

    static Set<AreaFlag> deserializeAreaFlags(Object serialized) {
        return Stream.of(serialized).map(ConfigUtil::deserializeAreaFlag).collect(Collectors.toSet());
    }

    static Set<Section> deserializeSections(ConfigurationSection configurationSection, String worldId) {
        return configurationSection
            .getKeys(false)
            .stream()
            .map((String key) -> {
                return configurationSection.getConfigurationSection(key);
            })
            .map((ConfigurationSection sectionConfigurationSection) -> {
                return deserializeSection(sectionConfigurationSection, worldId);
            })
            .collect(Collectors.toSet());
    }

    static Set<Area> deserializeAreas(ConfigurationSection configurationSection, String worldId) {
        return configurationSection
            .getKeys(false)
            .stream()
            .map((String key) -> {
                return configurationSection.getConfigurationSection(key);
            })
            .map((ConfigurationSection sectionConfigurationSection) -> {
                return deserializeArea(sectionConfigurationSection, worldId);
            })
            .collect(Collectors.toSet());
    }

    static Set<Arena> deserializeArenas(ConfigurationSection configurationSection) {
        return configurationSection
            .getKeys(false)
            .stream()
            .map((String key) -> {
                return configurationSection.getConfigurationSection(key);
            })
            .map((ConfigurationSection sectionConfigurationSection) -> {
                return deserializeArena(sectionConfigurationSection);
            })
            .collect(Collectors.toSet());
    }

    static WeatherType deserializeWeather(Object serialized) {
        return WeatherType.valueOf((String) serialized);
    }

    static GameMode deserializeGameMode(Object serialized) {
        return GameMode.valueOf((String) serialized);
    }

    static Difficulty deserializeDifficulty(Object serialized) {
        return Difficulty.valueOf((String) serialized);
    }

}
