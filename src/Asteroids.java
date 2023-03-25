import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Asteroids {
    private final ArrayList<Asteroid> asteroids;
    private final Pane pane;
    private final Ship ship;

    public Asteroids(Pane pane, Ship ship) {
        this.asteroids = new ArrayList<>();
        this.pane = pane;
        this.ship = ship;
    }

    public void move() {
        this.asteroids.forEach(Asteroid::move);
    }

    public ArrayList<Asteroid> getList() {
        return asteroids;
    }

    public void removeIfNotAlive() {
        this.asteroids.stream()
                .filter(Character::isNotAlive)
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        this.asteroids.removeAll(this.asteroids.stream()
                .filter(Character::isNotAlive)
                .toList());
    }

    public void spawnAsteroid() {
        if(Math.random() < 0.0025 * Game.FPS_RATIO) {
            addAsteroid(Game.WIDTH + 10, Game.HEIGHT + 10, 20);
        }
    }

    public void addAsteroid(int x, int y, int size) {
        Asteroid asteroid = new Asteroid(x, y, size);
        if (!asteroid.collide(ship)) {
            this.asteroids.add(asteroid);
            this.pane.getChildren().add(asteroid.getCharacter());
        }
    }

    public void breakIntoSmallerPieces() {
        ArrayList<int[]> newAsteroids = new ArrayList<>();
        this.asteroids.forEach(asteroid -> {
            if (asteroid.isNotAlive() && asteroid.getSize() > 5) {
                newAsteroids.add(new int[]{(int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(), asteroid.getSize() / 2});
            }
        });

        for (int[] arr : newAsteroids) {
            this.addAsteroid(arr[0], arr[1], arr[2]);
        }
    }
}
