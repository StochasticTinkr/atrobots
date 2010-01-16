package net.virtualinfinity.atrobots.atsetup;

/**
 * TODO: Describe this class.
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
    public final int address;

    private AtRobotRegister(int address) {
        this.address = address;
    }

}
