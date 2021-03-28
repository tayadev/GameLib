package one.taya.gamelib.game;

import org.bukkit.OfflinePlayer;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.GamePlayerType;

public class GamePlayer {
    
    @Getter @Setter private OfflinePlayer player;
    @Getter @Setter private Team team;
    @Getter @Setter private GamePlayerType type;

    public GamePlayer(OfflinePlayer player, Team team, GamePlayerType type) {
        this.player = player;
        this.team = team;
        this.type = type;
    }

    public GamePlayer(OfflinePlayer player) {
        this.player = player;
        this.type = GamePlayerType.SPECTATOR;
    }

}
