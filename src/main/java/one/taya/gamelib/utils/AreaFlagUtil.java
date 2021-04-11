package one.taya.gamelib.utils;

import one.taya.gamelib.enums.AreaFlag;

public class AreaFlagUtil {

    private static final String negativePrefix = "NO_";

    public static AreaFlag getInverseAreaFlag(AreaFlag areaFlag) {
        String name = areaFlag.name();

        return AreaFlag.valueOf(
            name.startsWith(negativePrefix)
                ? name.replaceFirst(negativePrefix, "")
                : negativePrefix + name
        );
    }
}