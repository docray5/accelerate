import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Particle extends Character {
    private final long timeBorn;
    private final long lifeTime;

    public Particle(int x, int y, long timeBorn, Color color, int fromDegrees, int toDegrees, double minSpeed, double maxSpeed, long baseLifeTime) {
        super(new Circle(new Random().nextInt(2, 6)), x, y, color);
        super.getCharacter().setRotate(new Random().nextInt(fromDegrees, toDegrees));
        this.timeBorn = timeBorn;
        super.accelerate(new Random().nextDouble(minSpeed, maxSpeed));
        this.lifeTime = new Random().nextInt(7) * 100_000_000 + baseLifeTime;
    }

    public long getTimeBorn() {
        return timeBorn;
    }

    public long getLifeTime() {
        return lifeTime;
    }
}
