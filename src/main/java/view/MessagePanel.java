package view;

import controller.MessageServer;
import lib.Utility;
import model.Message;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessagePanel extends JPanel {
    private Set<Integer> selectedServers;
    private MessageServer messageServer;
    private ProgressDialog progressDialog;
    private SwingWorker<List<Message>, Integer> worker;
    private TextPanel textPanel;
    private JList<Message> messageList;
    private DefaultListModel<Message> listModel;

    public MessagePanel(Window parent) {
        super();

        this.messageServer = new MessageServer();

        this.progressDialog = new ProgressDialog(parent);
        this.progressDialog.setListener(() -> {
            worker.cancel(true);
            this.progressDialog.setVisible(false);
        });

        this.selectedServers = new TreeSet<>();
        this.selectedServers.add(1);
        this.selectedServers.add(3);
        this.selectedServers.add(6);

        this.listModel = new DefaultListModel<>();
        this.textPanel = new TextPanel();

        this.messageList = new JList<>(this.listModel);
        this.messageList.setCellRenderer(new MessageCellRenderer());

        this.messageList.addListSelectionListener(e -> {
            if (messageList.getSelectedIndex() == -1) {
                return;
            }

            String content = messageList.getSelectedValue().getContent();
            textPanel.setText(content);

        });

        this.setBorder(BorderFactory.createEmptyBorder(14, 0, 10, 10));
        this.setLayout(new GridLayout());

        ServerTreeEditor serverTreeEditor = new ServerTreeEditor();
        serverTreeEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                ServerInfo serverInfo = (ServerInfo) serverTreeEditor.getCellEditorValue();

                if (serverInfo.isChecked()) {
                    MessagePanel.this.selectedServers.add(serverInfo.getId());
                } else {
                    MessagePanel.this.selectedServers.remove(serverInfo.getId());
                }

                MessagePanel.this.retrieveMessage();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });

        JTree treeServers = new JTree(this.serverList());
        treeServers.setCellRenderer(new ServerTreeRenderer());
        treeServers.setEditable(true);
        treeServers.setCellEditor(serverTreeEditor);
        treeServers.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JSplitPane lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(messageList), textPanel);
        JSplitPane upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(treeServers), lowerPane);

        textPanel.setMinimumSize(new Dimension(10, 100));
        messageList.setMinimumSize(new Dimension(10, 100));

        upperPane.setResizeWeight(0.5);
        lowerPane.setResizeWeight(0.5);

        this.add(upperPane);
    }

    public void retrieveMessage() {
        worker = new SwingWorker<>() {
            @Override
            protected void process(List<Integer> receives) {
                int count = receives.get(receives.size() - 1);
                MessagePanel.this.progressDialog.setProgress(count);
            }

            @Override
            protected void done() {
                try {
                    if (this.isCancelled()) return;

                    List<Message> messages = get();
                    listModel.removeAllElements();

                    for (Message m : messages) {
                        listModel.addElement(m);
                    }

                    messageList.setSelectedIndex(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                MessagePanel.this.progressDialog.setVisible(false);
            }

            @Override
            protected List<Message> doInBackground() throws Exception {
                var count = 0;
                List<Message> messages = new ArrayList<>();

                for (Message m : MessagePanel.this.messageServer) {
                    messages.add(m);
                    publish(++count);
                }

                return messages;
            }
        };

        MessagePanel.this.messageServer.setServerMessages(MessagePanel.this.selectedServers);
        this.progressDialog.setMaximum(MessagePanel.this.messageServer.getMessageCount());
        this.progressDialog.setVisible(true);

        worker.execute();
    }

    private DefaultMutableTreeNode serverList() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Servers");

        DefaultMutableTreeNode europeServer = new DefaultMutableTreeNode("Europe");
        DefaultMutableTreeNode asiaServer = new DefaultMutableTreeNode("Asia");

        europeServer.add(new DefaultMutableTreeNode(new ServerInfo("London", 1, this.selectedServers.contains(1))));
        europeServer.add(new DefaultMutableTreeNode(new ServerInfo("Moscow", 2, this.selectedServers.contains(2))));
        europeServer.add(new DefaultMutableTreeNode(new ServerInfo("Krimea", 3, this.selectedServers.contains(3))));
        europeServer.add(new DefaultMutableTreeNode(new ServerInfo("Madrid", 4, this.selectedServers.contains(4))));

        asiaServer.add(new DefaultMutableTreeNode(new ServerInfo("Seoul", 5, this.selectedServers.contains(5))));
        asiaServer.add(new DefaultMutableTreeNode(new ServerInfo("Tokyo", 6, this.selectedServers.contains(6))));
        asiaServer.add(new DefaultMutableTreeNode(new ServerInfo("Kuala Lumpur", 7, this.selectedServers.contains(7))));
        asiaServer.add(new DefaultMutableTreeNode(new ServerInfo("Jakarta", 8, this.selectedServers.contains(8))));

        root.add(europeServer);
        root.add(asiaServer);

        return root;
    }

    static class ServerTreeRenderer implements TreeCellRenderer {
        private DefaultTreeCellRenderer nonLeafNode;
        private JCheckBox leafNode;

        public ServerTreeRenderer() {
            nonLeafNode = new DefaultTreeCellRenderer();
            nonLeafNode.setOpenIcon(Utility.loadIcon("/server_delete.png"));
            nonLeafNode.setClosedIcon(Utility.loadIcon("/server_add.png"));
            nonLeafNode.setLeafIcon(Utility.loadIcon("/server.png"));

            leafNode = new JCheckBox();
        }

        @Override
        public Component getTreeCellRendererComponent(
                JTree tree, Object value, boolean selected,
                boolean expanded, boolean leaf, int row,
                boolean hasFocus
        ) {

            if (leaf) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                ServerInfo serverInfo = (ServerInfo) treeNode.getUserObject();

                Color textForeground = UIManager.getColor("Tree.textForeground");
                Color textBackground = UIManager.getColor("Tree.textBackground");
                Color selectionForeground = UIManager.getColor("Tree.selectionForeground");
                Color selectionBackground = UIManager.getColor("Tree.selectionBackground");

                leafNode.setText(serverInfo.getName());
                leafNode.setSelected(serverInfo.isChecked);

                if (selected) {
                    leafNode.setForeground(selectionForeground);
                    leafNode.setBackground(selectionBackground);
                } else {
                    leafNode.setForeground(textForeground);
                    leafNode.setBackground(textBackground);
                }
                return leafNode;
            } else {
                return nonLeafNode.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            }
        }
    }

    static class ServerTreeEditor extends AbstractCellEditor implements TreeCellEditor {
        private TreeCellRenderer renderer;
        private ServerInfo serverInfo;
        private JCheckBox checkBox;

        public ServerTreeEditor() {
            //Cannot use same renderer instance from treeServers.getCellRenderer(), cause it gona caused weird behaviour
            this.renderer = new ServerTreeRenderer();
        }

        @Override
        //What component is at that leaf? get that component
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            Component component = renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, true);

            if (leaf) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                this.serverInfo = (ServerInfo) treeNode.getUserObject();
                this.checkBox = (JCheckBox) component;

                ItemListener listener = new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        fireEditingStopped();
                        checkBox.removeItemListener(this);
                    }
                };

                checkBox.addItemListener(listener);
            }
            return component;
        }

        @Override
        //What value is at that leaf? get that value
        public Object getCellEditorValue() {
            this.serverInfo.setChecked(this.checkBox.isSelected());
            return this.serverInfo;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (!(e instanceof MouseEvent)) return false;

            MouseEvent mouseEvent = (MouseEvent) e;
            JTree tree = (JTree) e.getSource();
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());

            if (path == null) return false;

            Object component = path.getLastPathComponent();

            if (component == null) return false;

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) component;

            return treeNode.isLeaf();
        }
    }

    static class ServerInfo {
        private String name;
        private int id;
        private boolean isChecked;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean b) {
            this.isChecked = b;
        }

        public ServerInfo(String name, int id, boolean isChecked) {
            this.name = name;
            this.id = id;
            this.isChecked = isChecked;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
