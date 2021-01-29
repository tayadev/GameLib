package one.taya.gamelib.game;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.GameStatus;
import one.taya.gamelib.utils.IdUtil;

public class Game {
    
    @Getter private String id;
    @Getter @Setter private String name;

    @Getter @Setter private GameStatus status;
    @Getter private GameSettings settings;

    //private Set<GameArena> arenas = new HashSet<GameArena>();

    public Game(String id) {

        if(!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.name = id;

        this.status = GameStatus.WAITING;
        //this.settings = GameSettings.default;
    }

}
