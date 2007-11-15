package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Distance;

/**
 * @author Daniel Pitts
 */
public class Radar {
    private Robot robot;

    public PortHandler getScanPort() {
        return new PortHandler() {
            public short read() {
                getComputer().consumeCycles(3);
                final Distance distance = scan();
                if (distance == null) {
                    return Short.MAX_VALUE;
                }
                return (short) Math.round(distance.getMeters());
            }
        };
    }

    private Distance scan() {
        robot.scan(AngleBracket.all(), Distance.infinity());
        return null;
    }

}
