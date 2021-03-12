package one.taya.gamelib.listeners;

import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.managers.PlayerManager;

public class GeneralListener implements Listener  {
    //TODO: find a better name
    // Listens to a lot of events to cancel them if needed to enforce settings

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamelib.bypass.building")) return;

        Set<AreaFlag> flags = PlayerManager.getGamePlayer(player).getArena().getFlagsForLocation(event.getBlock().getLocation());
        if(!flags.contains(AreaFlag.BUILDING)) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamelib.bypass.breaking")) return;

        Set<AreaFlag> flags = PlayerManager.getGamePlayer(player).getArena().getFlagsForLocation(event.getBlock().getLocation());
        if(!flags.contains(AreaFlag.BREAKING)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamelib.bypass.dropping")) return;

        Set<AreaFlag> flags = PlayerManager.getGamePlayer(player).getArena().getFlagsForLocation(player.getLocation());
        if(!flags.contains(AreaFlag.DROPPING)) event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if(entity.getType() != EntityType.PLAYER) return;

        Player player = (Player) entity;
        if(player.hasPermission("gamelib.bypass.pickup")) return;

        Set<AreaFlag> flags = PlayerManager.getGamePlayer(player).getArena().getFlagsForLocation(player.getLocation());
        if(!flags.contains(AreaFlag.PICKUP)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamelib.bypass.durability_change")) return;

        Set<AreaFlag> flags = PlayerManager.getGamePlayer(player).getArena().getFlagsForLocation(player.getLocation());
        if(!flags.contains(AreaFlag.DURABILITY_CHANGE)) event.setCancelled(true);
    }
    
    // HEALTH, FOOD, PVP, PLAYER_COLLISION, INVENTORY_CHANGE, BLOCK_DROPS, MOB_SPAWNING
}
