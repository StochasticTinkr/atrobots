package net.virtualinfinity.atrobots.debugger;

import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.computer.DebugListener;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public abstract class BaseDebugListener implements DebugListener {
    private final Set<Integer> breakpoints = new HashSet<Integer>();

    protected Entrant getEntrant(Computer computer) {
        return computer.getEntrant();
    }

    protected static Game getGame(Computer computer) {
        return computer.getEntrant().getGame();
    }

    protected static Robot getRobot(Computer computer) {
        return computer.getEntrant().getCurrentRobot();
    }


    public void beforeInstruction(Computer computer) {
        if (isBreakpoint(computer)) {
            handleBreakpoint(computer);
        }
    }

    protected void handleBreakpoint(Computer computer) {
    }


    public boolean isBreakpoint(Computer computer) {
        return breakpoints.contains(computer.getInstructionPointer());
    }

    public void afterInstruction(Computer computer) {
    }

    public void setBreakpoint(int breakpoint) {
        breakpoints.add(breakpoint);
    }

    public void removeBreakpoint(int breakpoint) {
        breakpoints.remove(breakpoint);
    }
}
