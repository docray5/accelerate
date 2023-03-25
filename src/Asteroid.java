import java.util.Random;

public class Asteroid extends Character {

    private final double rotationalMovement;
    private final int size;

    public Asteroid(int x, int y, int size) {
        super(PolygonFactory.createPolygon(size), x, y, 2, 4, Game.ASTEROID_COLOR);
        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate(Game.ASTEROID_SPEED);
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
        this.size = size;
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }

    public int getSize() {
        return size;
    }
}