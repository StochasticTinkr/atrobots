package net.virtualinfinity.atrobots.computer;

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

    void memoryBoundsError(int address);

    void writeToRomError();

    void notAddressableError();
}
