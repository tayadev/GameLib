package one.taya.gamelib.events.player;

import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class PlayerLeaveGameEvent extends PlayerGameEvent {
    public PlayerLeaveGameEvent(GamePlayer player, Game game) {
        super(player, game);
    }
}
