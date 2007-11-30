package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.computer.Registers;
import net.virtualinfinity.atrobots.util.MapWithDefaultValue;

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
        return new ResetMetersInterrupt(robot, getRegisters().getMeters());
    }

    public InterruptHandler createGetRobotStatisticsInterrupt() {
        return new GetRobotStatisticsInterrupt(robot.getEntrant(), getDxCell(), getExCell(), getFxCell());
    }

    private MemoryCell getDxCell() {
        return getRegisters().getDx();
    }

    public InterruptHandler createResetQueueInterrupt() {
        return new ZeroMemoryCellsInterrupt(getRegisters().getCommunicationQueueHead(),
                getRegisters().getCommunicationQueueTail());
    }

    public InterruptHandler createGetQueueSizeInterrupt() {
        return new GetQueueSizeInterrupt(robot.getComputer().getCommQueue(), getFxCell());
    }

    public InterruptHandler createRecieveInterrupt() {
        return new ReceiveInterrupt(robot.getComputer().getCommQueue(), getFxCell());
    }

    public InterruptHandler createTransmitInterrupt() {
        return new TransmitInterrupt(robot.getTransceiver(), getAxCell());
    }

    public InterruptHandler createResetCollisionCountInterrupt() {
        return new ZeroMemoryCellsInterrupt(getRegisters().getCollisionCount());
    }

    public InterruptHandler createGetCollisionsInterrupt() {
        return new CopyMemoryCellsInterrupt(getRegisters().getCollisionCount(), getFxCell());
    }

    public InterruptHandler createGetRobotInfoInterrupt() {
        return new GetRobotInfoInterrupt(robot, getDxCell(), getExCell(), getFxCell());
    }

    public InterruptHandler createGetGameInfoInterrupt() {
        return new GetGameInfoInterrupt(robot.getEntrant().getGame(), getDxCell(), getExCell(), getFxCell());
    }

    public InterruptHandler createGetTargetInfoInterrupt() {
        return new CopyMemoryCellsInterrupt(getRegisters().getTargetHeading(), getExCell(),
                getRegisters().getTargetThrottle(), getFxCell());
    }

    public InterruptHandler createGetTargetIdInterrupt() {
        return new CopyMemoryCellsInterrupt(getRegisters().getTargetId(), getFxCell());
    }

    public InterruptHandler createFindAngleInterrupt() {
        return new FindAngleInterrupt(getExCell(), getFxCell(), getAxCell(), robot);
    }

    private MemoryCell getAxCell() {
        return getRegisters().getAx();
    }

    public InterruptHandler createGetTimerInterrupt() {
        return new GetTimerInterrupt(robot.getEntrant().getGame(), getExCell(), getFxCell());
    }

    public InterruptHandler createGetTransponderIdInterrupt() {
        return new GetIdInterrupt(robot.getTransponder(), getFxCell());
    }

    public InterruptHandler createOverburnInterrupt() {
        return new OverburnInterrupt(robot, getAxCell());
    }

    public InterruptHandler createSetKeepshiftInterrupt() {
        return new SetKeepshiftInterrupt(robot.getTurret(), getAxCell());
    }

    public InterruptHandler createLocateInterrupt() {
        return new LocateInterrupt(robot,
                getExCell(), getFxCell());
    }

    private MemoryCell getFxCell() {
        return getRegisters().getFx();
    }

    private MemoryCell getExCell() {
        return getRegisters().getEx();
    }

    private Registers getRegisters() {
        return robot.getComputer().getRegisters();
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

    public Map<Integer, InterruptHandler> createInterruptTable() {
        Map<Integer, InterruptHandler> interrupts = new HashMap<Integer, InterruptHandler>();

        interrupts.put(0, createDestructInterrupt());
        interrupts.put(1, createResetInterrupt().costs(10));
        interrupts.put(2, createLocateInterrupt().costs(5));
        interrupts.put(3, createSetKeepshiftInterrupt().costs(2));
        interrupts.put(4, createOverburnInterrupt().costs(1));
        interrupts.put(5, createGetTransponderIdInterrupt().costs(2));
        interrupts.put(6, createGetTimerInterrupt().costs(2));
        interrupts.put(7, createFindAngleInterrupt().costs(32));
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
