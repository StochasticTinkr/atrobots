package net.virtualinfinity.atrobots.debugger;

import net.virtualinfinity.atrobots.computer.Computer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class ConsoleDebugListener extends BaseDebugListener {
    private final BufferedReader input;
    private final PrintWriter output;
    private final BlockingQueue<Command> breakpointCommandQueue = new LinkedBlockingDeque<Command>();
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingDeque<Command>();
    private boolean wasAllPaused;

    private ConsoleDebugListener(BufferedReader input, PrintWriter output) {
        this.input = input;
        this.output = output;
    }

    public static ConsoleDebugListener create(BufferedReader input, PrintWriter output) {
        return new ConsoleDebugListener(input, output).start();
    }

    private void inputLoop() throws IOException {
        for (String line = readline(); line != null; line = readline()) {
            try {
                handleInput(line);
            } catch (Exception e) {
                e.printStackTrace(output);
                e.printStackTrace();
            }
        }
    }

    private void handleInput(String line) throws InterruptedException {
        final String[] tokens = line.split("\\s++");
        if (tokens[0].equalsIgnoreCase("pause")) {
            if (tokens.length > 1) {
                final int id = Integer.parseInt(tokens[1]);
                invokeLater(new Command() {
                    public void execute(Computer computer) {
                        pause(id);
                    }
                });
            } else {
                invokeLater(new Command() {
                    public void execute(Computer computer) {
                        pauseAll();
                    }
                });
            }
        } else if (tokens[0].equalsIgnoreCase("go")) {
            invokeOnBreakpoint(new Command() {
                public void execute(Computer computer) {
                    clearPaused(computer);
                }
            });
        } else if (tokens[0].equalsIgnoreCase("step")) {
            invokeOnBreakpoint(new Command() {
                public void execute(Computer computer) {
                    if (wasAllPaused) {
                        pauseAll();
                    } else {
                        pause(computer);
                    }
                }
            });
        }
    }

    private void invokeOnBreakpoint(Command command) throws InterruptedException {
        breakpointCommandQueue.put(command);
    }

    private void invokeLater(Command command) throws InterruptedException {
        commandQueue.put(command);
    }

    @Override
    public void beforeInstruction(Computer computer) {
        final Command command = commandQueue.poll();
        if (command != null) {
            command.execute(computer);
        }
        wasAllPaused = isAllPaused();
        super.beforeInstruction(computer);
    }

    @Override
    protected void handleBreakpoint(Computer computer) {
        printState(computer);
        try {
            waitForCommand(computer);
        } catch (InterruptedException e) {
            println("debugger thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    private void waitForCommand(Computer computer) throws InterruptedException {
        breakpointCommandQueue.take().execute(computer);
    }

    private void printState(Computer computer) {
        println("[" + computer.getFlags() + "] " + computer.getRegisters());
        println(computer.getInstructionString());
    }

    private void println(Object o) {
        output.println(o);
        output.flush();
    }

    private String readline() throws IOException {
        try {
            println("> ");
            return input.readLine();
        } catch (IOException e) {
            println("IO Exception handling debug command: " + e.getLocalizedMessage());
        }
        return null;
    }


    private ConsoleDebugListener start() {
        final Thread thread = new Thread("Debugger") {
            public void run() {
                try {
                    inputLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        return this;
    }

    private static interface Command {
        void execute(Computer computer);
    }
}
