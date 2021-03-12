package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.utils.IdUtil;
import one.taya.gamelib.utils.ParticleUtil;

public class Section implements ConfigurationSerializable {
   
    @Getter private String id;
    @Getter private Location corner1;
    @Getter private Location corner2;

    public Section(String id, Location corner1, Location corner2) {
        
        if(!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");
        this.id = id;

        this.corner1 = new Location(
            corner1.getWorld(),
            Math.min(corner1.getX(), corner2.getX()),
            Math.min(corner1.getY(), corner2.getY()),
            Math.min(corner1.getZ(), corner2.getZ())
        );

        this.corner2 = new Location(
            corner1.getWorld(),
            Math.max(corner1.getX(), corner2.getX()),
            Math.max(corner1.getY(), corner2.getY()),
            Math.max(corner1.getZ(), corner2.getZ())
        );
    }

    public void visualize(Player player) {
        Section section = this;
        new BukkitRunnable() {
            int counter = 10;
            @Override
            public void run() {
                if(counter != 0) {
                    ParticleUtil.drawBox(section.corner1, section.corner2, 0.1, player);
                    counter--;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(GameLib.getPlugin(), 0, 10);
    }

	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("corner1", corner1);
        map.put("corner2", corner2);
        return map;
    }
    
    public static Section deserialize(Map<String, Object> serialized) {
        return new Section(
            (String) serialized.get("id"),
            (Location) serialized.get("corner1"),
            (Location) serialized.get("corner2")
            );
    }

}
