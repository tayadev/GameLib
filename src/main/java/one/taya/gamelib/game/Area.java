package one.taya.gamelib.game;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.enums.AreaFlag;
import one.taya.gamelib.utils.IdUtil;

public class Area {
    
    @Getter private String id;
    @Getter private Set<Section> sections = new HashSet<Section>();
    @Getter @Setter private boolean settingsEnabled;
    @Getter @Setter private int priority;
    @Getter @Setter private Set<AreaFlag> flags = new HashSet<AreaFlag>();

    public Area(String id, Set<Section> sections, boolean settingsEnabled, int priority, Set<AreaFlag> flags) {
        
        if(!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.sections = sections;
        this.settingsEnabled = settingsEnabled;
        this.priority = priority;
        this.flags = flags;

    }

    public Area(String id) {
        this(id, new HashSet<Section>(), false, 0, new HashSet<AreaFlag>());
    }

    public void addSection(Section section) {
        sections.add(section);
    }

}