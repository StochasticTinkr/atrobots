package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.computer.Registers;
import net.virtualinfinity.atrobots.util.MapWithDefaultValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates built-in interrupt handlers for the standard AT-Robots set up.
 *
 * @author Daniel Pitts
 */
public class AtRobotInterruptFactory {
    public AtRobotInterruptFactory() {
    }

    private InterruptHandler createResetMetersInterrupt(Robot robot) {
        return new ResetMetersInterrupt(robot, getRegisters(robot).getMeters());
    }

    private InterruptHandler createGetRobotStatisticsInterrupt(Robot robot) {
        return new GetRobotStatisticsInterrupt(robot.getEntrant(), getDxCell(robot), getExCell(robot), getFxCell(robot));
    }

    private MemoryCell getDxCell(Robot robot) {
        return getRegisters(robot).getDx();
    }

    private InterruptHandler createResetQueueInterrupt(Robot robot) {
        return new ZeroMemoryCellsInterrupt(getRegisters(robot).getCommunicationQueueHead(),
                getRegisters(robot).getCommunicationQueueTail());
    }

    private InterruptHandler createGetQueueSizeInterrupt(Robot robot) {
        return new GetQueueSizeInterrupt(robot.getComputer().getCommQueue(), getFxCell(robot));
    }

    private InterruptHandler createRecieveInterrupt(Robot robot) {
        return new ReceiveInterrupt(robot.getComputer().getCommQueue(), getFxCell(robot));
    }

    private InterruptHandler createTransmitInterrupt(Robot robot) {
        return new TransmitInterrupt(robot.getTransceiver(), getAxCell(robot));
    }

    private InterruptHandler createResetCollisionCountInterrupt(Robot robot) {
        return new ZeroMemoryCellsInterrupt(getRegisters(robot).getCollisionCount());
    }

    private InterruptHandler createGetCollisionsInterrupt(Robot robot) {
        return new CopyMemoryCellsInterrupt(getRegisters(robot).getCollisionCount(), getFxCell(robot));
    }

    private InterruptHandler createGetRobotInfoInterrupt(Robot robot) {
        return new GetRobotInfoInterrupt(robot, getDxCell(robot), getExCell(robot), getFxCell(robot));
    }

    private InterruptHandler createGetGameInfoInterrupt(Robot robot) {
        return new GetGameInfoInterrupt(robot.getEntrant().getGame(), getDxCell(robot), getExCell(robot), getFxCell(robot));
    }

    private InterruptHandler createGetTargetInfoInterrupt(Robot robot) {
        return new CopyMemoryCellsInterrupt(getRegisters(robot).getTargetHeading(), getExCell(robot),
                getRegisters(robot).getTargetThrottle(), getFxCell(robot));
    }

    private InterruptHandler createGetTargetIdInterrupt(Robot robot) {
        return new CopyMemoryCellsInterrupt(getRegisters(robot).getTargetId(), getFxCell(robot));
    }

    private InterruptHandler createFindAngleInterrupt(Robot robot) {
        return new FindAngleInterrupt(getExCell(robot), getFxCell(robot), getAxCell(robot), robot);
    }

    private MemoryCell getAxCell(Robot robot) {
        return getRegisters(robot).getAx();
    }

    private InterruptHandler createGetTimerInterrupt(Robot robot) {
        return new GetTimerInterrupt(robot.getEntrant().getGame(), getExCell(robot), getFxCell(robot));
    }

    private InterruptHandler createGetTransponderIdInterrupt(Robot robot) {
        return new GetIdInterrupt(robot.getTransponder(), getFxCell(robot));
    }

    private InterruptHandler createOverburnInterrupt(Robot robot) {
        return new OverburnInterrupt(robot, getAxCell(robot));
    }

    private InterruptHandler createSetKeepshiftInterrupt(Robot robot) {
        return new SetKeepshiftInterrupt(robot.getTurret(), getAxCell(robot));
    }

    private InterruptHandler createLocateInterrupt(Robot robot) {
        return new LocateInterrupt(robot,
                getExCell(robot), getFxCell(robot));
    }

    private MemoryCell getFxCell(Robot robot) {
        return getRegisters(robot).getFx();
    }

    private MemoryCell getExCell(Robot robot) {
        return getRegisters(robot).getEx();
    }

    private Registers getRegisters(Robot robot) {
        return robot.getComputer().getRegisters();
    }

    private InterruptHandler createResetInterrupt(Robot robot) {
        return new ResetInterrupt(robot.getComputer());
    }

    private InterruptHandler createDestructInterrupt(Robot robot) {
        return new DestructInterrupt(robot);
    }

    InvalidInterrupt createInvalidInterrupt(Robot robot) {
        return new InvalidInterrupt(robot.getComputer().getErrorHandler());
    }

    public Map<Integer, InterruptHandler> createInterruptTable(Robot robot) {
        Map<Integer, InterruptHandler> interrupts = new HashMap<Integer, InterruptHandler>();

        interrupts.put(0, createDestructInterrupt(robot));
        interrupts.put(1, createResetInterrupt(robot).costs(10));
        interrupts.put(2, createLocateInterrupt(robot).costs(5));
        interrupts.put(3, createSetKeepshiftInterrupt(robot).costs(2));
        interrupts.put(4, createOverburnInterrupt(robot).costs(1));
        interrupts.put(5, createGetTransponderIdInterrupt(robot).costs(2));
        interrupts.put(6, createGetTimerInterrupt(robot).costs(2));
        interrupts.put(7, createFindAngleInterrupt(robot).costs(32));
        interrupts.put(8, createGetTargetIdInterrupt(robot).costs(1));
        interrupts.put(9, createGetTargetInfoInterrupt(robot).costs(2));
        interrupts.put(10, createGetGameInfoInterrupt(robot).costs(4));
        interrupts.put(11, createGetRobotInfoInterrupt(robot).costs(5));
        interrupts.put(12, createGetCollisionsInterrupt(robot).costs(1));
        interrupts.put(13, createResetCollisionCountInterrupt(robot).costs(1));
        interrupts.put(14, createTransmitInterrupt(robot).costs(1));
        interrupts.put(15, createRecieveInterrupt(robot).costs(1));
        interrupts.put(16, createGetQueueSizeInterrupt(robot).costs(1));
        interrupts.put(17, createResetQueueInterrupt(robot).costs(1));
        interrupts.put(18, createGetRobotStatisticsInterrupt(robot).costs(3));
        interrupts.put(19, createResetMetersInterrupt(robot).costs(1));
        for (InterruptHandler handler : interrupts.values()) {
            handler.setComputer(robot.getComputer());
        }
        return new MapWithDefaultValue<Integer, InterruptHandler>(interrupts, createInvalidInterrupt(robot));
    }
}
