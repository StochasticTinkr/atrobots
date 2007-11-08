package net.virtualinfinity.atrobots;

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
            return new Indirect(parse(lineNumber, token.substring(1, token.length()-2)));
        }
        if (token.startsWith("@")) {
            return new Indirect(new Constant(AtRobotLineLexer.parseNumber(token.substring(1))));
        }
        if (Character.isDigit(token.charAt(0)) ||
                (token.charAt(0) == '-' && token.length() > 1 &&  Character.isDigit(token.charAt(1)))) {
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
            return (short) (8 | inner.getMicrocode(symbols));
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
            return 0;
        }
    }

    private static class Name extends Token {
        private final String name;

        public Name(String token) {
            name = token;
        }

        public short getValue(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return isUnresolved(symbols) ? 0 : symbols.get(name).getValue();
        }

        public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return isUnresolved(symbols) ? 1 : symbols.get(name).getMicrocode();
        }

        public boolean isUnresolved(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return !symbols.containsKey(name);
        }
        public String toString() {
            return name;
        }
    }

    private static class Label extends Token {
        private final String name;

        public Label(String token) {
            super();
            name = token;
        }

        public short getValue(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return isUnresolved(symbols) ? 0 : symbols.get(name).getValue();
        }

        public short getMicrocode(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return 4;
        }

        public boolean isUnresolved(Map<String, EntrantLineVisitor.Symbol> symbols) {
            return !symbols.containsKey(name);
        }
        public String toString() {
            return name;
        }
    }

    public String toString() {
        return "<token>";
    }
}
