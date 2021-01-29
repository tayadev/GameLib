package one.taya.gamelib.game;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.GameLib;
import one.taya.gamelib.GameManager;
import one.taya.gamelib.utils.IdUtil;

public class GameArena {
    
    @Getter private String id;
    @Getter @Setter private String name;

    // Spawnpoints
    @Getter @Setter private Spawnpoint spectatorSpawnpoint;
    @Getter @Setter private Spawnpoint lobbySpawnpoint;
    @Getter private Set<Spawnpoint> spawnpoints = new HashSet<Spawnpoint>();

    private World world;


    /**
     * Represents a arena for a Game
     * @param id is used to uniquely identify this arena and to load the world with the same name
     */
    public GameArena(String id) {

        if (!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");
        
        this.id = id;
        this.name = id;

        // Load world if needed
        if(Bukkit.getWorld(id) == null) {
            Bukkit.createWorld(new WorldCreator(id)); //TODO: make this changeable
        }

        this.world = Bukkit.getWorld(id);
        saveDefaultWorld();

    }

    /**
     * Saves the currently loaded world to the default worlds folder
     */
    public void saveDefaultWorld() {

        // Delete current default if it exists and copy world over to defaults folder
    }
}
