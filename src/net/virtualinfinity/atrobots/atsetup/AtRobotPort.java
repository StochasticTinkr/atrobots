package net.virtualinfinity.atrobots.atsetup;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public enum AtRobotPort {
    SPEDOMETER(1),
    HEAT(2),
    COMPASS(3),
    TURRET_OFS(4),
    TURRET_ABS(5),
    DAMAGE(6, "ARMOR"),
    SCAN(7),
    ACCURACY(8),
    RADAR(9),
    RANDOM(10, "RAND"),
    THROTTLE(11),
    OFS_TURRET(12, "TROTATE"),
    ABS_TURRET(13, "TAIM"),
    STEERING(14),
    WEAP(15, "WEAPON", "FIRE"),
    SONAR(16),
    ARC(17, "SCANARC"),
    OVERBURN(18),
    TRANSPONDER(19),
    SHUTDOWN(20),
    CHANNEL(21),
    MINELAYER(22),
    MINETRIGGER(23),
    SHIELD(24, "SHIELDS");
    public final int value;
    public final Collection<String> names;
    private static final String PORT_SYMBOL_PREFIX = "P_";

    private AtRobotPort(int value) {
        this.value = value;
        names = Collections.singleton(PORT_SYMBOL_PREFIX + name());
    }

    private AtRobotPort(int value, String alternateName) {
        this.value = value;
        names = Collections.unmodifiableCollection(Arrays.asList(PORT_SYMBOL_PREFIX + name(), PORT_SYMBOL_PREFIX + alternateName));
    }

    private AtRobotPort(int value, String alternateName1, String alternateName2) {
        this.value = value;
        names = Collections.unmodifiableCollection(Arrays.asList(PORT_SYMBOL_PREFIX + name(), PORT_SYMBOL_PREFIX + alternateName1, PORT_SYMBOL_PREFIX + alternateName2));
    }

    public static String nameOf(short operandValue) {
        for (AtRobotPort port : values()) {
            if (port.value == operandValue) {
                return PORT_SYMBOL_PREFIX + port.name();
            }
        }
        return "<unknown>";
    }
}

