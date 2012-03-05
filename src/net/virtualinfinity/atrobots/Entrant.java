package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;
import net.virtualinfinity.atrobots.compiler.HardwareSpecification;
import net.virtualinfinity.atrobots.computer.DebugInfo;
import net.virtualinfinity.atrobots.computer.Program;

/**
 * Represents an entrant in a game.
 *
 * @author Daniel Pitts
 */
public class Entrant extends RobotFactory {
    private volatile RoundListener game;
    private final RobotScoreKeeper robotScoreKeeper = new RobotScoreKeeper();

    public Entrant(String name, Program program, HardwareSpecification hardwareSpecification, DebugInfo debugInfo, int maxProcessorSpeed, String message) {
        super(message, program, name, maxProcessorSpeed, debugInfo, hardwareSpecification);
    }

    /**
     * Creates an entrant based on this compiled output.
     *
     * @param name                  the name of the entrant to create.
     * @param atRobotCompilerOutput the compiler output
     * @return a configured entrant
     * @throws IllegalStateException if there are compiler errors.
     */
    public static Entrant createEntrant(String name, AtRobotCompilerOutput atRobotCompilerOutput) {
        if (atRobotCompilerOutput.hasErrors()) {
            throw new IllegalStateException("Can not create an entrant with errors. Check CompilerOutput.hasErrors() first.");
        }
        return new Entrant(name, atRobotCompilerOutput.getProgram(), atRobotCompilerOutput.getHardwareSpecification(), atRobotCompilerOutput.getDebugInfo(), atRobotCompilerOutput.getMaxProcessorSpeed(), atRobotCompilerOutput.getMessage());
    }

    /**
     * Get the game this entrant is in.
     *
     * @return the game.
     */
    public RoundListener getGame() {
        return game;
    }

    /**
     * Set the game that this entrant will enter.
     *
     * @param game the game.
     */
    public void setGame(RoundListener game) {
        this.game = game;
    }


    public RobotScoreKeeper getRobotScoreKeeper() {
        return robotScoreKeeper;
    }
}
