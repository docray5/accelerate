import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Character {
    private final Shape character;
    private Point2D movement;
    private boolean alive;

    public Character(Shape shape, int x, int y, Color color) {
        this.alive = true;
        this.character = shape;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.character.setFill(color);

        this.movement = new Point2D(0, 0);
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 4 * AsteroidsApplication.FPS_RATIO);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 4 * AsteroidsApplication.FPS_RATIO);
    }

    public void accelerate(double speed) {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= speed;
        changeX *= AsteroidsApplication.FPS_RATIO;
        changeY *= speed;
        changeY *= AsteroidsApplication.FPS_RATIO;

        if (this.movement.getX() + changeX > 3) return;
        if (this.movement.getX() + changeX < -3) return;
        if (this.movement.getY() + changeY > 3) return;
        if (this.movement.getY() + changeY < -3) return;

        this.movement = this.movement.add(changeX, changeY);
    }

    // Move/write character on the screen by movement variable and check if character out of screen
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

    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isNotAlive() {
        return !alive;
    }

    public Shape getCharacter() {
        return character;
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

}
