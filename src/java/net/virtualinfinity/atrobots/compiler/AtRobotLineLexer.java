package net.virtualinfinity.atrobots.compiler;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Does lexical analysis on lines from an AT-Robots 2 source code.
 *
 * @author Daniel Pitts
 */
public class AtRobotLineLexer {
    private final LineNumberReader reader;
    private final LineVisitor lineVisitor;
    private boolean stopProcessing;
    private int lockType;
    private int lockData;
    private int lockPos = -1;
    private byte[] lockKey;

    public AtRobotLineLexer(LineNumberReader reader, LineVisitor lineVisitor) {
        this.reader = reader;
        this.lineVisitor = lineVisitor;
    }

    public void visitAllLines() throws IOException {
        stopProcessing = false;
        String line;
        //noinspection NestedAssignment
        while (!(stopProcessing || (line = reader.readLine()) == null)) {
            visitLine(unlock(line));
        }
    }

    private String unlock(String line) {
        if (lockType == 0) {
            return line;
        }
        try {
            if (lockType < 3) {
                lockPos = -1;
            }
            final byte[] bytes = line.getBytes("ASCII");
            for (int i = 0; i < bytes.length ; ++i ) {
                ++lockPos;
                if (lockPos >= lockKey.length) {
                    lockPos = 0;
                }
                if (lockType == 3) {
                    --bytes[i];
                    bytes[i] ^= lockData;
                } else if (lockType == 2) {
                    bytes[i] ^=  1;
                }
                bytes[i] ^= lockKey[lockPos];
                lockData = bytes[i] & 15;
            }
            return new String(bytes, "ASCII");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void visitLine(String inputLine) throws IOException {
        String line = inputLine;
        lineVisitor.appendRawLine(line);
        final int commentStart = line.indexOf(';');
        if (commentStart >= 0) {
            line = line.substring(0, commentStart);
        }
        int i = 0;
        while (i < line.length() && isSeparator(line.charAt(i))) {
            ++i;
        }
        line = line.substring(i);
        if (line.length() != 0) {
            switch (line.charAt(0)) {
                case '#':
                    visitDirective(line);
                    break;
                case ':':
                    visitNumberLabel(line);
                    break;
                case '*':
                    visitMachineCode(line);
                    break;
                case '!':
                    visitLabel(line);
                    break;
                default:
                    visitNormalLine(line);
                    break;
            }
        }
    }

    private void visitLabel(String line) {
        int i = 0;
        while (i < line.length()) {
            if (isSeparator(line.charAt(i))) {
                line = line.substring(0, i);
                break;
            }
            ++i;
        }
        lineVisitor.label(line, getLineNumber());
    }

    private void visitNormalLine(String line) {
        final List<Token> tokens = new ArrayList<>(4);
        int i = 0;
        while (i < line.length()) {
            while (i < line.length() && isSeparator(line.charAt(i))) {
                i++;
            }
            final int last = i;
            while (i < line.length() && !isSeparator(line.charAt(i))) {
                i++;
            }
            if (last != i) {
                tokens.add(Token.parse(getLineNumber(), line.substring(last, i).toLowerCase()));
            }
        }
        lineVisitor.tokenizedLine(tokens, getLineNumber());
    }

    private static boolean isSeparator(char c) {
        return "0123456789ABCDEFGHJIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#![]:@*_-$".indexOf(c) < 0;
    }

    private void visitMachineCode(String line) {
        final String[] tokens = line.toLowerCase().split("[^0-9h]+");
        if (tokens.length != 4) {
            lineVisitor.expectedMoreTokens(getLineNumber());
            return;
        }
        final int[] values = new int[4];
        for (int i = 0; i < 4; ++i) {
            values[i] = parseNumber(tokens[i]);
            if (values[i] == Integer.MIN_VALUE) {
                lineVisitor.invalidNumber(getLineNumber());
                return;
            }
        }
        lineVisitor.machineCode(values, getLineNumber());
    }

    public static int parseNumber(String inputToken) {
        String token = inputToken;
        final boolean negative = token.charAt(0) == '-';
        if (negative) {
            token = token.substring(1);
        }
        final int radix;
        if (token.endsWith("h")) {
            token = token.substring(0, token.length() - 1);
            radix = 16;
        } else if (token.startsWith("0x") || token.startsWith("0X")) {
            token = token.substring(2);
            radix = 16;
        } else {
            radix = 10;
        }

        int value = 0;
        for (int i = 0; i < token.length(); ++i) {
            final int digit = Character.digit(token.charAt(i), radix);
            if (digit < 0) {
                return Integer.MIN_VALUE;
            }
            value = value * radix + digit;
        }
        return negative ? -value : value;
    }

    private void visitNumberLabel(String line) {
        final String number = line.substring(1);
        int value = 0;
        for (int i = 0; i < number.length(); ++i) {
            if (isSeparator(number.charAt(i))) {
                break;
            }
            if (!Character.isDigit(number.charAt(i))) {
                lineVisitor.expectedDigit(i + 1, getLineNumber());
                return;
            }
            value = value * 10 + Character.digit(number.charAt(i), 10);
        }
        lineVisitor.numberedLabel(value, getLineNumber());
    }

    private void visitDirective(String line) throws IOException {
        for (int i = 1; i < line.length(); ++i) {
            if (!(Character.isDigit(line.charAt(i)) || Character.isLetter(line.charAt(i)))) {
                handleDirective(line, i);
                return;
            }
        }
        handleDirective(line, line.length());

    }

    private void handleDirective(String line, int i) {
        final String directive = line.substring(1, i).toLowerCase();
        if (directive.length() == 0) {
            lineVisitor.expectedDirectiveName(i, getLineNumber());
            return;
        }
        if ("lock".equals(directive) || "lock1".equals(directive)) {
            lock(1, line.substring(i));
            return;
        }
        if ("lock2".equals(directive)) {
            lock(2, line.substring(i));
            return;
        }
        if ("lock3".equals(directive)) {
            lock(3, line.substring(i));
            return;
        }

        if (i < line.length() && !isSeparator(line.charAt(i))) {
            lineVisitor.unexpectedCharacter(i, getLineNumber());
        }
        while (i < line.length() && isSeparator(line.charAt(i))) {
            ++i;
        }
        final int start = i;
        if ("def".equals(directive)) {

            while (i < line.length()) {
                if (!isValidVariableNameChar(line.charAt(i))) {
                    lineVisitor.invalidVariableNameChar(i, getLineNumber());
                    return;
                }
                i++;
            }
            lineVisitor.defineVariable(line.substring(start).toLowerCase(), getLineNumber());
            return;
        }
        if ("time".equals(directive)) {
            while (i < line.length()) {
                if (!Character.isDigit(line.charAt(i))) {
                    lineVisitor.expectedDigit(i, getLineNumber());
                    return;
                }
                ++i;
            }
            lineVisitor.maxProcessorSpeed(Integer.parseInt(line.substring(start)));
            return;
        }
        if ("msg".equals(directive)) {
            lineVisitor.setMessage(line.substring(i));
            return;
        }
        if ("config".equals(directive)) {
            while (i < line.length()) {
                if (line.charAt(i) == '=') {
                    final String name = line.substring(start, i);
                    final int valueStart = ++i;
                    while (i < line.length() && !isSeparator(line.charAt(i))) {
                        if (!Character.isDigit(line.charAt(i))) {
                            lineVisitor.expectedDigit(i, getLineNumber());
                            return;
                        }
                        ++i;
                    }
                    try {
                        lineVisitor.setConfig(name, Integer.parseInt(line.substring(valueStart, i)));
                    } catch (NumberFormatException e) {
                        System.err.println("Unable to parse config line:");
                    }
                    return;
                }
                if (!Character.isLetter(line.charAt(i))) {
                    lineVisitor.expectedDeviceName(i, getLineNumber());
                    return;
                }
                ++i;
            }
            return;
        }
        if ("end".equals(directive)) {
            stopProcessing();
            return;
        }
        lineVisitor.unknownDirective(directive, getLineNumber());
    }

    private void lock(int lockType, String remainingLine) {
        this.lockType = lockType;
        try {
            this.lockKey = remainingLine.trim().toUpperCase().getBytes("ASCII");
            for (int i = 0; i < this.lockKey.length; ++i) {
                this.lockKey[i] -= 65;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private void stopProcessing() {
        stopProcessing = true;
    }

    private boolean isValidVariableNameChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    public int getLineNumber() {
        return reader.getLineNumber();
    }
}
