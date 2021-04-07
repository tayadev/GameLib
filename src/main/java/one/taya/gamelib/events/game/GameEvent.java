package one.taya.gamelib.events.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.game.Game;

public class GameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter @Setter private boolean isCancelled;

    @Getter private Game game;

    public GameEvent(Game game) {
        this.isCancelled = false;
        this.game = game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
