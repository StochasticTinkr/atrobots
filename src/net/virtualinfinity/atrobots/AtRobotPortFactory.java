package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.util.MapWithDefaultValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Daniel Pitts
 */
public class AtRobotPortFactory {
    private final Robot robot;

    public AtRobotPortFactory(Robot robot) {
        this.robot = robot;
    }

    public Map<Integer, PortHandler> createPortHandlers() {
        final Map<Integer, PortHandler> ports = new HashMap<Integer, PortHandler>();
        ports.put(1, robot.getThrottle().getSpedometer());
        ports.put(2, robot.getHeat().getHeatSensor());
        ports.put(3, robot.getHeading().getCompass());
        ports.put(4, robot.getTurretOffsetSensor());
        ports.put(5, robot.getTurret().getHeading().getCompass());
        ports.put(6, robot.getArmor().getSensor());
        ports.put(7, robot.getTurret().getScanner().getScanPort());
        ports.put(8, robot.getTurret().getScanner().getAccuracyPort());
        ports.put(9, robot.getRadar().getScanPort());
        ports.put(10, createRandomNumberGenerator());
        ports.put(11, robot.getThrottle().getActuator());
        ports.put(12, robot.getTurret().getHeading().getRotationPort());
        ports.put(13, robot.getAimTurretPort());
        ports.put(14, robot.getDesiredHeading().getRotationPort());
        ports.put(15, robot.getTurret().getMissileLauncher().getActuator());
        ports.put(16, robot.getSonar().getScanPort());
        ports.put(17, robot.getTurret().getScanner().getScanArcLatchPort());
        ports.put(18, robot.getOverburnLatchPort());
        ports.put(19, robot.getTransponder().getIdLatchPort());
        ports.put(20, robot.getShutdownLevelLatchPort());
        ports.put(21, robot.getTransceiver().getChannelLatchPort());
        ports.put(22, robot.getMineLayer().getMineBayPort());
        ports.put(23, robot.getMineLayer().getPlacedMinePort());
        ports.put(24, robot.getShield().getLatch());
        for (PortHandler handler : ports.values()) {
            handler.setComputer(robot.getComputer());
        }
        return new MapWithDefaultValue<Integer, PortHandler>(ports, robot.getComputer().createDefaultPortHandler());
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
