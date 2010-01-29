package net.virtualinfinity.atrobots.parser;

import net.virtualinfinity.atrobots.atsetup.AtRobotMicrocodes;

import java.util.Map;

/**
 * @author Daniel Pitts
 */
public abstract class Token {
    private int lineNumber;

    public static Token parse(int lineNumber, String token) {
        final Token t = getToken(lineNumber, token);
        t.setLineNumber(lineNumber);
        return t;
    }

    private static Token getToken(int lineNumber, String token) {
        if (token.startsWith("[") && token.endsWith("]")) {
            return new Indirect(parse(lineNumber, token.substring(1, token.length() - 1)));
        }
        if (token.startsWith("@")) {
            return new Reference(AtRobotLineLexer.parseNumber(token.substring(1)));
        }
        if (Character.isDigit(token.charAt(0)) ||
                (token.charAt(0) == '-' && token.length() > 1 && Character.isDigit(token.charAt(1)))) {
            return new Constant(AtRobotLineLexer.parseNumber(token));
        }
        if (token.charAt(0) == '!') {
            return new Label(token);
        }
        return new Name(token);
    }

    abstract public short getValue(Map<String, EntrantLineVisitor.Symbol> symbols);

    abstract public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols);

    public boolean isUnresolved(Map<String, EntrantLineVisitor.Symbol> symbols) {
        return false;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    private static class Indirect extends Token {
        private final Token inner;

        public Indirect(Token inner) {
            this.inner = inner;
        }

        public short getValue(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return inner.getValue(symbols);
        }

        public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return (short) (AtRobotMicrocodes.INDIRECT_REFERENCE_MASK | inner.getMicrocode(symbols));
        }
    }

    private static class Constant extends Token {
        private int value;

        public Constant(int value) {
            this.value = value;
        }

        public short getValue(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return (short) value;
        }

        public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return AtRobotMicrocodes.CONSTANT;
        }
    }

    private static abstract class Resolvable extends Token {
        protected final String name;

        private Resolvable(String token) {
            name = token;
        }

        public short getValue(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return isUnresolved(symbols) ? 0 : symbols.get(name).getValue();
        }

        public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return isUnresolved(symbols) ? getUnresolvedMicrocode() : symbols.get(name).getMicrocode();
        }

        protected abstract short getUnresolvedMicrocode();

        public boolean isUnresolved(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return !symbols.containsKey(name);
        }

        public String toString() {
            return name;
        }
    }

    private static class Name extends Resolvable {
        public Name(String token) {
            super(token);
        }

        protected short getUnresolvedMicrocode() {
            return AtRobotMicrocodes.CONSTANT;
        }
    }

    private static class Label extends Resolvable {
        private Label(String token) {
            super(token);
        }

        protected short getUnresolvedMicrocode() {
            return AtRobotMicrocodes.UNRESOLVED_LABEL;
        }
    }

    public String toString() {
        return "<token>";
    }

    private static class Reference extends Constant {
        public Reference(int value) {
            super(value);
        }

        public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return AtRobotMicrocodes.REFERENCE;
        }
    }
}
