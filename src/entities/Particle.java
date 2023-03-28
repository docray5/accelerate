package entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Particle extends Character {
    public Particle(int x, int y, long timeBorn, Color color, int fromDegrees, int toDegrees, double minSpeed, double maxSpeed, long baseLifeTime) {
        super(new Circle(new Random().nextInt(2, 6)), x, y, 2, 4, color, timeBorn, new Random().nextInt(7) * 100_000_000 + baseLifeTime);
        super.getCharacter().setRotate(new Random().nextInt(fromDegrees, toDegrees));
        super.accelerate(new Random().nextDouble(minSpeed, maxSpeed));
    }
}
