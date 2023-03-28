package logic;

import entities.Ship;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;


public class InputHandler {
    private final Map<KeyCode, Boolean> pressedKeys;
    private final Ship ship;

    public InputHandler(Ship ship) {
        this.pressedKeys = new HashMap<>();
        this.ship = ship;
    }

    public void addInput(KeyCode key, Boolean state) {
        this.pressedKeys.put(key, state);
    }

    public void handleInput(long now) {
        if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) this.ship.turnLeft();
        if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) this.ship.turnRight();
        if (pressedKeys.getOrDefault(KeyCode.UP, false)) this.ship.accel(now);
        if (pressedKeys.getOrDefault(KeyCode.SPACE, false)) this.ship.shoot(now);
    }
}
