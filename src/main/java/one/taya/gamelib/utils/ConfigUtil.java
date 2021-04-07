package one.taya.gamelib.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.enums.GameFlag;
import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.game.Section;
import one.taya.gamelib.game.Spawnpoint;
import one.taya.gamelib.game.Team;
import one.taya.gamelib.game.TeamSpawnpoint;
import one.taya.gamelib.managers.PlayerManager;

// TODO: saving to config

public class ConfigUtil {

    // GameLib
    
    public static Set<Team> loadTeams() {
        Configuration config = GameLib.getPlugin().getConfig();
        ConfigurationSection section = config.getConfigurationSection("teams");
        return section
            .getKeys(false)
            .stream()
            .map((String key) -> {
                return section.getConfigurationSection(key);
            })
            .map((ConfigurationSection sectionConfigurationSection) -> {
                return deserializeTeam(sectionConfigurationSection);
            })
            .collect(Collectors.toSet());
    }

    public static void saveTeams() {
        Configuration config = GameLib.getPlugin().getConfig();

        // Clear current data
        if(config.isConfigurationSection("teams")) {
            config.set("teams", null);
        } else {
            config.createSection("teams");
        }
        ConfigurationSection section = config.getConfigurationSection("teams");

        for(Team team : GameLib.getTeams()) {
            ConfigurationSection teamSection = section.createSection(team.getId());
            teamSection.set("name", team.getName());
            teamSection.set("chatColor", serializeChatColor(team.getChatColor()));
            teamSection.set("players", team.getPlayers().stream().map(GamePlayer::getPlayer).map(OfflinePlayer::getName).collect(Collectors.toList()));
        }

        GameLib.getPlugin().saveConfig();
    }

    static Team deserializeTeam(ConfigurationSection configurationSection) {
        String id = configurationSection.getName();
        String name = deserializeName(configurationSection.get("name"));
        ChatColor chatColor = deserializeChatColor(configurationSection.get("chatColor"));
        Set<GamePlayer> players = deserializeGamePlayers(configurationSection.getStringList("players"));

        return new Team(id, name, chatColor, players);
    }

    static ChatColor deserializeChatColor(Object serialized) {
        String value = (String) serialized;
        return ChatColor.of(value.toUpperCase());
    }

    static String serializeChatColor(ChatColor color) {
        return color.getName();
    }

    static Set<GamePlayer> deserializeGamePlayers(List<String> names) {
        return names.stream().map(ConfigUtil::deserializeGamePlayer).collect(Collectors.toSet());
    }

    static GamePlayer deserializeGamePlayer(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return PlayerManager.getGamePlayer(player);
    }

    // Game

    public static Game loadGame(Configuration config, String gameId, Plugin plugin) {
        ConfigurationSection section = config.getConfigurationSection(gameId);
        String name = deserializeName(section.get("name"));
        int health = (int) section.get("health");
        int food = (int) section.get("food");
        GameMode gameMode = deserializeGameMode(section.get("gamemode"));
        Difficulty difficulty = deserializeDifficulty(section.get("difficulty"));
        Set<GameFlag> flags = deserializeGameFlags(section.getStringList("flags"));
        Set<Arena> arenas = deserializeArenas(section.getConfigurationSection("arenas"));
        
        return new Game(gameId, name, health, food, gameMode, difficulty, flags, arenas, plugin);
    }

    public static void saveGame(Game game) {
        Configuration config = game.getPlugin().getConfig();
   
        // Clear current data
        String id = game.getId();
        if(config.isConfigurationSection(id)) {
            config.set(id, null);
        } else {
            config.createSection(id);
        }
        ConfigurationSection configSection = config.getConfigurationSection(id);
        configSection.set("name", game.getName());
        configSection.set("health", game.getHealth());
        configSection.set("food", game.getFood());
        configSection.set("gamemode", game.getGameMode().toString());
        configSection.set("difficulty", game.getDifficulty().toString());
        configSection.set("flags", game.getFlags().stream().map(GameFlag::toString).collect(Collectors.toList()));

        ConfigurationSection arenasSection = configSection.createSection("arenas");
        for(Arena arena : game.getArenas()) {
            ConfigurationSection arenaSection = arenasSection.createSection(arena.getId());
            arenaSection.set("name", game.getName());

            ConfigurationSection areasSection = configSection.createSection("areas");
            for(Area area : arena.getAreas()) {
                ConfigurationSection areaSection = areasSection.createSection(area.getId());

                ConfigurationSection sectionsSection = areaSection.createSection("sections");
                for(Section section : area.getSections()) {
                    ConfigurationSection sectionSection = sectionsSection.createSection(section.getId());
                    sectionSection.set("corner1", serializeLocation(section.getCorner1()));
                    sectionSection.set("corner2", serializeLocation(section.getCorner2()));
                }

                areaSection.set("settingsEnabled", area.isSettingsEnabled());
                areaSection.set("priority", area.getPriority());
                areaSection.set("flags", area.getFlags().stream().map(AreaFlag::toString).collect(Collectors.toList()));
            }

            arenaSection.set("spectatorSpawnpoint", serializeSpawnpoint(arena.getSpectatorSpawnpoint()));
            arenaSection.set("spawnpoints", arena.getSpawnpoints().stream().map(ConfigUtil::serializeSpawnpoint).collect(Collectors.toList()));
            arenaSection.set("daylightCycle", arena.isDaylightCycle());
            arenaSection.set("time", arena.getTime());
            arenaSection.set("weatherCycle", arena.isWeatherCycle());
            arenaSection.set("weather", arena.getWeather().toString());
            arenaSection.set("flags", arena.getFlags().stream().map(AreaFlag::toString).collect(Collectors.toList()));
        }
    }

