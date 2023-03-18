import java.util.Random;

public class Asteroid extends Character {

    private final double rotationalMovement;

    public Asteroid(int x, int y) {
        super(PolygonFactory.createPolygon(), x, y, AsteroidsApplication.ASTEROID_COLOR);
        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate(2);
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}