package one.taya.gamelib.game;

import java.util.Optional;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import lombok.Getter;
import lombok.Setter;

public class GameSettings extends ArenaSettings {
    
    @Getter @Setter private boolean teamWin;
    @Getter @Setter private boolean teamDisqualification;
    @Getter @Setter private boolean allowRejoining;
    @Getter @Setter private boolean chat;
    @Getter @Setter private boolean teamChat;
    @Getter @Setter private boolean allowGlobalChat;
    @Getter @Setter private boolean friendlyFire;
    @Getter @Setter private int healthLevel;
    @Getter @Setter private int foodLevel;
    @Getter @Setter private GameMode gameMode;
    @Getter @Setter private Difficulty difficulty;

    @Getter @Setter private boolean allowBuilding;
    @Getter @Setter private boolean allowBreaking;
    @Getter @Setter private boolean allowPvp;
    @Getter @Setter private boolean allowPve;
    @Getter @Setter private boolean allowInventoryChange;
    @Getter @Setter private boolean allowInteract;
    @Getter @Setter private boolean allowDropping;
    @Getter @Setter private boolean allowPickup;
    @Getter @Setter private boolean allowDurabilityChange;
    @Getter @Setter private boolean allowCollision;
    @Getter @Setter private boolean allowBlockDrop;
    @Getter @Setter private boolean allowMobSpawn;
    
    @Getter @Setter private boolean resetWorld;
    
    public GameSettings() {
        super();
        priority = Optional.of(1000);

        teamWin = false;
        teamDisqualification = false;
        allowRejoining = false;
        chat = true;
        teamChat = false;
        allowGlobalChat = false;
        friendlyFire = false;
        healthLevel = 20;
        foodLevel = 20;
        gameMode = GameMode.SURVIVAL;
        difficulty = Difficulty.NORMAL;

        allowBuilding = true;
        allowBreaking = true;
        allowPvp = true;
        allowPve = true;
        allowInventoryChange = true;
        allowInteract = true;
        allowDropping = true;
        allowPickup = true;
        allowDurabilityChange = true;
        allowCollision = true;
        allowBlockDrop = true;
        allowMobSpawn = true;

        resetWorld = true;
    }

}
