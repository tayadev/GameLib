package one.taya.gamelib.game;

public class Game {
    
    private String id;
    private String name;

    //private GameStatus status;
    //private GameSettings settings;

    //private Set<GameArena> arenas = new HashSet<GameArena>();

    public Game(String id) {
        this.id = id;
        this.name = id;

        //this.status = GameStatus.WAITING;
        //this.settings = GameSettings.default;
    }

}
