package net.virtualinfinity.atrobots;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps instruction pointer to a source line
 *
 * @author Daniel Pitts
 */
public class DebugInfo {
    Map<Integer, String> lineByInstructionPointer = new HashMap<Integer, String>();

    public String getLineForInstructionPointer(int instructionPointer) {
        return lineByInstructionPointer.get(instructionPointer);
    }

    public void setLineForInstructionPointer(int instructionPointer, String line) {
        lineByInstructionPointer.put(instructionPointer, line);
    }
}
