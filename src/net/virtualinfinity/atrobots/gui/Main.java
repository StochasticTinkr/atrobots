package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.EntrantFactory;
import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.gui.renderers.GradientExplosionRenderer;
import net.virtualinfinity.atrobots.gui.renderers.ScanRenderer;
import net.virtualinfinity.atrobots.gui.renderers.ScanRendererWithFilledArcs;
import net.virtualinfinity.atrobots.gui.renderers.SimpleExplosionRenderer;
import net.virtualinfinity.atrobots.parser.Errors;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * This is the main GUI class.
 *
 * @author Daniel Pitts
 */
public class Main implements Runnable {
    private JFrame mainFrame;
    private JMenuBar menubar;
    private JMenu menu;
    private Game game;
    private ArenaPane arenaPane;
    private boolean paused = true;

    private final Object pauseLock;
    private volatile int frameDelay = 25;
    private volatile boolean useDelay = true;
    private Thread gameThread = new Thread() {
        public void run() {
            while (!closed) {
                try {
                    if (useDelay) {
                        frameDelay = 25;
                        Thread.sleep(frameDelay);
                    }
                    synchronized (pauseLock) {
                        while (paused && !closed) {
                            pauseLock.wait();
                        }
                    }
                    game.stepRound();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private volatile boolean closed;
    private RobotStatusPane robotStatusPane;

    public Main() {
        pauseLock = new Object();
    }

    public void run() {
        initializeSystemLookAndFeel();
        gameThread.setDaemon(true);
        gameThread.start();
        mainFrame = new JFrame("AT-Robots 2 Clone 0.0.01");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                synchronized (pauseLock) {
                    closed = true;
                    paused = false;
                    pauseLock.notifyAll();
                }

            }
        });
        menubar = new JMenuBar();
        mainFrame.setJMenuBar(menubar);
        menubar.add(createFileMenu());
        final JMenu viewMenu = new JMenu("View");
        menubar.add(viewMenu);
        menubar.add(new JMenuItem(new AbstractAction("Run") {
            public void actionPerformed(ActionEvent e) {
                synchronized (pauseLock) {
                    paused = !paused;
                    this.putValue(Action.NAME, paused ? "Run" : "Pause");
                    pauseLock.notifyAll();
                }
            }
        }));
        menubar.add(new JMenuItem(new AbstractAction("Full Speed") {
            public void actionPerformed(ActionEvent e) {
                useDelay = !useDelay;
                this.putValue(Action.NAME, useDelay ? "Full Speed" : "Slower");
            }
        }));
        viewMenu.add(new JCheckBoxMenuItem(new AbstractAction("Gradiant Explosions") {
            public void actionPerformed(ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                boolean selected = aButton.getModel().isSelected();
                arenaPane.getArenaRenderer().setExplosionRenderer(selected ? new GradientExplosionRenderer() : new SimpleExplosionRenderer());
            }
        }));
        viewMenu.add(new JCheckBoxMenuItem(new AbstractAction("Filled Scan Arcs") {
            public void actionPerformed(ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                boolean selected = aButton.getModel().isSelected();
                arenaPane.getArenaRenderer().setScanRenderer(selected ? new ScanRendererWithFilledArcs() : new ScanRenderer());
            }
        }));
        robotStatusPane = RobotStatusPane.createRobotStatusPane();
        mainFrame.getContentPane().add(new JScrollPane(robotStatusPane), BorderLayout.EAST);
        arenaPane = new ArenaPane();
        mainFrame.getContentPane().add(arenaPane, BorderLayout.CENTER);
        arenaPane.setBackground(Color.black);
        arenaPane.setOpaque(true);
        arenaPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.gray, Color.darkGray));
        arenaPane.setPreferredSize(new Dimension(500, 500));
        game = new Game(1000);
        game.addSimulationObserver(arenaPane);
        game.addSimulationObserver(robotStatusPane);
        arenaPane.setRobotStatusPane(robotStatusPane);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLocationByPlatform(true);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JMenu createFileMenu() {
        menu = new JMenu("Game");
        menu.add(new AbstractAction("New Game") {
            public void actionPerformed(ActionEvent e) {
                final NewGameDialog dialog = new NewGameDialog();
                dialog.setModal(true);
                dialog.pack();
                dialog.setVisible(true);
                final Game newGame = dialog.getGame();
                if (newGame != null) {
//                    if (game != null) {
//                        game.dispose();
//                    }
                    game = newGame;
                    game.addSimulationObserver(arenaPane);
                }
                game.nextRound();
            }
        });
        menu.add(new AbstractAction("Add Entrant") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("AT-Robots files", ".at2", ".ats"));
                chooser.setMultiSelectionEnabled(true);
                if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                    new EntrantLoader(chooser.getSelectedFiles()).execute();
                }
            }
        });
        menubar.add(new JMenuItem(new AbstractAction("Add all original") {
            public void actionPerformed(ActionEvent e) {
                new EntrantLoader(new File("original").listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".at2");
                    }
                })).execute();

            }
        }));
        menubar.add(new JMenuItem(new AbstractAction("Add single") {
            public void actionPerformed(ActionEvent e) {
                for (final File parent : new File[]{new File("."), new File("original")})
                    new EntrantLoader(parent.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().matches("zitgun.at2") ||
                                    name.toLowerCase().endsWith("sniper2.at2");
                        }
                    })).execute();

            }
        }));
        return menu;
    }

    private void initializeSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Main());
    }

    private class EntrantLoader extends SwingWorker<Errors, Entrant> {
        private final File[] selectedFiles;

        public EntrantLoader(File[] selectedFiles) {
            this.selectedFiles = selectedFiles;
        }

        protected Errors doInBackground() throws Exception {
            Errors errors = new Errors();
            for (File file : selectedFiles) {
                EntrantFactory factory = new EntrantFactory();
                try {
                    System.out.println("Loading " + file);
                    final Errors result = factory.compile(file);
                    if (result.hasErrors()) {
                        errors.info("Errors in " + file.getName());
                        errors.addAll(result);
                    }
                    if (game != null) {
                        game.addEntrant(factory.createEntrant());
                    }
                } catch (IOException e1) {
                    errors.info("Errors in " + file.getName());
                    errors.info(e1.getMessage());
                } catch (Throwable t) {
                    errors.info("Compiler error in " + file.getName());
                    errors.info(t.getMessage());
                    t.printStackTrace();
                }
            }
            return errors;
        }

        protected void done() {
            try {
                get().showErrorDialog("Errors", mainFrame);
                game.nextRound();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        }
    }
}
