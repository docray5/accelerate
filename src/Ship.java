import javafx.scene.shape.Polygon;

public class Ship extends Character {
    public Ship(int x, int y) {
        super(new Polygon(-8, -8, 16, 0, -8, 8), x, y, AsteroidsApplication.SHIP_COLOR);
    }
}
