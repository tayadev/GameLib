package one.taya.gamelib.managers;

import org.bukkit.OfflinePlayer;

import one.taya.gamelib.GameLib;
import one.taya.gamelib.game.GamePlayer;

public class PlayerManager {
    
    public static GamePlayer getGamePlayer(OfflinePlayer player) {

        GamePlayer gamePlayer = GameLib.getPlayers().stream().filter((GamePlayer p) -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);

		if(gamePlayer != null){
			GameLib.logDebug("[PlayerManager] Added known Player " + gamePlayer.getPlayer().getName(), GameLib.getPlugin());
			return gamePlayer;
		}else{
			GamePlayer gpl = new GamePlayer(player);
			GameLib.getPlayers().add(gpl);
			GameLib.logDebug("[PlayerManager] Registered Player " + gpl.getPlayer().getName(), GameLib.getPlugin());
			return gpl;
        }
    }
}