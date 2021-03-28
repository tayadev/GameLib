package one.taya.gamelib;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.commands.GameLibCommand;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.game.Team;
import one.taya.gamelib.listeners.PlayerJoinListener;
import one.taya.gamelib.listeners.PlayerQuitListener;
import one.taya.gamelib.managers.GameManager;
import one.taya.gamelib.managers.PlayerManager;
import one.taya.gamelib.utils.ConfigUtil;

public class GameLib extends JavaPlugin {

	@Getter private static Plugin plugin;
	private static boolean debug;

	@Getter private static GameManager gameManager;
	@Getter private static PlayerManager playerManager;

	@Getter private static Set<Game> games = new HashSet<Game>();
    @Getter private static Set<Team> teams = new HashSet<Team>();
	@Getter private static Set<GamePlayer> players = new HashSet<GamePlayer>();
	
	@Getter private static String chatPrefix = "[" + ChatColor.DARK_PURPLE + "GameLib" + ChatColor.RESET + "] ";

    @Getter @Setter private static Game currentGame;
    @Getter @Setter private static Arena currentArena;

	@Override
    public void onLoad() {
		CommandAPI.onLoad(false);
		new GameLibCommand();
    }

	@Override
	public void onEnable() {
		plugin = this;

		CommandAPI.onEnable(this);

        // Config
        File configFile = new File(this.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            saveDefaultConfig();
        }
        teams = ConfigUtil.loadTeams();
        for(Team team : teams) {
            for(GamePlayer player : team.getPlayers()) {
                player.setTeam(team);
                player.setType(GamePlayerType.PLAYER);
            }
        }

        // Debug Mode
		debug = getConfig().getBoolean("debug", false);
        if(debug) logInfo(ChatColor.YELLOW + "Debug logging enabled");

		// Register Listeners
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);

		getLogger().info(ChatColor.BLUE + "Enabled GameLib by Taya");
	}

	@Override
	public void onDisable() {
		getLogger().info(ChatColor.BLUE + "Disabled GameLib by Taya");
	}

	public static void logInfo(String message) {
        System.out.println("[GameLib] [INFO] " + message);
    }
    
    public static void logDebug(String message, Plugin plugin) {
        if(debug) System.out.println("[GameLib]" + " [" + plugin.getName() + "]" + " [DEBUG] " + message);
	}

	public static void addGame(Game game) {
		games.add(game);
		logDebug("Registered game " + game.getName(), getPlugin());
	}

}
