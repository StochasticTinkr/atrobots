package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.SimulationObserver;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import net.virtualinfinity.atrobots.snapshots.SnapshotAdaptor;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: Describe this class.
 *
 * @author <a href="mailto:daniel.pitts@cbs.com">Daniel Pitts</a>
 */
public class RobotStatusPane extends JPanel implements SimulationObserver {
    private final JList robotListView;
    private final DefaultListModel robotList;

    private RobotStatusPane() {
        robotList = new DefaultListModel();
        robotListView = new JList(robotList);
        robotListView.setCellRenderer(new RobotStatusRenderer());
        add(new JScrollPane(robotListView));
    }


    public void frameAvailable(final SimulationFrameBuffer frameBuffer) {
        EventQueue.invokeLater(new UpdateRobotList(frameBuffer));
    }

    private void updateRobotStatus(RobotSnapshot robotSnapshot) {
        for (int i = 0; i < robotList.getSize(); ++i) {
            if (((RobotSnapshot) robotList.get(i)).getId() == robotSnapshot.getId()) {
                robotList.set(i, robotSnapshot);
                return;
            }
        }
        robotList.addElement(robotSnapshot);
    }

    public static RobotStatusPane createRobotStatusPane() {
        return new RobotStatusPane();
    }

    private static class RobotStatusRenderer extends JPanel implements ListCellRenderer {
        private final JLabel name = new JLabel();
        private final JLabel roundKills = new JLabel();
        private final JProgressBar armor = new JProgressBar();
        private final JProgressBar heat = new JProgressBar();
        private final JLabel lastMessage = new JLabel();

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            removeAll();
            setLayout(new GridLayout(5, 1));
            add(name);
            add(roundKills);
            add(armor);
            add(heat);
            add(lastMessage);
            final RobotSnapshot robotSnapshot = (RobotSnapshot) value;
            name.setText(robotSnapshot.getName());
            roundKills.setText(robotSnapshot.getRoundKills() + (robotSnapshot.getRoundKills() == 1 ? " kill" : " kills"));
            armor.setValue((int) Math.round(Math.min(100, robotSnapshot.getArmor())));
            heat.setValue((int) Math.round(Math.min(100, robotSnapshot.getTemperature().getLogScale() * .2)));
            lastMessage.setText(robotSnapshot.getLastMessage() == null ? "" : robotSnapshot.getLastMessage());
            return this;
        }
    }

    private class UpdateRobotList implements Runnable {
        private final SimulationFrameBuffer frameBuffer;

        public UpdateRobotList(SimulationFrameBuffer frameBuffer) {
            this.frameBuffer = frameBuffer;
        }

        public void run() {
            for (ArenaObjectSnapshot snapshot : frameBuffer.getCurrentFrame()) {
                snapshot.visit(new SnapshotAdaptor() {
                    @Override
                    public void acceptRobot(RobotSnapshot robotSnapshot) {
                        updateRobotStatus(robotSnapshot);
                    }
                });

            }
        }
    }
}
