package one.taya.gamelib.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.Location;
import org.junit.jupiter.api.Test;

import one.taya.gamelib.game.Area;
import one.taya.gamelib.game.Section;

public class LocationUtilsTest {
    
    @Test
    void isInSection() {

        Section section = new Section(new Location(null, 0, 0, 0), new Location(null, 10, 10, 10));
        Location inside = new Location(null, 5, 5, 5);
        Location outside = new Location(null, 15, 5, 5);

        assertEquals(true, LocationUtil.isInSection(inside, section));
        assertEquals(false, LocationUtil.isInSection(outside, section));
    }

    @Test
    void isInArea() {

        Section section1 = new Section(new Location(null, 0, 0, 0), new Location(null, 10, 10, 10));
        Section section2 = new Section(new Location(null, 10, 0, 0), new Location(null, 15, 5, 5));

        Area area = new Area("testArea");
        area.addSection(section1);
        area.addSection(section2);

        Location inside = new Location(null, 5, 5, 5);
        Location inside2 = new Location(null, 12, 3, 2);
        Location outside = new Location(null, 15, 8, 8);

        assertEquals(true, LocationUtil.isInArea(inside, area));
        assertEquals(true, LocationUtil.isInArea(inside2, area));
        assertEquals(false, LocationUtil.isInArea(outside, area));
    }

}
