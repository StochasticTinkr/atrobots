package net.virtualinfinity.atrobots;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * @author Daniel Pitts
 */
public class Errors {
    List<String> messages = new ArrayList<String>();
    public void add(String error, int lineNumber) {
        messages.add("Line " + lineNumber + ": " + error);
    }

    public  boolean hasErrors() {
        return !messages.isEmpty();
    }

    public void add(String error, int lineNumber, int column) {
        add(error, lineNumber);
    }

    public void showErrorDialog(String name, Frame parent) {
        if (!hasErrors()) {
            return;
        }
        final JDialog dialog = new JDialog(parent, name, true);
        final JList jList = new JList();
        dialog.add(new JScrollPane(jList));
        final DefaultListModel listModel = new DefaultListModel();
        for (String st: messages) {
            listModel.addElement(st);
        }
        jList.setModel(listModel);
        dialog.pack();
        dialog.setVisible(true);
    }

    public void info(String info) {
        messages.add(info);
    }

    public void addAll(Errors result) {
        messages.addAll(result.messages);
    }
}
