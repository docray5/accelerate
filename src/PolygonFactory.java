import javafx.scene.shape.Polygon;

import java.util.Random;

public class PolygonFactory {
    public static Polygon createPolygon(int size) {
        Random rnd = new Random();

        double finalSize = size + rnd.nextInt(10) + 2;

        Polygon polygon = new Polygon();
        double c1 = Math.cos(Math.PI * 2 / 5);
        double c2 = Math.cos(Math.PI / 5);
        double s1 = Math.sin(Math.PI * 2 / 5);
        double s2 = Math.sin(Math.PI * 4 / 5);

        polygon.getPoints().addAll(
                finalSize, 0.0,
                finalSize * c1, -1 * finalSize * s1,
                -1 * finalSize * c2, -1 * finalSize * s2,
                -1 * finalSize * c2, finalSize * s2,
                finalSize * c1, finalSize * s1);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            int change = rnd.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}
