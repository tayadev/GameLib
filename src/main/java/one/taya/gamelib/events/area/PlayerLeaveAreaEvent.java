package one.taya.gamelib.events.area;

import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.GamePlayer;

public class PlayerLeaveAreaEvent extends AreaEvent {

    public PlayerLeaveAreaEvent(GamePlayer player, Area area) {
        super(area, player);
    }
    
}
