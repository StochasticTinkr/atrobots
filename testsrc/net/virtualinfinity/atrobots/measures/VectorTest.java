package net.virtualinfinity.atrobots.measures;

import junit.framework.TestCase;

/**
 * @author Daniel Pitts
 */
public class VectorTest extends TestCase {
    private static final double DELTA = 0.000001;

    public void testCartesianAngles() {
        assertEquals(64, Vector.createCartesian(Distance.fromMeters(10), Distance.fromMeters(0)).getAngle().getBygrees());
        assertEquals(128, Vector.createCartesian(Distance.fromMeters(0), Distance.fromMeters(10)).getAngle().getBygrees());
        assertEquals(192, Vector.createCartesian(Distance.fromMeters(-10), Distance.fromMeters(0)).getAngle().getBygrees());
        assertEquals(0, Vector.createCartesian(Distance.fromMeters(0), Distance.fromMeters(-10)).getAngle().getBygrees());
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
        final Vector right5 = Vector.createCartesian(Distance.fromMeters(5), Distance.fromMeters(0));
        final Vector down5 = Vector.createCartesian(Distance.fromMeters(0), Distance.fromMeters(5));
        final Vector diagnal = Vector.createCartesian(Distance.fromMeters(5), Distance.fromMeters(5));
        assertEquals(0, right5.dot(down5).getSquareMeters(), DELTA);
        assertEquals(25, right5.dot(right5).getSquareMeters(), DELTA);
        assertEquals(25, right5.dot(diagnal).getSquareMeters(), DELTA);
    }

    public void testProjection() {
        final Vector down5 = Vector.createCartesian(Distance.fromMeters(0), Distance.fromMeters(5));
        final Vector down75 = Vector.createCartesian(Distance.fromMeters(0), Distance.fromMeters(75));
        final Vector diagnal5 = Vector.createCartesian(Distance.fromMeters(5), Distance.fromMeters(5));
        final Vector diagnal37dot5 = Vector.createCartesian(Distance.fromMeters(37.5), Distance.fromMeters(37.5));
        assertEquals(down5.getX().getMeters(), down75.project(diagnal5).getX().getMeters(), DELTA);
        assertEquals(down5.getY().getMeters(), down75.project(diagnal5).getY().getMeters(), DELTA);
        assertEquals(diagnal37dot5.getX().getMeters(), diagnal5.project(down75).getX().getMeters(), DELTA);
        assertEquals(diagnal37dot5.getY().getMeters(), diagnal5.project(down75).getY().getMeters(), DELTA);
    }


}
