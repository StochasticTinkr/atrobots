package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.EntrantFactory;
import net.virtualinfinity.atrobots.Errors;
import net.virtualinfinity.atrobots.Game;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Daniel Pitts
 */
public class Main implements Runnable {
    private JFrame mainFrame;
    private JMenuBar menubar;
    private JMenu menu;
    private Game game;
    private ArenaPane arenaPane;
    private Timer timer = new Timer(50, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            new Thread() {
                public void run() {
                    game.stepRound();
                }
            }.start();
        }
    });

    public void run() {
        initializeSystemLookAndFeel();
        mainFrame = new JFrame("AT-Robots 2 Clone 0.0.01");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                }
            }
        });
        menubar = new JMenuBar();
        mainFrame.setJMenuBar(menubar);
        menubar.add(createFileMenu());
        menubar.add(new JMenuItem(new AbstractAction("Step") {
            public void actionPerformed(ActionEvent e) {
//                new Thread() { public void run() { game.stepRound();}}.start();
                timer.setCoalesce(true);
                timer.start()
                        ;
            }
        }));
        arenaPane = new ArenaPane();
        mainFrame.getContentPane().add(arenaPane, BorderLayout.CENTER);
        arenaPane.setBackground(Color.black);
        arenaPane.setOpaque(true);
        arenaPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.gray, Color.darkGray));
        arenaPane.setPreferredSize(new Dimension(500, 500));
        game = new Game(1000);
        game.addSimulationObserver(arenaPane);
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
                    if (game != null) {
                        game.dispose();
                    }
                    game = newGame;
                    game.addSimulationObserver(arenaPane);
                }
            }
        });
        menu.add(new AbstractAction("Add Entrant") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
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
                new EntrantLoader(new File("original").listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith("circles.at2");
                    }
                })).execute();

            }
        }));
        return menu;
    }

    private void initializeSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
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
                EntrantFactory factory = new EntrantFactory(file);
                try {
                    System.out.println("Loading " + file);
                    final Errors result = factory.compile();
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
            } catch (ExecutionException e1) {
            }
        }
    }
}
