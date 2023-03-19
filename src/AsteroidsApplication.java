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

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
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

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        pane.setBackground(Background.fill(BACKGROUND));

        // TODO this text on top
        Text text = new Text(10, 35, "Points: 0");
        text.setFont(Font.font("Verdana", 36));
        text.setFill(POINTS_COLOR);
        pane.getChildren().add(text);
        AtomicInteger points = new AtomicInteger();

        Ship ship = new Ship(WIDTH/2, HEIGHT/2);
        pane.getChildren().add(ship.getCharacter());

        ArrayList<Character> projectiles = new ArrayList<>();

        ArrayList<Character> asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

        Scene scene = new Scene(pane);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), Boolean.TRUE));
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), Boolean.FALSE));

        new AnimationTimer() {
            // Variables for fps counter
            private long lastSecond = 0;
            private long frameCount = 0;
            private long lastShot = 0;

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
                }
                frameCount++;

                // ----------------Controls-----------------
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) ship.turnLeft();

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) ship.turnRight();

                if(pressedKeys.getOrDefault(KeyCode.UP, false)) ship.accelerate(SHIP_SPEED * FPS_RATIO);

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3 && now - lastShot > 1_000_000_000L) {
                    lastShot = now;
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate(PROJECTILE_SPEED);
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3 * FPS_RATIO));

                    pane.getChildren().add(projectile.getCharacter());
                }

                // --------------Move-----------------
                ship.move();
                asteroids.forEach(Character::move);
                projectiles.forEach(Character::move);

                // ---------Check for collisions-------------
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });

                    if(projectile.isNotAlive()) {
                        text.setText("Points: " + points.addAndGet(1));
                    }
                });

                removeCharacterFromListAndPane(projectiles, pane);
                removeCharacterFromListAndPane(asteroids, pane);

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });

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
        WIDTH = 0;
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

// TODO add particles
// TODO some glow, vignette, shadows and other VFX
// TODO Game over screen
// TODO Maybe pause menu
// TODO make bullets disappear after they leave the screen