package entities;

import game.Game;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class Character {
    private final Shape character;
    private Point2D movement;
    private boolean alive;
    private final double maxSpeed;
    private final double turningSpeed;
    private final long timeBorn;
    private final long lifeTime;

    public Character(Shape shape, int x, int y, double maxSpeed, double turningSpeed, Color color, long timeBorn, long lifeTime) {
        this.alive = true;
        this.character = shape;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.character.setFill(color);

        this.maxSpeed = maxSpeed;
        this.turningSpeed = turningSpeed;

        this.timeBorn = timeBorn;
        this.lifeTime = lifeTime;

        this.movement = new Point2D(0, 0);
    }

    public Character(Shape shape, int x, int y, double maxSpeed, double turningSpeed, Color color) {
        this(shape, x, y, maxSpeed, turningSpeed, color, 0, 0);
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - turningSpeed * Game.FPS_RATIO);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + turningSpeed * Game.FPS_RATIO);
    }

    /**
     * @param speed
     * accelerate in the direction that the character is looking
     */
    public void accelerate(double speed) {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= speed;
        changeX *= Game.FPS_RATIO;
        changeY *= speed;
        changeY *= Game.FPS_RATIO;

        if (this.movement.getX() + changeX > maxSpeed) return;
        if (this.movement.getX() + changeX < -maxSpeed) return;
        if (this.movement.getY() + changeY > maxSpeed) return;
        if (this.movement.getY() + changeY < -maxSpeed) return;

        this.movement = this.movement.add(changeX, changeY);
    }

    /**
     * Move/write character on the screen by movement variable and check if character is within the window border
     */
    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        if (this.character.getTranslateX() + this.character.getBoundsInParent().getWidth()/4 < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + Game.WIDTH);
        }

        if (this.character.getTranslateX() - this.character.getBoundsInParent().getWidth()/4 > Game.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() % Game.WIDTH);
        }

        if (this.character.getTranslateY() + this.character.getBoundsInParent().getHeight()/4 < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + Game.HEIGHT);
        }

        if (this.character.getTranslateY() - this.character.getBoundsInParent().getHeight()/4 > Game.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() % Game.HEIGHT);
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

    public long getTimeBorn() {
        return timeBorn;
    }

    public long getLifeTime() {
        return lifeTime;
    }
}
