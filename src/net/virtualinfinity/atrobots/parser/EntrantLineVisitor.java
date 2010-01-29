package net.virtualinfinity.atrobots.parser;

import net.virtualinfinity.atrobots.CompilerOutput;
import net.virtualinfinity.atrobots.DebugInfo;
import net.virtualinfinity.atrobots.HardwareSpecification;
import net.virtualinfinity.atrobots.atsetup.*;
import net.virtualinfinity.atrobots.computer.Program;

import java.util.*;

/**
 * @author Daniel Pitts
 */
public class EntrantLineVisitor implements LineVisitor {
    private Errors errors = new Errors();
    private int variables;
    private List<Short> programCode = new ArrayList<Short>();
    private Map<String, Symbol> symbols = new HashMap<String, Symbol>();
    private Map<Integer, Token> unresolved = new LinkedHashMap<Integer, Token>();
    private String message;
    private int maxProcessorSpeed;
    private Map<String, Integer> configs = new HashMap<String, Integer>();
    private List<String> lines = new ArrayList<String>();
    private List<Integer> instructionLineNumber = new ArrayList<Integer>();
    private final DebugInfo debugInfo = new DebugInfo();

    {
        setConfig(HardwareSpecification.SCANNER, 5);
        setConfig(HardwareSpecification.WEAPON, 2);
        setConfig(HardwareSpecification.ARMOR, 2);
        setConfig(HardwareSpecification.ENGINE, 2);
        setConfig(HardwareSpecification.HEATSINKS, 1);
        setConfig(HardwareSpecification.MINES, 0);
        setConfig(HardwareSpecification.SHIELD, 0);
    }

    {
        for (AtRobotRegister register : AtRobotRegister.values()) {
            addReference(register.address, register.name());
        }
        addConstant(Short.MAX_VALUE, "MAXINT");
        addConstant(Short.MIN_VALUE, "MININT");
        for (AtRobotPort port : AtRobotPort.values()) {
            addConstants(port.value, port.names);
        }
        for (AtRobotInstruction instruction : AtRobotInstruction.values()) {
            addConstants(instruction.value, instruction.names);
        }
        for (AtRobotInterrupt interrupt : AtRobotInterrupt.values()) {
            addConstants(interrupt.value, interrupt.names);
        }
    }

    private void addConstants(int value, Collection<String> names) {
        for (String name : names) {
            addConstant(value, name);
        }
    }

    private void addConstant(int value, String name) {
        symbols.put(name.toLowerCase(), new Symbol(AtRobotMicrocodes.CONSTANT, (short) value));
    }

    public void expectedDigit(int column, int lineNumber) {
        errors.add("Expected digit.", lineNumber, column);
    }

    public void expectedDirectiveName(int column, int lineNumber) {
        errors.add("Expected directive.", lineNumber, column);
    }

    public void unexpectedCharacter(int column, int lineNumber) {
        errors.add("Unexpected character", lineNumber, column);
    }

    public void invalidVariableNameChar(int column, int lineNumber) {
        errors.add("Invalid character in variable name", lineNumber, column);
    }

    public void expectedDeviceName(int column, int lineNumber) {
        errors.add("Expected device name", lineNumber, column);
    }

    public void expectedMoreTokens(int lineNumber) {
        errors.add("Expected more tokens on line", lineNumber);
    }

    public void invalidNumber(int lineNumber) {
        errors.add("Not a valid number", lineNumber);
    }

    public void defineVariable(String variableName) {
        addReference(variables + 128, variableName);
        variables++;
    }

    private void addReference(int value, String variableName) {
        debugInfo.addVariable(value, variableName);
        symbols.put(variableName.toLowerCase(), new Symbol(AtRobotMicrocodes.REFERENCE, (short) value));
    }

    public void numberedLabel(int value, int lineNumber) {
        programCode.add((short) value);
        alignProgram();
        programCode.set(programCode.size() - 1, AtRobotMicrocodes.NUMBERED_LABEL);
        markLineNumber(lineNumber);
    }

