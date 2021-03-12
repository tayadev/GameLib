package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.GamePlayerType;

public class GamePlayer implements ConfigurationSerializable {
    
    @Getter @Setter private OfflinePlayer player;
    @Getter @Setter private Team team;
    @Getter @Setter private GamePlayerType type;
    @Getter @Setter private Game game;
    @Getter @Setter private Arena arena;

    public GamePlayer(OfflinePlayer player, Team team, GamePlayerType type) {
        this.player = player;
        this.team = team;
        this.type = type;
    }

    public GamePlayer(OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("player", player);
        map.put("team", team);
        map.put("type", type);
        return map;
    }

    public static GamePlayer deserialize(Map<String, Object> serialized) {
        return new GamePlayer(
            (OfflinePlayer) serialized.get("player"),
            (Team) serialized.get("team"),
            (GamePlayerType) serialized.get("type")
        );
    }

}
