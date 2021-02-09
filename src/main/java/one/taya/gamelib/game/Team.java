package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.utils.IdUtil;

public class Team implements ConfigurationSerializable {
    
    @Getter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private ChatColor chatColor;
    @Getter private Set<GamePlayer> players = new HashSet<GamePlayer>();

    public Team(String id, String name, ChatColor chatColor, Set<GamePlayer> players) {

        if(!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.name = name;
        this.chatColor = chatColor;
        this.players = players;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("chatColor", chatColor);
        map.put("players", players.stream().map(GamePlayer::serialize).collect(Collectors.toSet()));
        return map;
    }

    public static Team deserialize(Map<String, Object> serialized) {
        return new Team(
            (String) serialized.get("id"),
            (String) serialized.get("name"),
            (ChatColor) serialized.get("chatColor"),
            Stream.of(serialized.get("players")).map(Map.class::cast).map(s -> GamePlayer.deserialize(s)).collect(Collectors.toSet())
            );
    }

}
