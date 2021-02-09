package one.taya.gamelib.events.team;

import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.Team;

public class TeamGetDisqualifiedEvent extends TeamGameEvent {

    public TeamGetDisqualifiedEvent(Team team, Game game) {
        super(team, game);
    }
    
}
