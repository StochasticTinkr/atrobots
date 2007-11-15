package net.virtualinfinity.atrobots;

import junit.framework.TestCase;

/**
 * @author Daniel Pitts
 */
public class HeadingTest extends TestCase {
    private Heading absoluteHeading;
    private Heading relativeHeading;
    private Heading desiredHeading;

    protected void setUp() throws Exception {
        super.setUp();
        absoluteHeading = new Heading();
        absoluteHeading.setAbsolute(true);
        absoluteHeading.setAngle(AbsoluteAngle.fromBygrees(0)); // Due north
        relativeHeading = new Heading();
        relativeHeading.setRelation(absoluteHeading);
        relativeHeading.setAbsolute(false);
        relativeHeading.setAngle(AbsoluteAngle.fromBygrees(0));
        desiredHeading = new Heading();
    }

    public void testSetup() {
        assertAbsoluteHeading(AbsoluteAngle.fromBygrees(0));
        assertRelativeHeading(AbsoluteAngle.fromBygrees(0));
    }

    public void testRotateAbsolute() {
        absoluteHeading.rotate(RelativeAngle.fromBygrees(5));
        assertAbsoluteHeading(AbsoluteAngle.fromBygrees(5));
        assertRelativeHeading(AbsoluteAngle.fromBygrees(5));
    }

    public void testRotateRelative() {
        relativeHeading.rotate(RelativeAngle.fromBygrees(5));
        assertAbsoluteHeading(AbsoluteAngle.fromBygrees(0));
        assertRelativeHeading(AbsoluteAngle.fromBygrees(5));
    }

    public void testMoveTo() {
        desiredHeading.setAngle(AbsoluteAngle.fromBygrees(9));
        absoluteHeading.moveToward(desiredHeading, RelativeAngle.fromBygrees(5));
        assertAbsoluteHeading(AbsoluteAngle.fromBygrees(5));
        assertRelativeHeading(AbsoluteAngle.fromBygrees(5));
        absoluteHeading.moveToward(desiredHeading, RelativeAngle.fromBygrees(5));
        assertAbsoluteHeading(AbsoluteAngle.fromBygrees(9));
        assertRelativeHeading(AbsoluteAngle.fromBygrees(9));
    }

    private void assertRelativeHeading(AbsoluteAngle actual) {
        assertEquals(relativeHeading.getAngle(), actual);
    }

    private void assertAbsoluteHeading(AbsoluteAngle actual) {
        assertEquals(absoluteHeading.getAngle(), actual);
    }

    private static void assertEquals(AbsoluteAngle expected, AbsoluteAngle actual) {
        assertEquals(expected.getNormalizedRadians(), actual.getNormalizedRadians(), Math.PI / 8000);
    }

}
