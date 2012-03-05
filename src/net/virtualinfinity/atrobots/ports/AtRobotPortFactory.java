package net.virtualinfinity.atrobots.ports;

import net.virtualinfinity.atrobots.atsetup.AtRobotPort;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.simulation.arena.Heading;
import net.virtualinfinity.atrobots.simulation.atrobot.HasHeading;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;
import net.virtualinfinity.atrobots.util.MapWithDefaultValue;

import java.util.*;

import static net.virtualinfinity.atrobots.atsetup.AtRobotPort.*;

/**
 * Creates the port mapping for a standard AT-Robot configuration.
 *
 * @author Daniel Pitts
 */
public class AtRobotPortFactory {

    public static PortHandler getCompass(final Heading heading) {
        return new PortHandler() {
            public short read() {
                return (short) heading.getAngle().getBygrees();
            }
        };
    }

    public static PortHandler getRotationPort(final Heading heading) {
        return new PortHandler() {
            public void write(short value) {
                heading.rotate(RelativeAngle.fromBygrees(value));
            }
        };
    }

    public Map<Integer, PortHandler> createPortHandlers(Robot robot) {
        final Map<Integer, PortHandler> ports = new HashMap<Integer, PortHandler>();
        mapPort(ports, SPEDOMETER, robot.getThrottle().getSpedometer());
        mapPort(ports, HEAT, robot.getHeat().getHeatSensor());
        mapPort(ports, COMPASS, getCompass(robot));
        mapPort(ports, TURRET_OFS, robot.getTurretOffsetSensor());
        mapPort(ports, TURRET_ABS, getCompass(robot.getTurret()));
        mapPort(ports, DAMAGE, robot.getArmor().getSensor());
        mapPort(ports, SCAN, robot.getTurret().getScanner().getScanPort());
        mapPort(ports, ACCURACY, robot.getTurret().getScanner().getAccuracyPort());
        mapPort(ports, RADAR, robot.getRadar().getScanPort());
        mapPort(ports, RANDOM, createRandomNumberGenerator());
        mapPort(ports, THROTTLE, robot.getThrottle().getActuator());
        mapPort(ports, OFS_TURRET, getRotationPort(robot.getTurret().getHeading()));
        mapPort(ports, ABS_TURRET, robot.getAimTurretPort());
        mapPort(ports, STEERING, getRotationPort(robot.getDesiredHeading()));
        mapPort(ports, WEAP, robot.getTurret().getMissileLauncher().getActuator());
        mapPort(ports, SONAR, robot.getSonar().getScanPort());
        mapPort(ports, ARC, robot.getTurret().getScanner().getScanArcLatchPort());
        mapPort(ports, OVERBURN, robot.getOverburnLatchPort());
        mapPort(ports, TRANSPONDER, robot.getTransponder().getIdLatchPort());
        mapPort(ports, SHUTDOWN, robot.getShutdownLevelLatchPort());
        mapPort(ports, CHANNEL, robot.getTransceiver().getChannelLatchPort());
        mapPort(ports, MINELAYER, robot.getMineLayer().getMineBayPort());
        mapPort(ports, MINETRIGGER, robot.getMineLayer().getPlacedMinePort());
        mapPort(ports, SHIELD, robot.getShield().getLatch());
        connectRobot(robot, ports.values());
        return new MapWithDefaultValue<Integer, PortHandler>(Collections.unmodifiableMap(ports), robot.getComputer().createDefaultPortHandler());
    }

    private PortHandler getCompass(HasHeading hasHeading) {
        return getCompass(hasHeading.getHeading());
    }

    private void connectRobot(Robot robot, Collection<PortHandler> collection) {
        for (PortHandler handler : collection) {
            handler.setComputer(robot.getComputer());
        }
    }

    private void mapPort(Map<Integer, PortHandler> ports, AtRobotPort port, PortHandler portHandler) {
        ports.put(port.portNumber, portHandler);
    }

    private PortHandler createRandomNumberGenerator() {
        return new PortHandler() {
            private final Random random = new Random();

            public short read() {
                return (short) random.nextInt();
            }
        };
    }
}
