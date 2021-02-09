package one.taya.gamelib.utils;

import org.bukkit.Location;

import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.Section;

public class LocationUtil {

    public static boolean isInArea(Location location, Area area) {
        for(Section section : area.getSections()) {
            if(isInSection(location, section)) return true;
        }

        return false;
    }

    public static boolean isInSection(Location location, Section section) {
        Location corner1 = section.getCorner1();
        Location corner2 = section.getCorner2();

        return corner1.getX() < location.getX() && location.getX() < corner2.getX() &&
                corner1.getY() < location.getY() && location.getY() < corner2.getY() &&
                corner1.getZ() < location.getZ() && location.getZ() < corner2.getZ();
    }
}
