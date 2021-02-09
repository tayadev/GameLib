package one.taya.gamelib.events.team;

import lombok.Getter;
import one.taya.gamelib.events.game.GameEvent;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.Team;

public class TeamGameEvent extends GameEvent {
    
    @Getter private Team team;

    public TeamGameEvent(Team team, Game game) {
        super(game);
        this.team = team;
    }

}
