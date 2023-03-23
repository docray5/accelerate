import javafx.scene.shape.Polygon;

public class Ship extends Character {
    public Ship(int x, int y) {
        super(new Polygon(-8, -8, 16, 0, -8, 8, -4, 0), x, y, 1.3, 2.2, AsteroidsApplication.SHIP_COLOR);
    }
}
