package one.taya.gamelib.listeners;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.enums.GameStatus;

public class GeneralListener implements Listener  {
    //TODO: find a better name
    // Listens to a lot of events to cancel them if needed to enforce settings

    boolean skip(String permission, Player player) {
        return GameLib.getCurrentGame().getStatus() != GameStatus.INGAME || player.hasPermission("gamelib.bypass." + permission);
    }

    boolean isFlagActiveForPlayer(AreaFlag flag, Player player, Location location) {
        Set<AreaFlag> flags = GameLib.getCurrentArena().getFlagsForLocation(location);
        return !flags.contains(flag);
    }
    
    boolean isFlagActiveForPlayer(AreaFlag flag, Player player) {
        Set<AreaFlag> flags = GameLib.getCurrentArena().getFlagsForLocation(player.getLocation());
        return !flags.contains(flag);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(skip("building", player)) return;
        if(isFlagActiveForPlayer(AreaFlag.BUILDING, player, event.getBlock().getLocation())) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(skip("breaking", player)) return;
        if(isFlagActiveForPlayer(AreaFlag.BREAKING, player, event.getBlock().getLocation())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(skip("dropping", player)) return;
        if(isFlagActiveForPlayer(AreaFlag.DROPPING, player)) event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if(entity.getType() != EntityType.PLAYER) return;
        Player player = (Player) entity;

        if(skip("pickup", player)) return;
        if(isFlagActiveForPlayer(AreaFlag.PICKUP, player)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        if(skip("durability_change", player)) return;
        if(isFlagActiveForPlayer(AreaFlag.DURABILITY_CHANGE, player)) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(skip("inventory_change", player)) return;
        if(isFlagActiveForPlayer(AreaFlag.INVENTORY_CHANGE, player, event.getInventory().getLocation())) event.setCancelled(true);
    }

    // HEALTH, FOOD, PVP, PLAYER_COLLISION, BLOCK_DROPS, MOB_SPAWNING
}