    static String serializeLocation(Location location) {
        return location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
    }

    static Object serializeSpawnpoint(Spawnpoint spawnpoint) {
        if(spawnpoint.getClass() == TeamSpawnpoint.class) {
            TeamSpawnpoint teamSpawnpoint = (TeamSpawnpoint) spawnpoint;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("location", serializeLocation(teamSpawnpoint.getLocation()));
            map.put("team", teamSpawnpoint.getTeam().getId());
            return map;
        } else {
            return serializeLocation(spawnpoint.getLocation());
        }
    }

    static Arena deserializeArena(ConfigurationSection configurationSection) {
        String id = configurationSection.getName();
        String name = deserializeName(configurationSection.get("name"));
        Set<Area> areas = deserializeAreas(configurationSection.getConfigurationSection("areas"), id);
        Spawnpoint spectatorSpawnpoint = new Spawnpoint(deserializeLocation(configurationSection.get("spectatorSpawnpoint"), id));
        Set<Spawnpoint> spawnpoints = deserializeSpawnpoints(configurationSection.getList("spawnpoints"), id);
        Boolean daylightCycle = (Boolean) configurationSection.get("daylightCycle");
        int time = (int) configurationSection.get("time");
        Boolean weatherCycle = (Boolean) configurationSection.get("weatherCycle");
        WeatherType weather = deserializeWeather(configurationSection.get("weather"));
        Set<AreaFlag> flags = deserializeAreaFlags(configurationSection.getStringList("flags"));

        return new Arena(id, name, areas, spectatorSpawnpoint, spawnpoints, daylightCycle, time,
                weatherCycle, weather, flags);
    }

    static Area deserializeArea(ConfigurationSection configurationSection, String worldId) {

        String id = configurationSection.getName();
        Set<Section> sections = deserializeSections(configurationSection.getConfigurationSection("sections"), worldId);
        Boolean settingsEnabled = (Boolean) configurationSection.get("settingsEnabled");
        int priority = (int) configurationSection.get("priority");
        Set<AreaFlag> flags = deserializeAreaFlags(configurationSection.getStringList("flags"));

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

    static GameFlag deserializeGameFlag(String value) {
        return GameFlag.valueOf(value.toUpperCase());
    }

    static Set<GameFlag> deserializeGameFlags(List<String> serialized) {
        return serialized.stream().map(ConfigUtil::deserializeGameFlag).collect(Collectors.toSet());
    }

    static AreaFlag deserializeAreaFlag(String value) {
        return AreaFlag.valueOf(value.toUpperCase());
    }

    static Set<AreaFlag> deserializeAreaFlags(List<String> serialized) {
        return serialized.stream().map(ConfigUtil::deserializeAreaFlag).collect(Collectors.toSet());
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
        String value = (String) serialized;
        return WeatherType.valueOf(value.toUpperCase());
    }

    static GameMode deserializeGameMode(Object serialized) {
        String value = (String) serialized;
        return GameMode.valueOf(value.toUpperCase());
    }

    static Difficulty deserializeDifficulty(Object serialized) {
        String value = (String) serialized;
        return Difficulty.valueOf(value.toUpperCase());
    }

    static Set<Spawnpoint> deserializeSpawnpoints(List<?> spawnpoints, String worldId) {
        return spawnpoints
            .stream()
            .map((Object spawnpoint) -> {
                return deserializeSpawnpoint(spawnpoint, worldId);
            })
            .collect(Collectors.toSet());
    }

    static Spawnpoint deserializeSpawnpoint(Object serialized, String worldId) {
        Spawnpoint spawnpoint;

        if(serialized.getClass() != String.class) {
            Map<String, Object> map = (Map<String, Object>) serialized;
            Location location = deserializeLocation(map.get("location"), worldId);
            Team team = deserializeTeamFromId(map.get("team"));
            spawnpoint = new TeamSpawnpoint(location, team);
        } else {
            Location location = deserializeLocation(serialized, worldId);
            spawnpoint = new Spawnpoint(location);
        }

        return spawnpoint;
    }

    static Team deserializeTeamFromId(Object serialized) {
        String teamId = (String) serialized;
        Team team = GameLib.getTeams().stream().filter((Team t) -> {
            return t.getId().equals(teamId);
        }).findAny().get();
        return team;
    }

    static String deserializeName(Object serialized) {
        String value = (String) serialized;
        return ChatColor.translateAlternateColorCodes('&', value);
    }

}
