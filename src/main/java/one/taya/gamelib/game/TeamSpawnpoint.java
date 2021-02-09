package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;

public class TeamSpawnpoint extends Spawnpoint implements ConfigurationSerializable {

    @Getter @Setter private Team team;

    public TeamSpawnpoint(Location location, Team team) {
        super(location);
        this.team = team;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("location", getLocation());
        map.put("team", team);
        return map;
    }

    public static TeamSpawnpoint deserialize(Map<String, Object> serialized) {
        return new TeamSpawnpoint((Location) serialized.get("location"), (Team) serialized.get("team"));
    }
    
}
