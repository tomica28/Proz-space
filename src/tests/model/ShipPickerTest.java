package model;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShipPickerTest {

    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    @Test
    public void getShip() {
        ShipPicker test = new ShipPicker(SHIP.BLUE);
        assertEquals(SHIP.BLUE, test.getShip());
    }

    @Test
    public void getIsCircleChoosen() {
        ShipPicker test = new ShipPicker(SHIP.BLUE);
        test.setIsCircleChoosen(true);
        assertTrue(test.getIsCircleChoosen());
        test.setIsCircleChoosen(false);
        assertFalse(test.getIsCircleChoosen());
    }

    @Test
    public void setIsCircleChoosen() {
        ShipPicker test = new ShipPicker(SHIP.BLUE);
        test.setIsCircleChoosen(true);
        assertTrue(test.getIsCircleChoosen());
    }
}