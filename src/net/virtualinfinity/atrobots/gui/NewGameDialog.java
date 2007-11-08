package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.EntrantFactory;
import net.virtualinfinity.atrobots.Game;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewGameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner numberOfRounds;
    private JSpinner spinner1;
    private Game game;

    public NewGameDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        game = new Game(((Number)numberOfRounds.getValue()).intValue());

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        game = null;
        dispose();
    }

    public static void main(String[] args) {
        NewGameDialog dialog = new NewGameDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
    }

    public Game getGame() {
        return game;
    }

}
