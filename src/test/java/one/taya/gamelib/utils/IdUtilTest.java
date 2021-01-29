package one.taya.gamelib.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IdUtilTest {
    
    @Test
    void notEmpty() {
        assertEquals(true, IdUtil.isValid("foobar"));
        assertEquals(false, IdUtil.isValid(""));
    }

    @Test
    void noSpaces() {
        assertEquals(true, IdUtil.isValid("foobar"));
        assertEquals(false, IdUtil.isValid("foo bar"));
    }

    @Test
    void noUppercase() {
        assertEquals(true, IdUtil.isValid("foobar"));
        assertEquals(false, IdUtil.isValid("Foobar"));
    }

}
