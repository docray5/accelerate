package entities;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Particles {
    private final ArrayList<Particle> particles;
    private final Pane pane;
    private long lastParticleTime;

    public Particles(Pane pane) {
        this.particles = new ArrayList<>();
        this.pane = pane;
        this.lastParticleTime = 0;
    }

    public void move() {
        this.particles.forEach(Particle::move);
    }

    public long getLastParticleTime() {
        return lastParticleTime;
    }

    public void checkLifeTime(long now) {
        for (Particle particle : this.particles) {
            if (now - particle.getTimeBorn() > particle.getLifeTime()) particle.setAlive(false);
        }
    }

    public void removeIfNotAlive() {
        this.particles.stream()
                .filter(Character::isNotAlive)
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        this.particles.removeAll(this.particles.stream()
                .filter(Character::isNotAlive)
                .toList());
    }

    public void addParticle(int x, int y, long now, Color color, int fromDegrees, int toDegrees, double minSpeed, double maxSpeed, long baseLifeTime, int amount) {
        for (int i = 0; i < amount; i++) {
            Particle particle = new Particle(
                    x, y, now, color, fromDegrees, toDegrees, minSpeed, maxSpeed, baseLifeTime);
            this.particles.add(particle);
            this.pane.getChildren().add(particle.getCharacter());
            this.lastParticleTime = now;
        }
    }

    public void clear() {
        this.particles.clear();
    }
}
