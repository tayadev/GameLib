package one.taya.gamelib.game;

import org.bukkit.Location;

public class TeamSpawnpoint extends Spawnpoint {

    private Team team;

    public TeamSpawnpoint(Location location, Team team) {
        super(location);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
}
