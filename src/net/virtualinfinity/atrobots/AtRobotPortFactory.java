package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.atsetup.AtRobotPort;
import static net.virtualinfinity.atrobots.atsetup.AtRobotPort.*;
import net.virtualinfinity.atrobots.util.MapWithDefaultValue;

import java.util.*;

/**
 * Creates the port mapping for a standard AT-Robot configuration.
 *
 * @author Daniel Pitts
 */
public class AtRobotPortFactory {

    public Map<Integer, PortHandler> createPortHandlers(Robot robot) {
        final Map<Integer, PortHandler> ports = new HashMap<Integer, PortHandler>();
        mapPort(ports, SPEDOMETER, robot.getThrottle().getSpedometer());
        mapPort(ports, HEAT, robot.getHeat().getHeatSensor());
        mapPort(ports, COMPASS, robot.getHeading().getCompass());
        mapPort(ports, TURRET_OFS, robot.getTurretOffsetSensor());
        mapPort(ports, TURRET_ABS, robot.getTurret().getHeading().getCompass());
        mapPort(ports, DAMAGE, robot.getArmor().getSensor());
        mapPort(ports, SCAN, robot.getTurret().getScanner().getScanPort());
        mapPort(ports, ACCURACY, robot.getTurret().getScanner().getAccuracyPort());
        mapPort(ports, RADAR, robot.getRadar().getScanPort());
        mapPort(ports, RANDOM, createRandomNumberGenerator());
        mapPort(ports, THROTTLE, robot.getThrottle().getActuator());
        mapPort(ports, OFS_TURRET, robot.getTurret().getHeading().getRotationPort());
        mapPort(ports, ABS_TURRET, robot.getAimTurretPort());
        mapPort(ports, STEERING, robot.getDesiredHeading().getRotationPort());
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
