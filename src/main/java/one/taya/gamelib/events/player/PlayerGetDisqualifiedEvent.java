package one.taya.gamelib.events.player;

import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class PlayerGetDisqualifiedEvent extends PlayerGameEvent {
    public PlayerGetDisqualifiedEvent(GamePlayer player, Game game) {
        super(player, game);
    }
}
