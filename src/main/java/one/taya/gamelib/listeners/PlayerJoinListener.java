package one.taya.gamelib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.managers.PlayerManager;

public class PlayerJoinListener implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        
        // Create/Get GamePlayer
        GamePlayer player = PlayerManager.getGamePlayer(bukkitPlayer);
        player.setPlayer(bukkitPlayer);

        String nameColor = ChatColor.GRAY.toString();
        if(player.getTeam() != null) nameColor = player.getTeam().getChatColor().toString();
        if(player.getType() == GamePlayerType.MODERATOR) nameColor = ChatColor.BOLD.toString() + ChatColor.GOLD.toString();

        // handle join message
        event.setJoinMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "+" +  ChatColor.WHITE + "] " + nameColor + bukkitPlayer.getDisplayName());

        //TODO: spawn player in correct world depending on current game
    }

}
