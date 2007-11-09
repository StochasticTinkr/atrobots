package net.virtualinfinity.atrobots;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class AtRobotInterruptFactory {
    public final Robot robot;

    public AtRobotInterruptFactory(Robot robot) {
        this.robot = robot;
    }

    public InterruptHandler createResetMetersInterrupt() {
        return new ResetMetersInterrupt(robot, robot.getComputer().getRegisters().getMeters());
    }

    public InterruptHandler createGetRobotStatisticsInterrupt() {
        return new GetRobotStatisticsInterrupt(robot.getEntrant(), robot.getComputer().getRegisters().getDx(), robot.getComputer().getRegisters().getEx(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createResetQueueInterrupt() {
        return new ZeroMemoryCellsInterrupt(robot.getComputer().getRegisters().getCommunicationQueueHead(),
                robot.getComputer().getRegisters().getCommunicationQueueTail());
    }

    public InterruptHandler createGetQueueSizeInterrupt() {
        return new GetQueueSizeInterrupt(robot.getComputer().getCommQueue(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createRecieveInterrupt() {
        return new ReceiveInterrupt(robot.getComputer().getCommQueue(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createTransmitInterrupt() {
        return new TransmitInterrupt(robot.getTransceiver(), robot.getComputer().getRegisters().getAx());
    }

    public InterruptHandler createResetCollisionCountInterrupt() {
        return new ZeroMemoryCellsInterrupt(robot.getComputer().getRegisters().getCollisionCount());
    }

    public InterruptHandler createGetCollisionsInterrupt() {
        return new CopyMemoryCellsInterrupt(robot.getComputer().getRegisters().getCollisionCount(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createGetRobotInfoInterrupt() {
        return new GetRobotInfoInterrupt(robot, robot.getComputer().getRegisters().getDx(), robot.getComputer().getRegisters().getEx(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createGetGameInfoInterrupt() {
        return new GetGameInfoInterrupt(robot.getEntrant().getGame(), robot.getComputer().getRegisters().getDx(), robot.getComputer().getRegisters().getEx(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createGetTargetInfoInterrupt() {
        return new CopyMemoryCellsInterrupt(robot.getComputer().getRegisters().getTargetHeading(), robot.getComputer().getRegisters().getEx(),
                robot.getComputer().getRegisters().getTargetThrottle(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createGetTargetIdInterrupt() {
        return new CopyMemoryCellsInterrupt(robot.getComputer().getRegisters().getTargetId(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createFindangleInterrupt() {
        return new FindAngleInterrupt(robot.getComputer().getRegisters().getEx(), robot.getComputer().getRegisters().getFx(), robot.getComputer().getRegisters().getAx());
    }

    public InterruptHandler createGetTimerInterrupt() {
        return new GetTimerInterrupt(robot.getEntrant().getGame(), robot.getComputer().getRegisters().getEx(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createGetTransponderIdInterrupt() {
        return new GetIdInterrupt(robot.getTransponder(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createOverburnInterrupt() {
        return new OverburnInterrupt(robot, robot.getComputer().getRegisters().getAx());
    }

    public InterruptHandler createSetKeepshiftInterrupt() {
        return new SetKeepshiftInterrupt(robot.getTurret(), robot.getComputer().getRegisters().getAx());
    }

    public InterruptHandler createLocateInterrupt() {
        return new LocateInterrupt(robot, robot.getComputer().getRegisters().getEx(), robot.getComputer().getRegisters().getFx());
    }

    public InterruptHandler createResetInterrupt() {
        return new ResetInterrupt(robot.getComputer());
    }

    public InterruptHandler createDestructInterrupt() {
        return new DestructInterrupt(robot);
    }

    InvalidInterrupt createInvalidInterrupt() {
        return new InvalidInterrupt(robot.getComputer().getErrorHandler());
    }

    Map<Integer, InterruptHandler> createInterruptTable() {
        Map<Integer, InterruptHandler> interrupts = new HashMap<Integer, InterruptHandler>();

        interrupts.put(0, createDestructInterrupt());
        interrupts.put(1, createResetInterrupt().costs(10));
        interrupts.put(2, createLocateInterrupt().costs(5));
        interrupts.put(3, createSetKeepshiftInterrupt().costs(2));
        interrupts.put(4, createOverburnInterrupt().costs(1));
        interrupts.put(5, createGetTransponderIdInterrupt().costs(2));
        interrupts.put(6, createGetTimerInterrupt().costs(2));
        interrupts.put(7, createFindangleInterrupt().costs(32));
        interrupts.put(8, createGetTargetIdInterrupt().costs(1));
        interrupts.put(9, createGetTargetInfoInterrupt().costs(2));
        interrupts.put(10, createGetGameInfoInterrupt().costs(4));
        interrupts.put(11, createGetRobotInfoInterrupt().costs(5));
        interrupts.put(12, createGetCollisionsInterrupt().costs(1));
        interrupts.put(13, createResetCollisionCountInterrupt().costs(1));
        interrupts.put(14, createTransmitInterrupt().costs(1));
        interrupts.put(15, createRecieveInterrupt().costs(1));
        interrupts.put(16, createGetQueueSizeInterrupt().costs(1));
        interrupts.put(17, createResetQueueInterrupt().costs(1));
        interrupts.put(18, createGetRobotStatisticsInterrupt().costs(3));
        interrupts.put(19, createResetMetersInterrupt().costs(1));
        for (InterruptHandler handler : interrupts.values()) {
            handler.setComputer(robot.getComputer());
        }
        return new MapWithDefaultValue<Integer, InterruptHandler>(interrupts, createInvalidInterrupt());
    }
}
