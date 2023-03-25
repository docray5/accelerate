import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Ship extends Character {

    private final Particles particles;
    private final Projectiles projectiles;

    public Ship(int x, int y, Particles particles, Projectiles projectiles) {
        super(new Polygon(-8, -8, 16, 0, -8, 8, -4, 0), x, y, 1.3, 2.2, Game.SHIP_COLOR);
        this.particles = particles;
        this.projectiles = projectiles;
    }

    public void accel(long now) {
        super.accelerate(Game.SHIP_SPEED * Game.FPS_RATIO);
        addSmoke(now);
    }

    public void shoot(long now) {
        this.projectiles.addProjectile(this, now);
    }

    private void addSmoke(long now) {
        if (now - this.particles.getLastParticleTime() > 5_000_000L) {
            //double shipRotation = Math.abs(ship.getCharacter().getRotate() - 360 * (int)(ship.getCharacter().getRotate() / 360));

            int particleX = (int) -Math.round(Math.cos(Math.toRadians(super.getCharacter().getRotate()))) * 7 + (int) super.getCharacter().getBoundsInParent().getCenterX();
            int particleY = (int) -Math.round(Math.sin(Math.toRadians(super.getCharacter().getRotate()))) * 7 + (int) super.getCharacter().getBoundsInParent().getCenterY();
            this.particles.addParticle(
                    particleX + new Random().nextInt(6) -3,
                    particleY + new Random().nextInt(6) -3,
                    now, Color.LIGHTSLATEGRAY, (int) -super.getCharacter().getRotate() - 90,
                    (int) -super.getCharacter().getRotate() + 90, 0.1, 0.2, 300_000_000L, 1);
        }
    }
}
