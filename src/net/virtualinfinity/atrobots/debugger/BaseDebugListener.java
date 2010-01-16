package net.virtualinfinity.atrobots.debugger;

import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.computer.DebugListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public abstract class BaseDebugListener implements DebugListener {
    private volatile boolean allPaused;

    protected void pauseAll() {
        allPaused = true;
    }

    private final Map<Integer, EntrantState> entrantStates = new HashMap<Integer, EntrantState>();

    protected EntrantState getEntrantState(Computer computer) {
        return getEntrantState(getEntrant(computer).getId());
    }

    private EntrantState getEntrantState(int id) {
        EntrantState state = entrantStates.get(id);
        if (state == null) {
            state = createEntrantState();
            entrantStates.put(id, state);
        }
        return state;
    }

    protected EntrantState createEntrantState() {
        return new EntrantState();
    }


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
        if (isBreakpoint(computer) || isPaused(computer)) {
            clearPaused(computer);
            handleBreakpoint(computer);
        }
    }

    protected boolean isPaused(Computer computer) {
        return allPaused || getEntrantState(computer).isPaused();
    }

    protected boolean pause(Computer computer) {
        return getEntrantState(computer).isPaused();
    }

    protected boolean pause(Integer computer) {
        return getEntrantState(computer).isPaused();
    }


    protected void clearPaused(Computer computer) {
        if (getEntrantState(computer).isPaused()) {
            getEntrantState(computer).clearPaused();
        } else {
            allPaused = false;
        }
    }

    private boolean isBreakpoint(Computer computer) {
        return getEntrantState(computer).isBreakpoint(computer);
    }

    protected abstract void handleBreakpoint(Computer computer);

    public void afterInstruction(Computer computer) {
    }

    public boolean isAllPaused() {
        return allPaused;
    }

    protected static class EntrantState {
        private Set<Integer> breakpoints = new HashSet<Integer>();
        private boolean paused;

        public boolean isBreakpoint(Computer computer) {
            return breakpoints.contains(computer.getInstructionPointer());
        }

        public void setBreakpoint(int breakpoint) {
            breakpoints.add(breakpoint);
        }

        public void removeBreakpoint(int breakpoint) {
            breakpoints.remove(breakpoint);
        }

        public boolean isPaused() {
            return paused;
        }

        public void clearPaused() {
            paused = false;
        }
    }

}
