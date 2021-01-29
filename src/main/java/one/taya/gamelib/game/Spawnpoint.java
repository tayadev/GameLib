package one.taya.gamelib.game;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

public class Spawnpoint {
    
    @Getter @Setter private Location location;

    public Spawnpoint(Location location) {
        this.location = location;
    }

}
