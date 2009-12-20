package net.virtualinfinity.atrobots.measures;

import junit.framework.TestCase;

import static java.lang.Math.PI;

/**
 * @author Daniel Pitts
 */
public class AngleTest extends TestCase {
    public static final double NORTH_SINE = -1;
    public static final int NORTH_BYGREES = 0;
    public static final double NORTH_COSINE = 0;
    public static final double EAST_SINE = 0;
    public static final double EAST_COSINE = 1;
    public static final double SOUTH_SINE = 1;
    public static final double SOUTH_COSINE = 0;
    public static final double WEST_SINE = 0;
    public static final double WEST_COSINE = -1;
    public static final int EAST_BYGREES = 64;
    public static final int SOUTH_BYGREES = 128;
    public static final int WEST_BYGREES = 192;
    public static final double NORTH_RADIANS = PI / 2 * 3;
    public static final double EAST_RADIANS = 0;
    public static final double SOUTH_RADIANS = PI / 2;
    public static final double WEST_RADIANS = PI;

    public void testBygreeOrientation() {
        assertEquals(NORTH_SINE, AbsoluteAngle.fromBygrees(NORTH_BYGREES).sine(), 0.00001);
        assertEquals(NORTH_COSINE, AbsoluteAngle.fromBygrees(NORTH_BYGREES).cosine(), 0.00001);

        assertEquals(EAST_SINE, AbsoluteAngle.fromBygrees(EAST_BYGREES).sine(), 0.00001);
        assertEquals(EAST_COSINE, AbsoluteAngle.fromBygrees(EAST_BYGREES).cosine(), 0.00001);

        assertEquals(SOUTH_SINE, AbsoluteAngle.fromBygrees(SOUTH_BYGREES).sine(), 0.00001);
        assertEquals(SOUTH_COSINE, AbsoluteAngle.fromBygrees(SOUTH_BYGREES).cosine(), 0.00001);

        assertEquals(WEST_SINE, AbsoluteAngle.fromBygrees(WEST_BYGREES).sine(), 0.00001);
        assertEquals(WEST_COSINE, AbsoluteAngle.fromBygrees(WEST_BYGREES).cosine(), 0.00001);
    }

    public void testFromBygrees() {
        assertEquals(NORTH_BYGREES, AbsoluteAngle.fromBygrees(NORTH_BYGREES).getBygrees());
        assertEquals(EAST_BYGREES, AbsoluteAngle.fromBygrees(EAST_BYGREES).getBygrees());
        assertEquals(SOUTH_BYGREES, AbsoluteAngle.fromBygrees(SOUTH_BYGREES).getBygrees());
        assertEquals(WEST_BYGREES, AbsoluteAngle.fromBygrees(WEST_BYGREES).getBygrees());
    }

    public void testRadianOrientation() {
        assertEquals(NORTH_SINE, AbsoluteAngle.fromRadians(NORTH_RADIANS).sine(), 0.00001);
        assertEquals(NORTH_COSINE, AbsoluteAngle.fromRadians(NORTH_RADIANS).cosine(), 0.00001);
        assertEquals(EAST_SINE, AbsoluteAngle.fromRadians(EAST_RADIANS).sine(), 0.00001);
        assertEquals(EAST_COSINE, AbsoluteAngle.fromRadians(EAST_RADIANS).cosine(), 0.00001);
        assertEquals(SOUTH_SINE, AbsoluteAngle.fromRadians(SOUTH_RADIANS).sine(), 0.00001);
        assertEquals(SOUTH_COSINE, AbsoluteAngle.fromRadians(SOUTH_RADIANS).cosine(), 0.00001);
        assertEquals(WEST_SINE, AbsoluteAngle.fromRadians(WEST_RADIANS).sine(), 0.00001);
        assertEquals(WEST_COSINE, AbsoluteAngle.fromRadians(WEST_RADIANS).cosine(), 0.00001);
    }

    public void testGetAngleClockwiseTo() {
        assertEquals(RelativeAngle.fromBygrees(EAST_BYGREES).getBygrees(), AbsoluteAngle.fromBygrees(EAST_BYGREES).getAngleCounterClockwiseTo(AbsoluteAngle.fromBygrees(NORTH_BYGREES)).getBygrees());
    }

    public void testClockwiseIsCloserTo() {
        assertTrue(AbsoluteAngle.fromBygrees(EAST_BYGREES).isClockwiseCloser(AbsoluteAngle.fromBygrees(NORTH_BYGREES)));
    }

}
