package one.taya.gamelib.events.player;

import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class PlayerWinGameEvent extends PlayerGameEvent {
    public PlayerWinGameEvent(GamePlayer player, Game game) {
        super(player, game);
    }
}
