package one.taya.gamelib.events.team;

import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.Team;

public class TeamWinEvent extends TeamGameEvent {
    
    public TeamWinEvent(Team team, Game game) {
        super(team, game);
    }

}
