package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class AreaSettings implements ConfigurationSerializable {

    private Optional<Integer> priority;
    private Optional<Boolean> pvp;
    private Optional<Boolean> health;
    private Optional<Boolean> food;
    private Optional<Boolean> allowBuilding;
    private Optional<Boolean> allowBreaking;
    private Optional<Boolean> allowInteract;
    private Optional<Boolean> allowDropping;
    private Optional<Boolean> allowPickup;
    private Optional<Boolean> allowDurabilityChange;
    private Optional<Boolean> playerCollision;
    private Optional<Boolean> allowInventoryChange;
    private Optional<Boolean> blockDrops;
    private Optional<Boolean> mobSpawning;

    public AreaSettings(int priority, boolean pvp, boolean health, boolean food, boolean allowBuilding,
            boolean allowBreaking, boolean allowInteract, boolean allowDropping, boolean allowPickup,
            boolean allowDurabilityChange, boolean playerCollision, boolean allowInventoryChange, boolean blockDrops,
            boolean mobSpawning) {
        this.priority = Optional.of(priority);
        this.pvp = Optional.of(pvp);
        this.health = Optional.of(health);
        this.food = Optional.of(food);
        this.allowBuilding = Optional.of(allowBuilding);
        this.allowBreaking = Optional.of(allowBreaking);
        this.allowInteract = Optional.of(allowInteract);
        this.allowDropping = Optional.of(allowDropping);
        this.allowPickup = Optional.of(allowPickup);
        this.allowDurabilityChange = Optional.of(allowDurabilityChange);
        this.playerCollision = Optional.of(playerCollision);
        this.allowInventoryChange = Optional.of(allowInventoryChange);
        this.blockDrops = Optional.of(blockDrops);
        this.mobSpawning = Optional.of(mobSpawning);
    }

    public AreaSettings() {
        this.priority = Optional.empty();
        this.pvp = Optional.empty();
        this.health = Optional.empty();
        this.food = Optional.empty();
        this.allowBuilding = Optional.empty();
        this.allowBreaking = Optional.empty();
        this.allowInteract = Optional.empty();
        this.allowDropping = Optional.empty();
        this.allowPickup = Optional.empty();
        this.allowDurabilityChange = Optional.empty();
        this.playerCollision = Optional.empty();
        this.allowInventoryChange = Optional.empty();
        this.blockDrops = Optional.empty();
        this.mobSpawning = Optional.empty();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("priority", priority);
        map.put("pvp", pvp);
        map.put("health", health);
        map.put("food", food);
        map.put("allowBuilding", allowBuilding);
        map.put("allowBreaking", allowBreaking);
        map.put("allowInteract", allowInteract);
        map.put("allowDropping", allowDropping);
        map.put("allowPickup", allowPickup);
        map.put("allowDurabilityChange", allowDurabilityChange);
        map.put("playerCollision", playerCollision);
        map.put("allowInventoryChange", allowInventoryChange);
        map.put("blockDrops", blockDrops);
        map.put("mobSpawning", mobSpawning);
        return map;
    }

    public static AreaSettings deserialize(Map<String, Object> serialized) {
        return new AreaSettings(
            (int) serialized.get("priority"),
            (boolean) serialized.get("pvp"),
            (boolean) serialized.get("health"),
            (boolean) serialized.get("food"),
            (boolean) serialized.get("allowBuilding"),
            (boolean) serialized.get("allowBreaking"),
            (boolean) serialized.get("allowInteract"),
            (boolean) serialized.get("allowDropping"),
            (boolean) serialized.get("allowPickup"),
            (boolean) serialized.get("allowDurabilityChange"),
            (boolean) serialized.get("playerCollision"),
            (boolean) serialized.get("allowInventoryChange"),
            (boolean) serialized.get("blockDrops"),
            (boolean) serialized.get("mobSpawning")
            );
    }

}
