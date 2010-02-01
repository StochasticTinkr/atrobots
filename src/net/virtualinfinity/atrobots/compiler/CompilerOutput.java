package net.virtualinfinity.atrobots.compiler;

import net.virtualinfinity.atrobots.DebugInfo;
import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.HardwareSpecification;
import net.virtualinfinity.atrobots.computer.Program;

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
    private final String message;

    public CompilerOutput(Errors errors, Program program, HardwareSpecification hardwareSpecification, int maxProcessorSpeed, DebugInfo debugInfo, String message) {
        this.errors = errors;
        this.program = program;
        this.hardwareSpecification = hardwareSpecification;
        this.maxProcessorSpeed = maxProcessorSpeed;
        this.debugInfo = debugInfo;
        this.message = message;
    }

    public CompilerOutput(Errors errors) {
        this(errors, null, null, -1, null, null);
    }

    public Entrant createEntrant(String name) {
        final Entrant entrant = new Entrant();
        entrant.setProgram(program);
        entrant.setName(name);
        entrant.setHardwareSpecification(hardwareSpecification);
        entrant.setDebugInfo(debugInfo);
        entrant.setMaxProcessorSpeed(maxProcessorSpeed);
        entrant.setMessage(message);
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

    public String getMessage() {
        return message;
    }
}
