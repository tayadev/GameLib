package one.taya.gamelib.events.player;

import lombok.Getter;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class PlayerGetPointsEvent extends PlayerGameEvent {

    @Getter private int points;

    public PlayerGetPointsEvent(GamePlayer player, Game game, int points) {
        super(player, game);
    }
}
