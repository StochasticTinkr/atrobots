package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public interface ComputerErrorHandler {
    void invalidMicrocodeError();

    void divideByZeroError();

    void genericError(short operandValue);

    void unknownInstructionError();

    void invalidInterruptError();

    void invalidPortError();

    void commQueueEmptyError();

    void memoryBoundsError();

    void writeToRomError();

    void notAddressableError();
}
