package net.virtualinfinity.atrobots.atsetup;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public enum AtRobotInterrupt {
    DESTRUCT(0),
    RESET(1),
    LOCATE(2),
    KEEPSHIFT(3),
    OVERBURN(4),
    ID(5),
    TIMER(6),
    ANGLE(7),
    TARGETID(8, "TID"),
    TARGETINFO(9, "TINFO"),
    GAMEINFO(10, "GINFO"),
    ROBOTINFO(11, "RINFO"),
    COLLISIONS(12),
    RESETCOLCNT(13),
    TRANSMIT(14),
    RECEIVE(15),
    DATAREADY(16),
    CLEARCOM(17),
    KILLS(18, "DEATHS"),
    CLEARMETERS(19),;
    public final int value;
    public final Collection<String> names;
    private static final String INTERRUPT_PREFIX = "I_";
    private static AtRobotInterrupt[] byValue = new AtRobotInterrupt[values().length];

    static {
        for (AtRobotInterrupt instruction : values()) {
            if (byValue[instruction.value] != null) {
                throw new IllegalStateException();
            }
            byValue[instruction.value] = instruction;
        }
    }

    private AtRobotInterrupt(int value) {
        this.value = value;
        this.names = Collections.singleton(INTERRUPT_PREFIX + name());
    }

    private AtRobotInterrupt(int value, String alternate) {
        this.value = value;
        this.names = Collections.unmodifiableCollection(Arrays.asList(INTERRUPT_PREFIX + name(), INTERRUPT_PREFIX + alternate));
    }

    public static String nameOf(short value) {
        if (value >= 0 && value < byValue.length) {
            return INTERRUPT_PREFIX + byValue[value].name();
        }
        return "<unknown>";
    }
}