package one.taya.gamelib.game;

import org.bukkit.Location;

public class Spawnpoint {
    
    private Location location;

    public Spawnpoint(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
