package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public interface ComputerErrorHandler {
    void invalidMicrocodeError();

    void divideByZeroError();

    void genericError(short operandValue);

    void unknownInstructionError(short operandValue);

    void invalidInterruptError(short operandValue);

    void invalidPortError();

    void commQueueEmptyError();

    void memoryBoundsError(int address);

    void writeToRomError();

    void notAddressableError();

    void labelNotFound(short value);
}
