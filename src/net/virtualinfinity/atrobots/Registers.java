package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Registers {
    private final MemoryCell flagsCell;
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
        flagsCell = memory.getCell(64);
        flags = new Flags(flagsCell);
        ax = memory.getCell(65);
        bx = memory.getCell(66);
        cx = memory.getCell(67);
        dx = memory.getCell(68);
        ex = memory.getCell(69);
        fx = memory.getCell(70);
        sp = memory.getCell(71);
        desiredSpeed = memory.getCell(0);
        desiredHeading = memory.getCell(1);
        turretOffset = memory.getCell(2);
        accuracy = memory.getCell(3);
        swap = memory.getCell(4);
        targetId = memory.getCell(5);
        targetHeading = memory.getCell(6);
        targetThrottle = memory.getCell(7);
        collisionCount = memory.getCell(8);
        meters = memory.getCell(9);
        communicationQueueHead = memory.getCell(10);
        communicationQueueTail = memory.getCell(11);
        targetVelocity = memory.getCell(13);
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
}