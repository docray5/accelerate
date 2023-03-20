import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Particle extends Character {
    private final long timeBorn;
    private final long lifeTime;

    public Particle(int x, int y, long timeBorn, Color color, int direction) {
        super(new Circle(new Random().nextInt(2, 6)), x, y, color);
        super.getCharacter().setRotate(new Random().nextInt(direction-90, direction+90));
        this.timeBorn = timeBorn;
        super.accelerate(new Random().nextDouble(0.1, 0.2));
        this.lifeTime = new Random().nextInt(7) * 100_000_000 + 900_000_000L;
    }

    public long getTimeBorn() {
        return timeBorn;
    }

    public long getLifeTime() {
        return lifeTime;
    }
}
