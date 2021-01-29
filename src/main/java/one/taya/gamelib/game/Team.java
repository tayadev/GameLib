package one.taya.gamelib.game;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import one.taya.gamelib.utils.IdUtil;

public class Team {
    
    @Getter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private ChatColor chatColor;
    //private Set<GamePlayer> players = new HashSet<GamePlayer>();

    public Team(String id) {

        if(!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.name = id;
        this.chatColor = ChatColor.RESET;
    }

}
