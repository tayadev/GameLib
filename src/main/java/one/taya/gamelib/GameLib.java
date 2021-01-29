package one.taya.gamelib;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class GameLib extends JavaPlugin {

	@Getter private static Plugin plugin;
	private static boolean debug;

	@Override
    public void onLoad() {
        CommandAPI.onLoad(true); //Load with verbose output
    }

	@Override
	public void onEnable() {
		plugin = this;

		CommandAPI.onEnable(this);

		// Debug mode TODO: load from config
		debug = true;
        if(debug) logInfo(ChatColor.YELLOW + "Debug logging enabled");

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

}