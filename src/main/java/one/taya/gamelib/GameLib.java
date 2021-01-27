package one.taya.gamelib;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class GameLib extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info(ChatColor.BLUE + "Enabled GameLib by Taya");
	}

	@Override
	public void onDisable() {
		getLogger().info(ChatColor.BLUE + "Disabled GameLib by Taya");
	}

}