package one.taya.gamelib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.managers.PlayerManager;

public class PlayerQuitListener implements Listener {
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player bukkitPlayer = event.getPlayer();

        GamePlayer player = PlayerManager.getGamePlayer(bukkitPlayer);
        String nameColor = ChatColor.GRAY.toString();
        if(player.getTeam() != null) nameColor = player.getTeam().getChatColor().toString();
        if(player.getType() == GamePlayerType.MODERATOR) nameColor = ChatColor.BOLD.toString() + ChatColor.GOLD.toString();

        // handle join message
        event.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" +  ChatColor.GRAY + "] " + nameColor + bukkitPlayer.getDisplayName());
    }

}
