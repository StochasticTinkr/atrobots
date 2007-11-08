package net.virtualinfinity.atrobots;

import java.util.List;

/**
 * @author Daniel Pitts
 */
public interface LineVisitor {
    void expectedDigit(int column);
    void numberedLabel(int value);
    void expectedDirectiveName(int column);
    void unexpectedCharacter(int column);
    void invalidVariableNameChar(int column);
    void defineVariable(String variableName);
    void maxProcessorSpeed(int speed);
    void setMessage(String message);
    void setConfig(String name, int value);
    void expectedDeviceName(int column);
    void expectedMoreTokens();
    void invalidNumber();
    void machineCode(int[] values);
    void label(String line);
    void tokenizedLine(List<Token> tokens);
}