    public void maxProcessorSpeed(int speed) {
        this.maxProcessorSpeed = speed;
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setConfig(String name, int value) {
        configs.put(name.toLowerCase(), value);
    }

    public void machineCode(int[] values, int lineNumber) {
        for (int value : values) {
            programCode.add((short) value);
        }
        alignProgram();
        markLineNumber(lineNumber);
    }

    private void alignProgram() {
        while (programCode.size() % 4 != 0) {
            programCode.add((short) 0);
        }
    }

    private void markLineNumber(int lineNumber) {
        debugInfo.setLineForInstructionPointer(instructionLineNumber.size(), lineNumber + ": " + lines.get(lines.size() - 1));
        instructionLineNumber.add(lineNumber);
    }

    public void label(String line) {
        symbols.put(line, new Symbol(AtRobotMicrocodes.RESOLVED_LABEL, (short) (programCode.size() / 4)));
    }

    public void tokenizedLine(List<Token> tokens, int lineNumber) {
        addTokensToProgram(tokens);
        alignProgram();
        addMicrocodeToProgram(tokens);
        markLineNumber(lineNumber);
    }

    public void unknownDirective(String directive, int lineNumber) {
        errors.add("Unknown directive: #" + directive, lineNumber);
    }

    public void appendRawLine(String line) {
        lines.add(line);
    }

    private void addMicrocodeToProgram(List<Token> tokens) {
        programCode.set(programCode.size() - 1, getMicrocodeFor(tokens));
    }

    private void addTokensToProgram(List<Token> tokens) {
        for (Token token : tokens) {
            addTokenToProgram(token);
        }
    }

    private void addTokenToProgram(Token token) {
        programCode.add(token.getValue(symbols));
        if (token.isUnresolved(symbols)) {
            unresolved.put(programCode.size() - 1, token);
        }
    }

    private short getMicrocodeFor(List<Token> tokens) {
        short microcode = 0;
        for (ListIterator<Token> iterator = tokens.listIterator(tokens.size()); iterator.hasPrevious();) {
            microcode = (short) ((microcode << 4) | iterator.previous().getMicrocode(symbols));
        }
        return microcode;
    }

    public void resolve() {
        int sum = 0;
        for (int value : configs.values()) {
            sum += value;
        }
        if (sum > 12) {
            errors.info("Config points too high. " + sum + " out of a max of 12.");
        }
        for (Map.Entry<Integer, Token> entry : unresolved.entrySet()) {
            if (entry.getValue().isUnresolved(symbols)) {
                errors.add("Unresolved symbol: " + entry.getValue().toString(), entry.getValue().getLineNumber());
            } else {
                resolve(entry.getKey(), entry.getValue());
            }
        }
    }

    private void resolve(int address, Token token) {
        adjustValue(address, token);
        adjustMicrocode(address, token);
    }

    private void adjustValue(int address, Token token) {
        programCode.set(address, token.getValue(symbols));
    }

    private void adjustMicrocode(int address, Token token) {
        final int microcodeIndex = address | 3;
        final int opOffset = address & 3;
        final short oldMicrocode = programCode.get(microcodeIndex);
        programCode.set(microcodeIndex, (short) ((oldMicrocode & (0x0FFF >> ((3 - opOffset) * 4))) | (token.getMicrocode(symbols) << (opOffset * 4))));
    }

    public short[] getProgramCode() {
        final short[] programCode = new short[this.programCode.size()];
        int i = 0;
        for (short value : this.programCode) {
            programCode[i++] = value;
        }
        return programCode;
    }

    public Program createProgram() {
        if (hasErrors()) {
            return null;
        } else {
            return new Program(getProgramCode());
        }
    }

    public HardwareSpecification createHardwareSpecification() {
        if (hasErrors()) {
            return null;
        } else {
            return new HardwareSpecification(configs);
        }
    }

    private boolean hasErrors() {
        return errors.hasErrors();
    }

    public Errors getErrors() {
        return errors;
    }

    public List<String> getLines() {
        return lines;
    }

    public DebugInfo getDebugInfo() {
        return debugInfo;
    }

    public CompilerOutput createCompilerOutput() {
        return new CompilerOutput(getErrors(), createProgram(), createHardwareSpecification(), getMaxProcessorSpeed(), getDebugInfo());
    }

    public class Symbol {
        private final short microcode;
        private final short value;

        public Symbol(short microcode, short value) {
            this.microcode = microcode;
            this.value = value;
        }

        public short getValue() {
            return value;
        }

        public short getMicrocode() {
            return microcode;
        }
    }
}
