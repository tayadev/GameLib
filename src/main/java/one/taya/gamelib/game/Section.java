package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;

public class Section implements ConfigurationSerializable {
    
    @Getter private Location corner1;
    @Getter private Location corner2;

    public Section(Location corner1, Location corner2) {
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

	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corner1", corner1);
        map.put("corner2", corner2);
        return map;
    }
    
    public static Section deserialize(Map<String, Object> serialized) {
        return new Section(
            (Location) serialized.get("corner1"),
            (Location) serialized.get("corner2")
            );
    }

}
