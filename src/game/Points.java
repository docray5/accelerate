package game;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class Points {
    private final Text text;
    private final AtomicInteger points;

    public Points() {
        this.text = new Text(Game.WIDTH/2.0 - 250, Game.HEIGHT/2.0 + 20, "Points: 0");
        this.text.setFont(Font.font("Consolas", 100));
        this.text.setFill(Game.POINTS_COLOR);
        this.points = new AtomicInteger();
    }

    public void reset() {
        this.points.set(0);
        this.text.setText("Points: 0");
    }

    public int getPointsNum() {
        return this.points.get();
    }

    public Text getText() {
        return text;
    }

    public void increment() {
        increment(1);
    }

    public void increment(int amount) {
        this.text.setText("Points: " + points.addAndGet(amount));
    }
}
