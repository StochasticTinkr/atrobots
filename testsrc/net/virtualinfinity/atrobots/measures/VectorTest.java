package net.virtualinfinity.atrobots.measures;

import junit.framework.TestCase;

/**
 * @author Daniel Pitts
 */
public class VectorTest extends TestCase {
    private static final double DELTA = 0.000001;

    public void testCartesianAngles() {
        assertEquals(AngleTest.NORTH_BYGREES, createCartesian(AngleTest.NORTH_COSINE, AngleTest.NORTH_SINE).getAngle().getBygrees());
        assertEquals(AngleTest.EAST_BYGREES, createCartesian(AngleTest.EAST_COSINE, AngleTest.EAST_SINE).getAngle().getBygrees());
        assertEquals(AngleTest.SOUTH_BYGREES, createCartesian(AngleTest.SOUTH_COSINE, AngleTest.SOUTH_SINE).getAngle().getBygrees());
        assertEquals(AngleTest.WEST_BYGREES, createCartesian(AngleTest.WEST_COSINE, AngleTest.WEST_SINE).getAngle().getBygrees());
    }

    private static Vector createCartesian(double cosine, double sine) {
        return Vector.createCartesian(Distance.fromMeters(cosine), Distance.fromMeters(sine));
    }

    public void testPolar() {
        assertEquals(0, Vector.createPolar(AbsoluteAngle.fromBygrees(0), Distance.fromMeters(1)).getX().getMeters(), DELTA);
        assertEquals(-1, Vector.createPolar(AbsoluteAngle.fromBygrees(0), Distance.fromMeters(1)).getY().getMeters(), DELTA);

        assertEquals(1, Vector.createPolar(AbsoluteAngle.fromBygrees(64), Distance.fromMeters(1)).getX().getMeters(), DELTA);
        assertEquals(0, Vector.createPolar(AbsoluteAngle.fromBygrees(64), Distance.fromMeters(1)).getY().getMeters(), DELTA);

        assertEquals(0, Vector.createPolar(AbsoluteAngle.fromBygrees(128), Distance.fromMeters(1)).getX().getMeters(), DELTA);
        assertEquals(1, Vector.createPolar(AbsoluteAngle.fromBygrees(128), Distance.fromMeters(1)).getY().getMeters(), DELTA);

        assertEquals(-1, Vector.createPolar(AbsoluteAngle.fromBygrees(192), Distance.fromMeters(1)).getX().getMeters(), DELTA);
        assertEquals(0, Vector.createPolar(AbsoluteAngle.fromBygrees(192), Distance.fromMeters(1)).getY().getMeters(), DELTA);

    }

    public void testDot() {
        final Vector right5 = createCartesian(5, 0);
        final Vector down5 = createCartesian(0, 5);
        final Vector diagnal = createCartesian(5, 5);
        assertEquals(0, right5.dot(down5).getSquareMeters(), DELTA);
        assertEquals(25, right5.dot(right5).getSquareMeters(), DELTA);
        assertEquals(25, right5.dot(diagnal).getSquareMeters(), DELTA);
    }

    public void testProjection() {
        final Vector down5 = createCartesian(0, 5);
        final Vector down75 = createCartesian(0, 75);
        final Vector diagnal5 = createCartesian(5, 5);
        final Vector diagnal37dot5 = createCartesian(37.5, 37.5);
        assertEquals(down5.getX().getMeters(), down75.project(diagnal5).getX().getMeters(), DELTA);
        assertEquals(down5.getY().getMeters(), down75.project(diagnal5).getY().getMeters(), DELTA);
        assertEquals(diagnal37dot5.getX().getMeters(), diagnal5.project(down75).getX().getMeters(), DELTA);
        assertEquals(diagnal37dot5.getY().getMeters(), diagnal5.project(down75).getY().getMeters(), DELTA);
    }


}
