import java.util.Random;

public class Asteroid extends Character {

    private final double rotationalMovement;

    public Asteroid(int x, int y, int size) {
        super(PolygonFactory.createPolygon(size), x, y, 2, 4, AsteroidsApplication.ASTEROID_COLOR, size, 0);
        // we set size as time born bcs I am lazy and don't want to make any new variables
        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate(AsteroidsApplication.ASTEROID_SPEED);
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}