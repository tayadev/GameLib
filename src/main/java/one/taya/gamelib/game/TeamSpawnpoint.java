package one.taya.gamelib.game;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

public class TeamSpawnpoint extends Spawnpoint { // We do not need to implement ConfigurationSerializable, because Spawnpoint already implements it

    @Getter @Setter private Team team;

    public TeamSpawnpoint(Location location, Team team) {
        super(location);
        this.team = team;
    }

}
