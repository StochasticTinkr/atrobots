package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.SimulationObserver;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import net.virtualinfinity.atrobots.snapshots.SnapshotAdaptor;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Describe this class.
 *
 * @author <a href="mailto:daniel.pitts@cbs.com">Daniel Pitts</a>
 */
public class RobotStatusPane extends JList implements SimulationObserver {
    private final DefaultListModel robotList;

    private static class RobotItem {
        private RobotSnapshot robotSnapshot;
        private boolean dead;
        private boolean updated;

        public RobotItem(RobotSnapshot robotSnapshot) {
            setRobotSnapshot(robotSnapshot);
        }

        public RobotSnapshot getRobotSnapshot() {
            return robotSnapshot;
        }

        public boolean isUpdated() {
            return updated;
        }

        public void clearUpdated() {
            this.updated = false;
        }

        public void setRobotSnapshot(RobotSnapshot robotSnapshot) {
            this.updated = true;
            this.dead = robotSnapshot.getArmor() <= 0;
            this.robotSnapshot = robotSnapshot;
        }

        public boolean isDead() {
            return dead;
        }

        public int getId() {
            return getRobotSnapshot().getId();
        }

        public void died() {
            dead = true;
        }
    }

    private RobotStatusPane() {
        robotList = new DefaultListModel();
        setModel(robotList);
        this.setCellRenderer(new RobotStatusRenderer());
    }

    public void frameAvailable(final SimulationFrameBuffer frameBuffer) {
        EventQueue.invokeLater(new UpdateRobotList(frameBuffer));
    }

    private void updateRobotStatus(RobotSnapshot robotSnapshot) {
        for (int i = 0; i < robotList.getSize(); ++i) {
            final RobotItem robotItem = (RobotItem) robotList.get(i);
            if (robotItem.getId() == robotSnapshot.getId()) {
                robotItem.setRobotSnapshot(robotSnapshot);
                return;
            }
        }
        robotList.addElement(new RobotItem(robotSnapshot));
        revalidate();
    }

    public static RobotStatusPane createRobotStatusPane() {
        return new RobotStatusPane();
    }

    public java.util.Set<Integer> getSelectedRobotIds() {
        final Object[] objects = this.getSelectedValues();
        final Set<Integer> selectedIds = new HashSet<Integer>();
        for (Object o : objects) {
            selectedIds.add(((RobotItem) o).getId());

        }
        return selectedIds;
    }

    private static class RobotStatusRenderer extends JPanel implements ListCellRenderer {
        private final JLabel name = new JLabel();
        private final JLabel roundKills = new JLabel();
        private final JProgressBar armor = new JProgressBar();
        private final JProgressBar heat = new JProgressBar();
        private final JLabel lastMessage = new JLabel();

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final Color background = isSelected ? Color.blue : Color.black;
            setBackground(background);
            setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.lightGray, Color.darkGray));
            final Color foreground = isSelected ? Color.yellow : Color.gray;
//            armor.setBackground(Color.green);
//            armor.setForeground(Color.green.brighter());
//            heat.setBackground(Color.red);
//            heat.setForeground(Color.red.brighter());
            name.setForeground(foreground);
            roundKills.setForeground(foreground);
            lastMessage.setForeground(foreground);
            removeAll();
            setLayout(new GridLayout(5, 1));
            add(name);
            add(roundKills);
            add(armor);
            add(heat);
            add(lastMessage);
            RobotItem item = (RobotItem) value;
            final RobotSnapshot robotSnapshot = item.getRobotSnapshot();
            if (!item.isDead()) {
                name.setText(robotSnapshot.getName());
            } else {
                name.setText("<html><strike>" + robotSnapshot.getName() + "</html></strike>");
            }
            roundKills.setText("Kills (round/total): " + robotSnapshot.getRoundKills() + "/" + robotSnapshot.getTotalKills());
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
            for (int i = 0; i < robotList.getSize(); ++i) {
                final RobotItem robotItem = (RobotItem) robotList.get(i);
                robotItem.clearUpdated();
            }
            for (ArenaObjectSnapshot snapshot : frameBuffer.getCurrentFrame()) {
                snapshot.visit(new SnapshotAdaptor() {
                    @Override
                    public void acceptRobot(RobotSnapshot robotSnapshot) {
                        updateRobotStatus(robotSnapshot);
                    }
                });
            }
            for (int i = 0; i < robotList.getSize(); ++i) {
                final RobotItem robotItem = (RobotItem) robotList.get(i);
                if (robotItem.isUpdated()) {
                    robotList.set(i, robotItem);
                } else {
                    if (!robotItem.isDead()) {
                        robotItem.died();
                        robotList.set(i, robotItem);
                    }
                }
            }
        }
    }
}
