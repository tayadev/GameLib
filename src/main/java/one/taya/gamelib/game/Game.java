package one.taya.gamelib.game;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.enums.GameFlag;
import one.taya.gamelib.enums.GameStatus;
import one.taya.gamelib.utils.IdUtil;

public class Game {

    @Getter private String id;
    @Getter @Setter private String name;

    // The plugin registering this game
    @Getter private Plugin plugin;

    @Getter @Setter private GameStatus status;

    @Getter @Setter private int health;
    @Getter @Setter private int food;
    @Getter @Setter private GameMode gameMode;
    @Getter @Setter private Difficulty difficulty;

    @Getter @Setter private Set<GameFlag> flags = new HashSet<GameFlag>();

    @Getter private Set<Arena> arenas = new HashSet<Arena>();

    public Game(String id, String name, int health, int food, GameMode gameMode, Difficulty difficulty, Set<GameFlag> flags, Set<Arena> arenas, Plugin plugin) {
        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.name = name;

        this.plugin = plugin;

        this.health = health;
        this.food = food;
        this.gameMode = gameMode;
        this.difficulty = difficulty;

        this.flags = flags;

        this.arenas = arenas;

        this.status = GameStatus.WAITING;

        // Register game
        GameLib.addGame(this);
    }

}
