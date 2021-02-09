package one.taya.gamelib.game;

import org.bukkit.WeatherType;

import lombok.Getter;
import lombok.Setter;

public class ArenaSettings extends AreaSettings {
    
    @Getter @Setter private boolean daylightCycle;
    @Getter @Setter private int time;
    @Getter @Setter private boolean weatherCycle;
    @Getter @Setter private WeatherType weather;

    public ArenaSettings() {
        super();
        priority = 500;

        daylightCycle = true;
        time = 1000; // same as /time set day
        weatherCycle = true;
        weather = WeatherType.CLEAR;
    }

}
