package one.taya.gamelib.utils;

public class IdUtil {
    public static boolean isValid(String id) {

        // Id cannot be empty
        if(id.isEmpty()) {
            return false;
        }

        // Id must be one word
        if(id.contains(" ")) {
            return false;
        }

        // Id must be all lowercase
        if(id.toLowerCase() != id) {
            return false;
        }

        return true;
    }
}
