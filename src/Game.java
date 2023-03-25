import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Random;

public class Game {
    // -------Config variables for the game-------------
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static double FPS_RATIO = 1;
    public static Color BACKGROUND = Color.rgb(57, 57, 74);
    public static Color ASTEROID_COLOR = Color.rgb(224, 75, 103);
    public static Color SHIP_COLOR = Color.rgb(70, 196, 195);
    // when adding thruster color to ship change ship to be light green grass and thruster to be the current ship color
    public static Color PROJECTILE_COLOR = Color.rgb(202, 132, 39);
    public static Color POINTS_COLOR = Color.rgb(70, 70, 93);
    public static double ASTEROID_SPEED = 0.08;
    public static double PROJECTILE_SPEED = 0.15;
    public static double SHIP_SPEED = 0.017;

    // -----------Class variables-------------
    private Scene scene;
    private Pane pane;
    private Points points;
    private Ship ship;
    private InputHandler inputHandler;
    private Particles particles;
    private Projectiles projectiles;
    private Asteroids asteroids;
    private CollisionHandler collisionHandler;

    public Game() {
        this.initialize();
    }

    private void initialize() {
        this.pane = new Pane();
        this.pane.setPrefSize(WIDTH, HEIGHT);
        this.pane.setBackground(Background.fill(BACKGROUND));

        this.particles = new Particles(this.pane);

        this.projectiles = new Projectiles(this.pane, this.particles);

        this.points = new Points();
        this.ship = new Ship(WIDTH/2, HEIGHT/2, this.particles, this.projectiles);
        this.pane.getChildren().add(this.points.getText());
        this.pane.getChildren().add(this.ship.getCharacter());

        this.asteroids = new Asteroids(this.pane, this.ship);
        // spawn in a few asteroids at the start
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            this.asteroids.addAsteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT), 20);
        }

        this.scene = new Scene(this.pane);

        this.collisionHandler = new CollisionHandler(particles, ship, projectiles, asteroids);

        this.inputHandler = new InputHandler(this.ship);
        this.scene.setOnKeyPressed(event -> this.inputHandler.addInput(event.getCode(), Boolean.TRUE));
        this.scene.setOnKeyReleased(event -> this.inputHandler.addInput(event.getCode(), Boolean.FALSE));
    }

    public void gameOver() {
        System.out.println("Over!");
    }

    public Scene getScene() {
        return this.scene;
    }

    public Points getPoints() {
        return points;
    }

    public Ship getShip() {
        return ship;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public Particles getParticles() {
        return particles;
    }

    public Projectiles getProjectiles() {
        return projectiles;
    }

    public Asteroids getAsteroids() {
        return asteroids;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }
}
