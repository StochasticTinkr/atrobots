package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.interrupts.InterruptHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class HardwareBus {

    private Map<Integer, PortHandler> ports;
    private Map<Integer, InterruptHandler> interrupts;
    private final Collection<Resetable> resetables = new ArrayList<Resetable>();
    private final Heading desiredHeading = new Heading();
    private Temperature autoShutDown = Temperature.fromLogScale(350);
    private Computer computer;
    private Robot robot;

    public Map<Integer, PortHandler> getPorts() {
        return ports;
    }

    public void setPorts(Map<Integer, PortHandler> ports) {
        this.ports = ports;
    }

    public Map<Integer, InterruptHandler> getInterrupts() {
        return interrupts;
    }

    public void setInterrupts(Map<Integer, InterruptHandler> interrupts) {
        this.interrupts = interrupts;
    }

    public void callInterrupt(int interruptNumber) {
        interrupts.get(interruptNumber).call();
    }

    public short readPort(int portNumber) {
        return ports.get(portNumber).read();
    }

    public void writePort(int portNumber, short value) {
        ports.get(portNumber).write(value);
    }

    public void reset() {
        for (Resetable resetable : resetables) {
            resetable.reset();
        }
    }

    public void addResetable(Resetable resetable) {
        resetables.add(resetable);
    }

    public Heading getDesiredHeading() {
        return desiredHeading;
    }

    public void preInstruction() {
        computer.getRegisters().getDesiredSpeed().set((short) robot.getThrottle().getDesiredPower());
        computer.getRegisters().getDesiredHeading().set((short) (desiredHeading.getAngle().getBygrees() & 255));
        computer.getRegisters().getTurretOffset().set((short) robot.getTurretShift());
        computer.getRegisters().getAccuracy().set((short) robot.getTurret().getScanner().getAccuracy());
        computer.getRegisters().getMeters().set((short) Math.round(robot.getOdometer().getDistance().getMeters()));
    }


    public void setComputer(Computer computer) {
        this.computer = computer;

    }

    public void setRobot(Robot robot) {
        this.robot = robot;
        desiredHeading.setAngle(robot.getHeading().getAngle());
    }

    public void checkHeat() {
        if (robot.getHeat().getTemperature().compareTo(autoShutDown) >= 0) {
            shutDown();
        }
        if (computer.isShutDown() && robot.getHeat().getTemperature().compareTo(autoShutDown.minus(Temperature.fromLogScale(50))) < 0) {
            startUp();
        }
    }

    private void startUp() {
        computer.startUp();
    }

    private void shutDown() {
        desiredHeading.setAngle(robot.getHeading().getAngle());
        robot.getThrottle().setDesiredPower(0);
        computer.shutDown();
    }

    public int getShutdownLevel() {
        return (int) Math.round(autoShutDown.getLogScale());
    }


    public void setShutdownLevel(int value) {
        autoShutDown = Temperature.fromLogScale(value);
    }
}
