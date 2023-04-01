package game;

import javafx.animation.AnimationTimer;
import utils.FpsCounter;

public class GameLoop extends AnimationTimer {
    private final FpsCounter fpsCounter;
    private final Game game;

    public GameLoop() {
        this.fpsCounter = new FpsCounter();
        this.game = new Game();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void handle(long now) {
        this.fpsCounter.countAndPrint(now); // it also calculates FPS_RATIO

        // ----------------Controls-----------------
        this.game.getInputHandler().handleInput(now);

        // --------------Move-----------------
        this.game.getShip().move();
        this.game.getProjectiles().move();
        this.game.getParticles().move();
        this.game.getAsteroids().move();

        // ---------Check for collisions and lifeTime-------------
        this.game.getParticles().checkLifeTime(now);
        this.game.getProjectiles().checkLifeTime(now);

        this.game.getCollisionHandler().handleCollisions(this.getGame(), now);

        // ----------breaking asteroids into smaller pieces-------------
        this.game.getAsteroids().breakIntoSmallerPieces();

        // ---------delete unnecessary Characters-------------
        this.game.getProjectiles().removeIfNotAlive();
        this.game.getParticles().removeIfNotAlive();
        this.game.getAsteroids().removeIfNotAlive();

        // ---------Spawn new Asteroids-------------
        this.game.getAsteroids().spawnAsteroid();
    }
}
