package one.taya.gamelib.events.area;

import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.GamePlayer;

public class PlayerEnterAreaEvent extends AreaEvent {

    public PlayerEnterAreaEvent(GamePlayer player, Area area) {
        super(area, player);
    }
    
}
