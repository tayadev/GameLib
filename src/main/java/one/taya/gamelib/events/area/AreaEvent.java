package one.taya.gamelib.events.area;

import lombok.Getter;
import one.taya.gamelib.events.player.PlayerGameEvent;
import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.GamePlayer;

public class AreaEvent extends PlayerGameEvent {

    @Getter private Area area;

    public AreaEvent(Area area, GamePlayer player) {
        super(player, player.getGame());
        this.area = area;
    }
    
}
