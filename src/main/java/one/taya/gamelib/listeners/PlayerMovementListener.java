package one.taya.gamelib.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.GamePlayerType;
import one.taya.gamelib.events.area.AreaEvent;
import one.taya.gamelib.events.area.PlayerEnterAreaEvent;
import one.taya.gamelib.events.area.PlayerLeaveAreaEvent;
import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.GamePlayer;
import one.taya.gamelib.utils.LocationUtil;

public class PlayerMovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(GameLib.getCurrentGame() == null) return;
        GamePlayer player = GameLib.getPlayers().stream().filter((GamePlayer gp) -> {return gp.getPlayer().getPlayer() == event.getPlayer();}).findFirst().get();
        if(player.getType() != GamePlayerType.PLAYER) return;
        if(event.getFrom().equals(event.getTo())) return;

        for(Area area : GameLib.getCurrentArena().getAreas()) {
            Location from = event.getFrom();
            Location to = event.getTo();
            boolean fromIsIn = false;
            boolean toIsIn = false;
            if(LocationUtil.isInArea(from, area)) {
                fromIsIn = true;
            }
            if(LocationUtil.isInArea(to, area)) {
                toIsIn = true;
            }
            AreaEvent areaEvent = null;
            if(fromIsIn && ! toIsIn) {
                areaEvent = new PlayerLeaveAreaEvent(player, area);
            } else if(!fromIsIn && toIsIn) {
                areaEvent = new PlayerEnterAreaEvent(player, area);
            }
            if(areaEvent != null) {
                Bukkit.getPluginManager().callEvent(areaEvent);
                event.setCancelled(areaEvent.isCancelled());
            }
        }
    }

}
