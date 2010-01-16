package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.Flags;
import net.virtualinfinity.atrobots.atsetup.AtRobotRegister;
import static net.virtualinfinity.atrobots.atsetup.AtRobotRegister.*;
import static net.virtualinfinity.atrobots.atsetup.AtRobotRegisterAddresses.*;

/**
 * @author Daniel Pitts
 */
public class Registers {
    private final Flags flags;
    private final MemoryCell ax;
    private final MemoryCell bx;
    private final MemoryCell cx;
    private final MemoryCell dx;
    private final MemoryCell ex;
    private final MemoryCell fx;
    private final MemoryCell sp;
    private final MemoryCell desiredSpeed;
    private final MemoryCell desiredHeading;
    private final MemoryCell turretOffset;
    private final MemoryCell accuracy;
    private final MemoryCell swap;
    private final MemoryCell targetId;
    private final MemoryCell targetHeading;
    private final MemoryCell targetThrottle;
    private final MemoryCell collisionCount;
    private final MemoryCell meters;
    private final MemoryCell communicationQueueHead;
    private final MemoryCell communicationQueueTail;
    private final MemoryCell targetVelocity;

    public Registers(Memory memory) {
        flags = new Flags(getCell(memory, FLAGS));
        ax = getCell(memory, AX);
        bx = getCell(memory, BX);
        cx = getCell(memory, CX);
        dx = getCell(memory, DX);
        ex = getCell(memory, EX);
        fx = getCell(memory, FX);
        sp = getCell(memory, SP);
        desiredSpeed = memory.getCell(DESIRED_SPEED_ADDRESS);
        desiredHeading = memory.getCell(DESIRED_HEADING_ADDRESS);
        turretOffset = memory.getCell(TURRET_OFFSET_ADDRESS);
        accuracy = memory.getCell(ACCURACY_ADDRESS);
        swap = memory.getCell(SWAP_ADDRESS);
        targetId = memory.getCell(TARGET_ID_ADDRESS);
        targetHeading = memory.getCell(TARGET_HEADING_ADDRESS);
        targetThrottle = memory.getCell(TARGET_THROTTLE_ADDRESS);
        collisionCount = memory.getCell(COLLISION_COUNT_ADDRESS);
        meters = memory.getCell(METERS_ADDRESS);
        communicationQueueHead = memory.getCell(COMMUNICATION_QUEUE_HEAD_ADDRESS);
        communicationQueueTail = memory.getCell(COMMUNICATION_QUEUE_TAIL_ADDRESS);
        targetVelocity = memory.getCell(TARGET_VELOCITY_ADDRESS);
    }

    private static MemoryCell getCell(Memory memory, AtRobotRegister register) {
        return memory.getCell(register.address);
    }

    public MemoryCell getStackPointerCell() {
        return sp;
    }

    public Flags getFlags() {
        return flags;
    }

    public MemoryCell getAx() {
        return ax;
    }

    public MemoryCell getBx() {
        return bx;
    }

    public MemoryCell getCx() {
        return cx;
    }

    public MemoryCell getDx() {
        return dx;
    }

    public MemoryCell getEx() {
        return ex;
    }

    public MemoryCell getFx() {
        return fx;
    }

    public MemoryCell getSp() {
        return sp;
    }

    public MemoryCell getDesiredSpeed() {
        return desiredSpeed;
    }

    public MemoryCell getDesiredHeading() {
        return desiredHeading;
    }

    public MemoryCell getTurretOffset() {
        return turretOffset;
    }

    public MemoryCell getAccuracy() {
        return accuracy;
    }

    public MemoryCell getSwap() {
        return swap;
    }

    public MemoryCell getTargetId() {
        return targetId;
    }

    public MemoryCell getTargetHeading() {
        return targetHeading;
    }

    public MemoryCell getTargetThrottle() {
        return targetThrottle;
    }

    public MemoryCell getCollisionCount() {
        return collisionCount;
    }

    public MemoryCell getMeters() {
        return meters;
    }

    public MemoryCell getCommunicationQueueHead() {
        return communicationQueueHead;
    }

    public MemoryCell getCommunicationQueueTail() {
        return communicationQueueTail;
    }

    public MemoryCell getTargetVelocity() {
        return targetVelocity;
    }

    public String toString() {
        return "AX=" + getAx().signed()
                + ", BX=" + getBx().signed()
                + ", CX=" + getCx().signed()
                + ", DX=" + getDx().signed()
                + ", EX=" + getEx().signed()
                + ", FX=" + getFx().signed()
                + ", SP=" + getSp().signed()
                + ", FL=" + getFlags();
    }
}