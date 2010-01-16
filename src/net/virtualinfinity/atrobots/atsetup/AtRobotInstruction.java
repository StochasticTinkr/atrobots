package net.virtualinfinity.atrobots.atsetup;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public enum AtRobotInstruction {
    NOP(0),
    ADD(1),
    SUB(2),
    OR(3),
    AND(4),
    XOR(5),
    NOT(6),
    MPY(7),
    DIV(8),
    MOD(9),
    RET(10),
    GSB(11, "CALL"),
    JMP(12, "GOTO"),
    JLS(13, "JB"),
    JGR(14, "JA"),
    JNE(15),
    JEQ(16, "JE"),
    XCHG(17, "SWAP"),
    DO(18),
    LOOP(19),
    CMP(20),
    TEST(21),
    SET(22, "MOV"),
    LOC(23),
    GET(24),
    PUT(25),
    INT(26),
    IPO(27, "IN"),
    OPO(28, "OUT"),
    DEL(29, "DELAY"),
    PUSH(30),
    POP(31),
    ERR(32, "ERROR"),
    INC(33),
    DEC(34),
    SHL(35),
    SHR(36),
    ROL(37),
    ROR(38),
    JZ(39),
    JNZ(40),
    JAE(41, "JGE"),
    JBE(42, "JLE"),
    SAL(43),
    SAR(44),
    NEG(45),
    JTL(46),;
    public final int value;
    public final Collection<String> names;

    private AtRobotInstruction(int value) {
        this.value = value;
        this.names = Collections.singleton(name());
    }

    private AtRobotInstruction(int value, String alternate) {
        this.value = value;
        this.names = Collections.unmodifiableCollection(Arrays.asList(name(), alternate));
    }
}
