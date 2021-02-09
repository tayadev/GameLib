package one.taya.gamelib.events.player;

import lombok.Getter;
import one.taya.gamelib.events.game.GameEvent;
import one.taya.gamelib.game.Game;
import one.taya.gamelib.game.GamePlayer;

public class PlayerGameEvent extends GameEvent {
    
    @Getter private GamePlayer player;

    public PlayerGameEvent(GamePlayer player, Game game) {
        super(game);
        this.player = player;
    }
}
