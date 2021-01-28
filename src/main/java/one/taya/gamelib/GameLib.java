package one.taya.gamelib;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import net.md_5.bungee.api.ChatColor;

public class GameLib extends JavaPlugin {

	@Override
    public void onLoad() {
        CommandAPI.onLoad(true); //Load with verbose output
    }

	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		getLogger().info(ChatColor.BLUE + "Enabled GameLib by Taya");
	}

	@Override
	public void onDisable() {
		getLogger().info(ChatColor.BLUE + "Disabled GameLib by Taya");
	}

}