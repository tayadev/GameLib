package one.taya.gamelib.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;
import one.taya.gamelib.utils.IdUtil;

public class Area implements ConfigurationSerializable {
    
    @Getter private String id;
    @Getter private Set<Section> sections = new HashSet<Section>();
    @Getter @Setter private boolean settingsEnabled = false;
    @Getter private AreaSettings settings;

    public Area(String id, Set<Section> sections, boolean settingsEnabled, AreaSettings settings) {
        
        if(!IdUtil.isValid(id)) throw new IllegalArgumentException("Invalid id");

        this.id = id;
        this.sections = sections;
        this.settingsEnabled = settingsEnabled;
        this.settings = settings;

    }

    public Area(String id) {
        this(id, new HashSet<Section>(), false, new AreaSettings());
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("sections", sections.stream().map(Section::serialize).collect(Collectors.toSet()));
        map.put("settingsEnabled", settingsEnabled);
        map.put("settings", settings);
        return map;
    }

    public static Area deserialize(Map<String, Object> serialized) {
        return new Area(
            (String) serialized.get("id"),
            Stream.of(serialized.get("sections")).map(Map.class::cast).map(s -> Section.deserialize(s)).collect(Collectors.toSet()),
            (boolean) serialized.get("settingsEnabled"),
            (AreaSettings) serialized.get("settings")
            );
    }

}