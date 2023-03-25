import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Projectiles {
    private final ArrayList<Projectile> projectiles;
    private final Pane pane;
    private final Particles particles;
    private long lastProjectileTime;

    public Projectiles(Pane pane, Particles particles) {
        this.projectiles = new ArrayList<>();
        this.pane = pane;
        this.particles = particles;
        this.lastProjectileTime = 0;
    }

    public ArrayList<Projectile> getList() {
        return projectiles;
    }

    public void move() {
        this.projectiles.forEach(Projectile::move);
    }

    public void checkLifeTime(long now) {
        for (Projectile projectile : this.projectiles) {
            if (now - projectile.getTimeBorn() > projectile.getLifeTime()) {
                projectile.setAlive(false);
                this.particles.addParticle(
                        (int) projectile.getCharacter().getTranslateX(), (int) projectile.getCharacter().getTranslateY(),
                        now, Game.PROJECTILE_COLOR,
                        0, 360, 0.1, 0.2, 100_000_000, 40);
            }
        }
    }

    // TODO this needs a little work
    public void removeIfNotAlive() {
        this.projectiles.stream()
                .filter(Character::isNotAlive)
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        this.projectiles.removeAll(this.projectiles.stream().filter(Character::isNotAlive).toList());
    }

    public void addProjectile(Ship ship, long now) {
        if (this.projectiles.size() < 6 && now - this.lastProjectileTime > 300_000_000L) {
            Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(),
                    (int) ship.getCharacter().getTranslateY(), now, 4_000_000_000L);
            projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
            projectile.accelerate(Game.PROJECTILE_SPEED);
            projectile.setMovement(projectile.getMovement().normalize().multiply(3 * Game.FPS_RATIO));

            this.projectiles.add(projectile);
            this.pane.getChildren().add(projectile.getCharacter());

            ship.accelerate(-0.2);
            this.lastProjectileTime = now;

            // after that we add a little smoke
            int particleX = (int) Math.round(Math.cos(Math.toRadians(ship.getCharacter().getRotate()))) * 6 + (int) ship.getCharacter().getTranslateX();
            int particleY = (int) Math.round(Math.sin(Math.toRadians(ship.getCharacter().getRotate()))) * 6 + (int) ship.getCharacter().getTranslateY();

            this.particles.addParticle(particleX + new Random().nextInt(10) -5,
                    particleY + new Random().nextInt(10) -5,
                    now, Color.WHITE, (int) ship.getCharacter().getRotate() - 90,
                    (int) ship.getCharacter().getRotate() + 90, 0.2, 0.3, 30_000_000L, 25);
        }
    }
}
