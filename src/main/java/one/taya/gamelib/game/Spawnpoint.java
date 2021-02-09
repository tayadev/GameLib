package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;

public class Spawnpoint implements ConfigurationSerializable {
    
    @Getter @Setter private Location location;

    public Spawnpoint(Location location) {
        this.location = location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("location", location);
        return map;
    }

    public static Spawnpoint deserialize(Map<String, Object> serialized) {
        return new Spawnpoint((Location) serialized.get("location"));
    }

}
