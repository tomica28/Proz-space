package model;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;



public class SpriteTest {
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    @Test
    public void setDead() {
        Sprite test = new Sprite("tests/test.png", "test");
        test.setDead(true);
        assertTrue(test.getDead());
    }

    @Test
    public void getDead() {
        Sprite test = new Sprite("tests/test.png", "test");
        assertFalse(test.getDead());
        test.setDead(true);
        assertTrue(test.getDead());
    }

    @Test
    public void getType() {
        Sprite test = new Sprite("tests/test.png", "test");
        assertEquals(test.getType(), "test");
    }

    @Test
    public void setLives() {
        Sprite test = new Sprite("tests/test.png", "test");
        test.setLives(3);
        assertEquals(test.getLives(), 3);
    }

    @Test
    public void getLives() {
        Sprite test = new Sprite("tests/test.png", "test");
        assertEquals(test.getLives(), 1);
        test.setLives(3);
        assertEquals(test.getLives(), 3);
    }
}