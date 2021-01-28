package one.taya.gamelib.game;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;

public class Team {
    
    private String id;
    private String name;
    private ChatColor chatColor;
    private Set<GamePlayer> players = new HashSet<GamePlayer>();

    public Team(String id) {
        
    }

}
