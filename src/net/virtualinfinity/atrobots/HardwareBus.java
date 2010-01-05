package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.interrupts.InterruptHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Represents all the hardware connected to a single robot.
 *
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

    /**
     * Get the port handler map.
     *
     * @return map of port numbers to port handler.
     */
    public Map<Integer, PortHandler> getPorts() {
        return ports;
    }

    /**
     * Set the port handler map.
     *
     * @param ports map of port numbers to port handler.
     */
    public void setPorts(Map<Integer, PortHandler> ports) {
        this.ports = ports;
    }

    /**
     * Get the interrupt handler map.
     *
     * @return map of interrupt numbers to interrupt handler.
     */
    public Map<Integer, InterruptHandler> getInterrupts() {
        return interrupts;
    }

    /**
     * Set the interrupt handler map.
     *
     * @param interrupts map of interrupt numbers to interrupt handler.
     */
    public void setInterrupts(Map<Integer, InterruptHandler> interrupts) {
        this.interrupts = interrupts;
    }

    /**
     * Call a specific interrupt.
     *
     * @param interruptNumber the interrupt to execute.
     */
    public void callInterrupt(int interruptNumber) {
        interrupts.get(interruptNumber).call();
    }

    /**
     * Read from a specific port.
     *
     * @param portNumber the port to read from
     * @return the value read.
     */
    public short readPort(int portNumber) {
        return ports.get(portNumber).read();
    }

    /**
     * Write to a specific port
     *
     * @param portNumber the port number
     * @param value      the value to write.
     */
    public void writePort(int portNumber, short value) {
        ports.get(portNumber).write(value);
    }

    /**
     * Reset all resetables in this hardward bus.
     */
    public void reset() {
        for (Resetable resetable : resetables) {
            resetable.reset();
        }
    }

    /**
     * Register a resetable.
     *
     * @param resetable a resetible to get reset when this bus is reset.
     */
    public void addResetable(Resetable resetable) {
        resetables.add(resetable);
    }

    /**
     * get the desired heading.
     *
     * @return the desired heading.
     */
    public Heading getDesiredHeading() {
        return desiredHeading;
    }

    /**
     * Update registers before executing an instruction.
     */
    public void preInstruction() {
//        computer.getRegisters().getDesiredSpeed().set((short) robot.getThrottle().getDesiredPower());
//        computer.getRegisters().getDesiredHeading().set((short) (desiredHeading.getAngle().getBygrees() & 255));
//        computer.getRegisters().getTurretOffset().set((short) robot.getTurretShift());
//        computer.getRegisters().getAccuracy().set((short) robot.getTurret().getScanner().getAccuracy());
//        computer.getRegisters().getMeters().set((short) Math.round(robot.getOdometer().getDistance()));
    }


    /**
     * Connect a computer.
     *
     * @param computer the computer
     */
    public void connectComputer(Computer computer) {
        this.computer = computer;
        computer.setHardwareBus(this);

    }

    /**
     * connect the robot.
     *
     * @param robot the robot
     */
    public void connectToRobot(Robot robot) {
        this.robot = robot;
        robot.setHardwareBus(this);
        desiredHeading.setAngle(robot.getHeading().getAngle());
    }

    /**
     * Check temperature against autoShutDown temp
     */
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
        robot.getShield().setActive(false);
        computer.shutDown();
    }

    /**
     * Get the temperature that shuts down computer.
     *
     * @return the temperature that shuts down computer.
     */
    public int getShutdownLevel() {
        return (int) Math.round(autoShutDown.getLogScale());
    }


    /**
     * Set the temperature that shuts down computer.
     *
     * @param value the temperature that shuts down computer.
     */
    public void setShutdownLevel(int value) {
        autoShutDown = Temperature.fromLogScale(value);
    }
}
