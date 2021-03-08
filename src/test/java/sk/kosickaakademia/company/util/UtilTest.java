package sk.kosickaakademia.company.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.kosickaakademia.company.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    private Util util;

    @BeforeEach
    void setUp() {
        util = new Util();
    }

    @Test
    void normalizeName() {
        assertEquals("", util.normalizeName(null));
        assertEquals("", util.normalizeName(""));
        assertEquals("", util.normalizeName("  "));
        assertEquals("Peter", util.normalizeName(" peTeR  "));
        assertEquals("Uhorcy", util.normalizeName("UHORCY"));
        assertEquals("Se kali", util.normalizeName(" se kalI"));
        assertEquals("*9sak", util.normalizeName("*9sAk"));
    }
}