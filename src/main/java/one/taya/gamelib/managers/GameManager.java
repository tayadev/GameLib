package one.taya.gamelib.managers;

import org.bukkit.Bukkit;

import one.taya.gamelib.GameLib;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class GameManager {

    public static void joinGame(Game game, Arena arena) {
        GameLib.setCurrentGame(game);
        GameLib.setCurrentArena(arena);
        GameLib.getPlayers().forEach((GamePlayer player) -> {
            if(player.getPlayer().isOnline()) {
                // TODO: add actual spawnpoint finding logic
                player.getPlayer().getPlayer().teleport(arena.getSpawnpoints().stream().findFirst().orElse(null).getLocation());
            }
        });
    }


    public static void leaveGame() {
        GameLib.setCurrentGame(null);
        GameLib.setCurrentArena(null);
        GameLib.getPlayers().forEach((GamePlayer player) -> {
            if(player.getPlayer().isOnline()) {
                player.getPlayer().getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
        });
    }

}