package net.virtualinfinity.atrobots.measures;

import junit.framework.TestCase;

/**
 * @author Daniel Pitts
 */
public class AngleTest extends TestCase {
    public void testOrientation() {
        assertEquals(-1, AbsoluteAngle.fromBygrees(0).sine(), 0.00001);
        assertEquals(0, AbsoluteAngle.fromBygrees(0).cosine(), 0.00001);

        assertEquals(0, AbsoluteAngle.fromBygrees(64).sine(), 0.00001);
        assertEquals(1, AbsoluteAngle.fromBygrees(64).cosine(), 0.00001);

        assertEquals(1, AbsoluteAngle.fromBygrees(128).sine(), 0.00001);
        assertEquals(0, AbsoluteAngle.fromBygrees(128).cosine(), 0.00001);

        assertEquals(0, AbsoluteAngle.fromBygrees(192).sine(), 0.00001);
        assertEquals(-1, AbsoluteAngle.fromBygrees(192).cosine(), 0.00001);
    }

    public void testFromBygrees() {
        assertEquals(0, AbsoluteAngle.fromBygrees(0).getBygrees());
        assertEquals(64, AbsoluteAngle.fromBygrees(64).getBygrees());
        assertEquals(128, AbsoluteAngle.fromBygrees(128).getBygrees());
        assertEquals(192, AbsoluteAngle.fromBygrees(192).getBygrees());
    }


}
