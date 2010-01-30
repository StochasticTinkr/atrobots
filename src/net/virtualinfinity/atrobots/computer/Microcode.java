package net.virtualinfinity.atrobots.computer;

import java.util.Arrays;

/**
 * Microcode handlers.
 *
 * @author Daniel Pitts
 */
public enum Microcode {
    Constant {
        public short getValue(Computer computer, int opnumber) {
            return computer.getConstant(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            computer.notAddressableError();
            return 0;
        }},
    Dereference {
        public short getValue(Computer computer, int opnumber) {
            return computer.getDeferencedValue(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            return computer.getConstant(opnumber);
        }},
    DoubleDereference {
        public short getValue(Computer computer, int opnumber) {
            return computer.getDoubleDereferencedValue(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            return computer.getDeferencedValue(opnumber);
        }},
    NumberedLabel {
        public short getValue(Computer computer, int opnumber) {
            return computer.getNumberedLabelValue(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            computer.notAddressableError();
            return 0;
        }},
    ResolvedLabel {
        public short getValue(Computer computer, int opnumber) {
            return computer.getLabelValue(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            computer.notAddressableError();
            return 0;
        }},
    UnresolvedLabel {
        public short getValue(Computer computer, int opnumber) {
            return computer.getUnresolvedLabelValue(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            computer.notAddressableError();
            return 0;
        }},
    Invalid {
        public short getValue(Computer computer, int opnumber) {
            computer.invalidMicrocodeError();
            return computer.getConstant(opnumber);
        }
        public int getAddress(Computer computer, int opnumber) {
            computer.notAddressableError();
            return 0;
        }};

    private static final Microcode[] codes = new Microcode[15];

    static {
        Arrays.fill(codes, Invalid);
        codes[0] = Constant;
        codes[1] = Dereference;
        codes[2] = NumberedLabel;
        codes[3] = UnresolvedLabel;
        codes[4] = ResolvedLabel;
        codes[8] = Dereference;
        codes[9] = DoubleDereference;
    }

    public static Microcode get(int microcode) {
        return codes[microcode & 15];
    }

    public abstract short getValue(Computer computer, int opnumber);

    public boolean isValid() {
        return this != Invalid && this != UnresolvedLabel;
    }

    public boolean isAddressible() {
        return this == Dereference || this == DoubleDereference;
    }

    public boolean hasValue() {
        return isAddressible() || this == Constant;
    }

    public abstract int getAddress(Computer computer, int opnumber);

}
