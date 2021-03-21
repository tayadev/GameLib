package one.taya.gamelib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.managers.PlayerManager;

public class PlayerJoinListener implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        
        // Create/Get GamePlayer
        PlayerManager.getGamePlayer(bukkitPlayer).setPlayer(bukkitPlayer);

        // handle join message
        event.setJoinMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "+" +  ChatColor.WHITE + "] " + bukkitPlayer.getDisplayName());

        //TODO: spawn player in correct world depending on current game
    }

}
