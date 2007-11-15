package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class Program {
    private short[] programCode;

    public Program(short[] programCode) {
        this.programCode = programCode;
    }

    public MemoryArray createProgramMemory() {
        final ReadOnlyMemoryArray readOnlyMemoryArray = new ReadOnlyMemoryArray(programCode.length);
        readOnlyMemoryArray.flash(programCode);
        return readOnlyMemoryArray;
    }
}
