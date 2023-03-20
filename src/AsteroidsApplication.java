import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidsApplication extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static double FPS_RATIO = 1;
    public static Color BACKGROUND = Color.rgb(57, 57, 74);
    public static Color ASTEROID_COLOR = Color.rgb(224, 75, 103);
    public static Color SHIP_COLOR = Color.rgb(70, 196, 195);
    // when adding thruster color to ship change ship to be light green grass and thruster to be the current ship color
    public static Color PROJECTILE_COLOR = Color.rgb(202, 132, 39);
    public static Color POINTS_COLOR = Color.rgb(245, 233, 207);
    public static double ASTEROID_SPEED = 0.08;
    public static double PROJECTILE_SPEED = 0.15;
    public static double SHIP_SPEED = 0.03;

    // object Variables
    private Pane pane;
    private Text pointsText;
    private AtomicInteger points;
    private Ship ship;
    private ArrayList<Character> projectiles;
    private ArrayList<Character> asteroids;
    private ArrayList<Particle> particles;
    private Map<KeyCode, Boolean> pressedKeys;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        this.initialize();

        new AnimationTimer() {
            // Variables for fps counter
            private long lastSecond = 0;
            private long frameCount = 0;
            private long lastShot = 0;
            private long lastParticle = 0;

            @Override
            public void handle(long now) {
                // -----------Simple fps counter------------
                if (now - lastSecond > 1_000_000_000L) {
                    double fps = ((double) frameCount / (now - lastSecond)) * 1_000_000_000L; // type conversion is slow
                    System.out.println(fps);

                    // -------------Calc FPS ratio--------------
                    // works kinda like delta time, but we just calculate the ratio instead
                    if (fps != 0.0) FPS_RATIO = 143 / fps;

                    lastSecond = now;
                    frameCount = 0;
                    //System.out.println("Total memory: " + Runtime.getRuntime().totalMemory()/1024 + ", Free memory: " + Runtime.getRuntime().freeMemory()/1024 + ", Diff: " + (Runtime.getRuntime().totalMemory()/1024 - Runtime.getRuntime().freeMemory()/1024));
                }
                frameCount++;

                // ----------------Controls-----------------
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) ship.turnLeft();

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) ship.turnRight();

                if(pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate(SHIP_SPEED * FPS_RATIO);
                    if (now - lastParticle > 5_000_000L) {
                        lastParticle = now;
                        createParticle(
                                (int) ship.getCharacter().getTranslateX() + 4,
                                (int) ship.getCharacter().getTranslateY() - 4,
                                now, Color.AQUAMARINE, (int) -ship.getCharacter().getRotate(),
                                0.1, 0.2, 700_000_000L);
                    }
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3 && now - lastShot > 1_000_000_000L) {
                    lastShot = now;

                    createProjectile();

                    ship.accelerate(-0.2);

                    for (int i = 0; i < 25; i++) {
                        createParticle((int)ship.getCharacter().getTranslateX()+4 + new Random().nextInt(10) -5,
                                (int)ship.getCharacter().getTranslateY() + new Random().nextInt(10) -5,
                                now, Color.WHITE, (int)ship.getCharacter().getRotate(), 0.2, 0.3, 30_000_000L);
                    }
                }

                // --------------Move-----------------
                ship.move();
                asteroids.forEach(Character::move);
                projectiles.forEach(Character::move);
                particles.forEach(Character::move);

                // ---------Check for collisions and lifeTime-------------
                for (Particle particle : particles) {
                    if (now - particle.getTimeBorn() > particle.getLifeTime()) particle.setAlive(false);
                }

                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });

                    if(projectile.isNotAlive()) {
                        pointsText.setText("Points: " + points.addAndGet(1));
                    }
                });

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });

                // ---------delete unnecessary Characters-------------
                removeCharacterFromListAndPane(projectiles, pane);
                removeCharacterFromListAndPane(asteroids, pane);
                removeParticleFromListAndPane(particles, pane);

                // ---------Spawn new Asteroids-------------
                if(Math.random() < 0.005 * FPS_RATIO) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    public void createProjectile() {
        Projectile projectile = new Projectile((int) this.ship.getCharacter().getTranslateX(),
                (int) this.ship.getCharacter().getTranslateY());
        projectile.getCharacter().setRotate(this.ship.getCharacter().getRotate());

        projectile.accelerate(PROJECTILE_SPEED);
        projectile.setMovement(projectile.getMovement().normalize().multiply(3 * FPS_RATIO));

        this.projectiles.add(projectile);
        this.pane.getChildren().add(projectile.getCharacter());
    }

    public void createParticle(int x, int y, long now, Color color, int direction, double minSpeed, double maxSpeed, long baseLifeTime) {
        Particle particle = new Particle(
                x + new Random().nextInt(10) -5,
                y + new Random().nextInt(10) -5,
                now, color, direction, minSpeed, maxSpeed, baseLifeTime);
        this.particles.add(particle);
        this.pane.getChildren().add(particle.getCharacter());
    }

    public void initialize() {
        this.pane = new Pane();
        this.pane.setPrefSize(WIDTH, HEIGHT);
        this.pane.setBackground(Background.fill(BACKGROUND));

        this.pointsText = new Text(WIDTH/2.0 - 250, HEIGHT/2.0 + 20, "Points: 0");
        this.pointsText.setFont(Font.font("Consolas", 100));
        this.pointsText.setFill(POINTS_COLOR);
        this.pane.getChildren().add(this.pointsText);
        this.points = new AtomicInteger();

        this.ship = new Ship(WIDTH/2, HEIGHT/2);
        this.pane.getChildren().add(this.ship.getCharacter());

        this.projectiles = new ArrayList<>();

        this.asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT));
            this.asteroids.add(asteroid);
        }
        this.asteroids.forEach(asteroid -> this.pane.getChildren().add(asteroid.getCharacter()));

        this.particles = new ArrayList<>();

        this.scene = new Scene(this.pane);

        this.pressedKeys = new HashMap<>();

        this.scene.setOnKeyPressed(event -> this.pressedKeys.put(event.getCode(), Boolean.TRUE));
        this.scene.setOnKeyReleased(event -> this.pressedKeys.put(event.getCode(), Boolean.FALSE));
    }

    // TODO this could be improved
    public static void removeCharacterFromListAndPane(ArrayList<Character> list, Pane pane) {
        list.stream()
                .filter(Character::isNotAlive)
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        list.removeAll(list.stream()
                .filter(Character::isNotAlive)
                .toList());
    }

    public static void removeParticleFromListAndPane(ArrayList<Particle> list, Pane pane) {
        list.stream()
                .filter(Particle::isNotAlive)
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        list.removeAll(list.stream()
                .filter(Particle::isNotAlive)
                .toList());
    }
}

// TODO add particles
// TODO some glow, vignette, shadows and other VFX
// TODO Game over screen
// TODO Timer for how long u survived
// TODO Maybe pause menu and high score
// TODO make bullets disappear after they leave the screen
// TODO Thruster
// TODO Add pixel art
// TODO MAKE A DOCUMENT
// TODO scalable difficulty
// TODO Update color pallet
// TODO Camera shake
// TODO make screen actually scrollable?
// TODO achievements like drifting or flying fast between asteroids

// Suggestions:
// TODO An object for the whole scene and stuff????
// TODO packages??
// TODO arrayList for all Characters in the game?
// TODO Maybe move to LibGdx