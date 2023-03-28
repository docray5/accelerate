package game;

import entities.Asteroids;
import entities.Particles;
import entities.Projectiles;
import entities.Ship;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logic.CollisionHandler;
import logic.InputHandler;

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
    private Pane gamePane;
    private Points points;
    private Ship ship;
    private InputHandler inputHandler;
    private Particles particles;
    private Projectiles projectiles;
    private Asteroids asteroids;
    private CollisionHandler collisionHandler;
    private GameOverWindow gameOverWindow;

    // TODO maybe change it so each class has one parameter, Game game and from there we can get all the panes and other parameters

    public Game() {
        this.initialize();
    }

    private void initialize() {
        StackPane root = new StackPane();
        // For a shadow effect when on game over window Put different color
        root.setBackground(Background.fill(BACKGROUND));

        this.gamePane = new Pane();
        this.gamePane.setPrefSize(WIDTH, HEIGHT);
        this.gamePane.setBackground(Background.fill(BACKGROUND));

        this.particles = new Particles(this.gamePane);

        this.projectiles = new Projectiles(this.gamePane, this.particles);

        this.points = new Points();
        this.ship = new Ship(WIDTH/2, HEIGHT/2, this.particles, this.projectiles);
        this.gamePane.getChildren().add(this.points.getText());
        this.gamePane.getChildren().add(this.ship.getCharacter());

        this.asteroids = new Asteroids(this.gamePane, this.ship);
        spawnAsteroids();

        this.gameOverWindow = new GameOverWindow(this);
        root.getChildren().addAll(this.gamePane, this.gameOverWindow.getView());
        this.scene = new Scene(root);
        this.scene.getStylesheets().add("style.css");

        this.collisionHandler = new CollisionHandler(particles, ship, projectiles, asteroids);

        this.inputHandler = new InputHandler(this.ship);
        this.scene.setOnKeyPressed(event -> this.inputHandler.addInput(event.getCode(), Boolean.TRUE));
        this.scene.setOnKeyReleased(event -> this.inputHandler.addInput(event.getCode(), Boolean.FALSE));
    }

    public void gameOver() {
        this.gamePane.setEffect(new GaussianBlur(30));
        this.gameOverWindow.display();
        this.inputHandler = new InputHandler(this.ship);
        System.out.println("Over!");
    }

    public void restart() {
        System.out.println("restart");
        this.gameOverWindow.hide();
        this.gamePane.setEffect(null);
        this.particles.clear();
        this.projectiles.clear();
        this.asteroids.clear();
        this.points.reset();
        this.gamePane.getChildren().setAll(this.points.getText(), this.ship.getCharacter());
        this.ship.appear();
        spawnAsteroids();
    }

    private void spawnAsteroids() { // TODO add variety
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            this.asteroids.addAsteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT), 20);
        }
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
