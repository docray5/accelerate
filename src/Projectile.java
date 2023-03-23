import javafx.scene.shape.Polygon;

public class Projectile extends Character {
    public Projectile(int x, int y) {
        super(new Polygon(6, -1.5, 6, 1.5, -6, 3, -6, -3), x, y, 2, 4, AsteroidsApplication.PROJECTILE_COLOR);
    }
}
