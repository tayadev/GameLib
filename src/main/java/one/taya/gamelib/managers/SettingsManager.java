package one.taya.gamelib.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.AreaSettings;
import one.taya.gamelib.game.Arena;
import one.taya.gamelib.utils.LocationUtil;

public class SettingsManager {
    
    public static Set<AreaSettings> getSettingsForLocation(Arena arena, Location location) {
        Set<AreaSettings> settings = new HashSet<AreaSettings>();
        
        for(Area area : arena.getAreas()) {
            if(area.isSettingsEnabled()) {
                if(LocationUtil.isInArea(location, area)) {
                    settings.add(area.getSettings());
                }
            }
        }

        return settings;
    }

}