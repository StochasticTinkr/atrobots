package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Program;
import net.virtualinfinity.atrobots.parser.Errors;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class CompilerOutput {
    private final Errors errors;
    private final Program program;
    private final HardwareSpecification hardwareSpecification;
    private final int maxProcessorSpeed;
    private final DebugInfo debugInfo;

    public CompilerOutput(Errors errors, Program program, HardwareSpecification hardwareSpecification, int maxProcessorSpeed, DebugInfo debugInfo) {
        this.errors = errors;
        this.program = program;
        this.hardwareSpecification = hardwareSpecification;
        this.maxProcessorSpeed = maxProcessorSpeed;
        this.debugInfo = debugInfo;
    }

    public CompilerOutput(Errors errors) {
        this(errors, null, null, -1, null);
    }

    public Entrant createEntrant(String name) {
        final Entrant entrant = new Entrant();
        entrant.setProgram(program);
        entrant.setName(name);
        entrant.setHardwareSpecification(hardwareSpecification);
        entrant.setDebugInfo(debugInfo);
        entrant.setMaxProcessorSpeed(maxProcessorSpeed);
        return entrant;
    }

    public Errors getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors.hasErrors();
    }

    public Program getProgram() {
        return program;
    }
}
