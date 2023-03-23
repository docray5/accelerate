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
    public static Color POINTS_COLOR = Color.rgb(70, 70, 93);
    public static double ASTEROID_SPEED = 0.08;
    public static double PROJECTILE_SPEED = 0.15;
    public static double SHIP_SPEED = 0.017;

    // object Variables
    private Pane pane;
    private Text pointsText;
    private AtomicInteger points;
    private Ship ship;
    private ArrayList<Character> projectiles;
    private ArrayList<Character> asteroids;
    private ArrayList<Character> particles;
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
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) ship.turnLeft();

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) ship.turnRight();

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate(SHIP_SPEED * FPS_RATIO);
                    if (now - lastParticle > 5_000_000L) {
                        lastParticle = now;

                        //double shipRotation = Math.abs(ship.getCharacter().getRotate() - 360 * (int)(ship.getCharacter().getRotate() / 360));

                        int particleX = (int) -Math.round(Math.cos(Math.toRadians(ship.getCharacter().getRotate()))) * 7 + (int) ship.getCharacter().getBoundsInParent().getCenterX();
                        int particleY = (int) -Math.round(Math.sin(Math.toRadians(ship.getCharacter().getRotate()))) * 7 + (int) ship.getCharacter().getBoundsInParent().getCenterY();

                        createParticle(
                                particleX + new Random().nextInt(6) -3,
                                particleY + new Random().nextInt(6) -3,
                                now, Color.LIGHTSLATEGRAY, (int) -ship.getCharacter().getRotate() - 90,
                                (int) -ship.getCharacter().getRotate() + 90, 0.1, 0.2, 300_000_000L);
                    }
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 6 && now - lastShot > 250_000_000L) {
                    lastShot = now;

                    createProjectile();

                    ship.accelerate(-0.2);

                    int particleX = (int) Math.round(Math.cos(Math.toRadians(ship.getCharacter().getRotate()))) * 6 + (int) ship.getCharacter().getTranslateX();
                    int particleY = (int) Math.round(Math.sin(Math.toRadians(ship.getCharacter().getRotate()))) * 6 + (int) ship.getCharacter().getTranslateY();

                    for (int i = 0; i < 25; i++) {
                        createParticle(particleX + new Random().nextInt(10) -5,
                                particleY + new Random().nextInt(10) -5,
                                now, Color.WHITE, (int)ship.getCharacter().getRotate()-90, (int)ship.getCharacter().getRotate()+90, 0.2, 0.3, 30_000_000L);
                    }
                }

                // --------------Move-----------------
                ship.move();
                asteroids.forEach(Character::move);
                projectiles.forEach(Character::move);
                particles.forEach(Character::move);

                // ---------Check for collisions and lifeTime-------------
                for (Character particle : particles) {
                    if (now - particle.getTimeBorn() > particle.getLifeTime()) particle.setAlive(false);
                }

                projectiles.forEach(projectile -> asteroids.forEach(asteroid -> {
                    if(projectile.collide(asteroid)) {
                        projectile.setAlive(false);
                        asteroid.setAlive(false);
                        pointsText.setText("Points: " + points.addAndGet(1));

                        for (int i = 0; i < 75; i++) createParticle((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                                now, ASTEROID_COLOR, 0.2, 0.3, 100_000_000);
                        for (int i = 0; i < 75; i++) createParticle((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                                now, ASTEROID_COLOR, 0.1, 0.2, 100_000_000);
                    }
                }));

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });

                // ----------breaking asteroids into smaller pieces-------------
                ArrayList<Character> smallerAsteroids = new ArrayList<>();
                for (Character asteroid : asteroids) {
                    if (asteroid.isNotAlive() && asteroid.getTimeBorn() > 5) {
                        for (int i = 0; i < 2; i++) {
                            smallerAsteroids.add(new Asteroid((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(), (int) asteroid.getTimeBorn()/2));
                        }
                    }
                }

                for (Character asteroid : smallerAsteroids) {
                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getCharacter());
                }

                // ---------delete unnecessary Characters-------------
                removeCharacterFromListAndPane(projectiles, pane);
                removeCharacterFromListAndPane(asteroids, pane);
                removeCharacterFromListAndPane(particles, pane);

                // ---------Spawn new Asteroids-------------
                if(Math.random() < 0.0025 * FPS_RATIO) {
                    Asteroid asteroid = new Asteroid(WIDTH + 10, HEIGHT + 10, 20);
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

    public void createParticle(int x, int y, long now, Color color, int fromDegrees, int toDegrees, double minSpeed, double maxSpeed, long baseLifeTime) {
        Particle particle = new Particle(
                x, y, now, color, fromDegrees, toDegrees, minSpeed, maxSpeed, baseLifeTime);
        this.particles.add(particle);
        this.pane.getChildren().add(particle.getCharacter());
    }

    public void createParticle(int x, int y, long now, Color color, double minSpeed, double maxSpeed, long baseLifeTime) {
        createParticle(x, y, now, color, 0, 360, minSpeed, maxSpeed, baseLifeTime);
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
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT), 20);
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
}

// TODO FINALLY FIX CHARACTER OUT OF SCREEN kinda done but still have to decide on whether characters stay within the window border or go off by their size
// TODO PLEASE MAKE IT SO I CAN USE ACTUAL CLASS INSTEAD OF CHARACTER IN ARRAYLISTS like asteroids
// TODO some glow, vignette, shadows and other VFX
// TODO add glow around projectiles and a little trail
// TODO add snappy explosions of asteroids
// TODO slow the ship a little
// TODO Game over screen
// TODO Timer for how long u survived
// TODO Maybe pause menu and high score
// TODO Add pixel art
// TODO MAKE A DOCUMENT
// TODO scalable difficulty
// TODO Update color pallet
// TODO Camera shake
// TODO make three modes ARCADE MODE(the one where nothing goes off the screen), ENDLESS MODE(same as before one but no time in between asteroids spawn) AND ADVENTURE MODE(screen actually scrollable)
// TODO achievements like drifting or flying fast between asteroids
// TODO CLEAN UP AND REFACTOR SMALL
// TODO the asteroids getting smaller is a bit junk y
// TODO transparent UI (like a blur)
// TODO projectile life time
// TODO cleanup
// TODO power ups like shield that makes you bounce of a asteroid instead of killing u
// TODO add lives system
// TODO some economy system
// TODO add modifiable color palettes in the menu of the game
// TODO smoother spawning of asteroids
// TODO asteroids cant spawn INSIDE of the player
// TODO statistics for how close and how fast you flew by an asteroid
// TODO get double points for destroying asteroid after bullet teleported to other side of the screen

// Suggestions:
// TODO An object for the whole scene and stuff????
// TODO packages??
// TODO Maybe move to LibGdx