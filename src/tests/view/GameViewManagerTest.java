package view;

import model.JavaFXThreadingRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameViewManagerTest {

    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
    
    @Test
    public void getLose() {
        GameViewManager test = new GameViewManager();
        assertFalse(test.getLose());
    }

    @Test
    public void getScore() {
        GameViewManager test = new GameViewManager();
        assertEquals(test.getScore(), 0);
    }
}