package logic;

import entities.Asteroids;
import entities.Particles;
import entities.Projectiles;
import entities.Ship;
import game.Game;
import game.Points;

public class CollisionHandler {
    private final Particles particles;
    private final Ship ship;
    private final Projectiles projectiles;
    private final Asteroids asteroids;

    public CollisionHandler(Particles particles, Ship ship, Projectiles projectiles, Asteroids asteroids) {
        this.particles = particles;
        this.ship = ship;
        this.projectiles = projectiles;
        this.asteroids = asteroids;
    }

    public void handleCollisions(Game game, long now) {
        this.checkShipAndAsteroidCollision(game, now);
        this.checkProjectileAndAsteroidCollision(game.getPoints(), now);
    }

    public void checkProjectileAndAsteroidCollision(Points points, long now) {
        this.projectiles.getList().forEach(projectile -> this.asteroids.getList().forEach(asteroid -> {
            if(projectile.collide(asteroid)) {
                projectile.setAlive(false);
                asteroid.setAlive(false);
                points.increment();

                this.particles.addParticle((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                        now, Game.ASTEROID_COLOR, 0, 360, 0.2, 0.3, 100_000_000, 75);
                this.particles.addParticle((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                        now, Game.ASTEROID_COLOR, 0, 360, 0.1, 0.2, 100_000_000, 75);
            }
        }));
    }

    public void checkShipAndAsteroidCollision(Game game, long now) {
        this.asteroids.getList().forEach(asteroid -> {
            if (this.ship.collide(asteroid) && !this.ship.isNotAlive()) {
                this.ship.disappear(now);
                game.gameOver();
            }
        });
    }
}
