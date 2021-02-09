package one.taya.gamelib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerQuitListener implements Listener {
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player bukkitPlayer = event.getPlayer();

        // handle join message
        event.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" +  ChatColor.GRAY + "] " +bukkitPlayer.getDisplayName());
    }

}
