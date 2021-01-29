package one.taya.gamelib;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import one.taya.gamelib.game.Game;

public class GameManager {
    
    @Getter private Set<Game> games = new HashSet<Game>();

}
