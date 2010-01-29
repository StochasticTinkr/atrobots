package net.virtualinfinity.atrobots.atsetup;

/**
 * A mapping between register symbol names and addresses.
 *
 * @author Daniel Pitts
 */
public enum AtRobotRegister {
    COLCNT(8),
    METERS(9),
    COMBASE(10),
    COMEND(11),
    FLAGS(64),
    AX(65),
    BX(66),
    CX(67),
    DX(68),
    EX(69),
    FX(70),
    SP(71),;
    /**
     * The address of this register.
     */
    public final int address;

    private AtRobotRegister(int address) {
        this.address = address;
    }
}
