import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Character {
    private final Polygon character;
    private Point2D movement;
    private boolean alive;

    public Character(Polygon polygon, int x, int y, Color color) {
        this.alive = true;
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.character.setFill(color);

        this.movement = new Point2D(0, 0);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public Polygon getCharacter() {
        return character;
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 450 * ((double) AsteroidsApplication.DELTA_TIME / 1_000_000_000L));
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 450 * ((double) AsteroidsApplication.DELTA_TIME / 1_000_000_000L));
    }

    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        // TODO Fix character out of screen
        if (this.character.getTranslateX() - this.character.getBoundsInParent().getWidth() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + AsteroidsApplication.WIDTH);
        }

        if (this.character.getTranslateX() + this.character.getBoundsInParent().getWidth() > AsteroidsApplication.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() % AsteroidsApplication.WIDTH);
        }

        if (this.character.getTranslateY() - this.character.getBoundsInParent().getHeight() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplication.HEIGHT);
        }

        if (this.character.getTranslateY() + this.character.getBoundsInParent().getHeight() > AsteroidsApplication.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() % AsteroidsApplication.HEIGHT);
        }
    }

    public void accelerate(double speed) {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= speed;
        changeX *= (double) AsteroidsApplication.DELTA_TIME / 1_000_000_000L;
        changeY *= speed;
        changeY *= (double) AsteroidsApplication.DELTA_TIME / 1_000_000_000L;

        if (this.movement.getX() + changeX > 4) return;
        if (this.movement.getX() + changeX < -4) return;
        if (this.movement.getY() + changeY > 4) return;
        if (this.movement.getY() + changeY < -4) return;

        this.movement = this.movement.add(changeX, changeY);
    }

    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
