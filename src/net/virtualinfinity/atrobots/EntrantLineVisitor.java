package net.virtualinfinity.atrobots;

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
    private AtRobotLineLexer lexer;
    {
        addReference(8, "COLCNT");
        addReference(9, "METERS");
        addReference(10, "COMBASE");
        addReference(11, "COMEND");
        addReference(64, "FLAGS");
        addReference(65, "AX");
        addReference(66, "BX");
        addReference(67, "CX");
        addReference(68, "DX");
        addReference(69, "EX");
        addReference(70, "FX");
        addReference(71, "SP");
        addConstants(Short.MAX_VALUE, "MAXINT");
        addConstants(Short.MIN_VALUE, "MININT");
        addConstants(0, "NOP", "I_DESTRUCT");
        addConstants(1, "ADD", "P_SPEDOMETER" ,"I_RESET");
        addConstants(2, "SUB", "P_HEAT" ,"I_LOCATE");
        addConstants(3, "OR ", "P_COMPASS" ,"I_KEEPSHIFT");
        addConstants(4, "AND", "P_TURRET_OFS" ,"I_OVERBURN");
        addConstants(5, "XOR", "P_TURRET_ABS" ,"I_ID");
        addConstants(6, "NOT", "P_DAMAGE", "P_ARMOR" ,"I_TIMER");
        addConstants(7, "MPY", "P_SCAN" ,"I_ANGLE");
        addConstants(8, "DIV", "P_ACCURACY" ,"I_TID", "I_TARGETID");
        addConstants(9, "MOD", "P_RADAR" ,"I_TINFO", "I_TARGETINFO");
        addConstants(10, "RET", "P_RANDOM", "P_RAND" ,"I_GINFO", "I_GAMEINFO");
        addConstants(11, "GSB", "CALL", "P_THROTTLE" ,"I_RINFO", "I_ROBOTINFO");
        addConstants(12, "JMP", "GOTO", "P_OFS_TURRET", "P_TROTATE" ,"I_COLLISIONS");
        addConstants(13, "JLS", "JB", "P_ABS_TURRET", "P_TAIM" ,"I_RESETCOLCNT");
        addConstants(14, "JGR", "JA", "P_STEERING" ,"I_TRANSMIT");
        addConstants(15, "JNE", "P_WEAP", "P_WEAPON","P_FIRE" ,"I_RECEIVE");
        addConstants(16, "JEQ", "JE", "P_SONAR" ,"I_DATAREADY");
        addConstants(17, "XCHG", "SWAP", "P_ARC", "P_SCANARC" ,"I_CLEARCOM");
        addConstants(18, "DO" , "P_OVERBURN" ,"I_KILLS", "I_DEATHS");
        addConstants(19, "LOOP" , "P_TRANSPONDER" ,"I_CLEARMETERS");
        addConstants(20, "CMP" , "P_SHUTDOWN");
        addConstants(21, "TEST" , "P_CHANNEL");
        addConstants(22, "SET", "MOV", "P_MINELAYER");
        addConstants(23, "LOC" , "P_MINETRIGGER");
        addConstants(24, "GET" , "P_SHIELD","P_SHIELDS");
        addConstants(25, "PUT");
        addConstants(26, "INT");
        addConstants(27, "IPO", "IN");
        addConstants(28, "OPO", "OUT");
        addConstants(29, "DEL", "DELAY");
        addConstants(30, "PUSH");
        addConstants(31, "POP");
        addConstants(32, "ERR", "ERROR");
        addConstants(33, "INC");
        addConstants(34, "DEC");
        addConstants(35, "SHL");
        addConstants(36, "SHR");
        addConstants(37, "ROL");
        addConstants(38, "ROR");
        addConstants(39, "JZ");
        addConstants(40, "JNZ");
        addConstants(41, "JAE", "JGE");
        addConstants(42, "JBE", "JLE");
        addConstants(43, "SAL");
        addConstants(44, "SAR");
        addConstants(45, "NEG");
        addConstants(46, "JTL");
    }
    private void addConstants(int value, String...names) {
        for (String n : names) {
            symbols.put(n.toLowerCase(), new Symbol((short) 0, (short) value));
        }
    }

    public void expectedDigit(int column) {
        errors.add("Expected digit.", lexer.getLineNumber(), column);
    }

    public void expectedDirectiveName(int column) {
        errors.add("Expected directive.", lexer.getLineNumber(), column);
    }

    public void unexpectedCharacter(int column) {
        errors.add("Unexpected character", lexer.getLineNumber(), column);
    }

    public void invalidVariableNameChar(int column) {
        errors.add("Invalid character in variable name", lexer.getLineNumber(), column);
    }

    public void expectedDeviceName(int column) {
        errors.add("Expected device name", lexer.getLineNumber(), column);
    }

    public void expectedMoreTokens() {
        errors.add("Expected more tokens on line", lexer.getLineNumber());
    }

    public void invalidNumber() {
        errors.add("Not a valid number", lexer.getLineNumber());
    }

    public void defineVariable(String variableName) {
        addReference(variables + 128, variableName);
        variables++;
    }

    private void addReference(int value, String variableName) {
        symbols.put(variableName.toLowerCase(), new Symbol((short) 1, (short) value));
    }

    public void numberedLabel(int value) {
        programCode.add((short)value);
        alignProgram();
        programCode.set(programCode.size()-1, (short)2);
    }

    public void maxProcessorSpeed(int speed) {
    }

    public void setMessage(String message) {
    }

    public void setConfig(String name, int value) {
    }

    public void machineCode(int[] values) {
        for (int value : values) {
            programCode.add((short) value);
        }
        alignProgram();
    }

    private void alignProgram() {
        while (programCode.size() % 4 != 0) {
            programCode.add((short)0);
        }
    }

    public void label(String line) {
        symbols.put(line, new Symbol((short) 4, (short) (programCode.size()/4)));
    }

    public void tokenizedLine(List<Token> tokens) {
        addTokensToProgram(tokens);
        alignProgram();
        addMicrocodeToProgram(tokens);
    }

    public void unknownDirective(String directive) {
        errors.add("Unknown directive: #" + directive, lexer.getLineNumber());
    }

    private void addMicrocodeToProgram(List<Token> tokens) {
        programCode.set(programCode.size()-1, getMicrocodeFor(tokens));
    }

    private void addTokensToProgram(List<Token> tokens) {
        for (Token token: tokens) {
            addTokenToProgram(token);
        }
    }

    private void addTokenToProgram(Token token) {
        programCode.add(token.getValue(symbols));
        if (token.isUnresolved(symbols)) {
            unresolved.put(programCode.size()-1, token);
        }
    }

    private short getMicrocodeFor(List<Token> tokens) {
        short microcode =0;
        for (ListIterator<Token> iterator = tokens.listIterator(tokens.size()); iterator.hasPrevious();) {
            microcode = (short) ((microcode << 4) | iterator.previous().getMicrocode(symbols));
        }
        return microcode;
    }

    public void resolve() {
        for (Map.Entry<Integer, Token> entry: unresolved.entrySet()) {
            if (entry.getValue().isUnresolved(symbols)) {
                errors.add("Unresolved symbol: " + entry.getValue().toString() , entry.getValue().getLineNumber());
            }
            programCode.set(entry.getKey(), entry.getValue().getValue(symbols));
        }
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
        return hasErrors() ? null : new Program(getProgramCode());
    }

    public HardwareSpecification createHardwareSpecification() {
        return hasErrors() ? null : new HardwareSpecification();
    }

    private boolean hasErrors() {
        return errors.hasErrors();
    }

    public Errors getErrors() {
        return errors;
    }

    public void setLexer(AtRobotLineLexer lexer) {
        this.lexer = lexer;
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
