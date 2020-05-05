package view;

import com.formdev.flatlaf.FlatLightLaf;
import lib.Utility;
import model.Database;
import model.FormModel;
import model.Person;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class App {
    private ToolbarPanel toolbarPanel;
    private FormPanel formPanel;
    private TablePanel tablePanel;
    private TextPanel textPanel;
    private JFrame frame;
    private JFileChooser fileChooser;
    private PreferencesDialog preferencesDialog;
    private Preferences preferences;
    private Database database;
    private JSplitPane splitPane;
    private JTabbedPane tabbedPane;
    private MessagePanel messagePanel;

    public App() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        this.preferences = Preferences.userRoot().node("preferences");

        String user = this.preferences.get("username", "");
        String passwrod = this.preferences.get("password", "");
        int port = this.preferences.getInt("port", 0);

        this.database = new Database(user, passwrod, port);

        this.preferencesDialog = new PreferencesDialog(this.frame);
        this.preferencesDialog.setDefaults(user, passwrod, port);
        this.preferencesDialog.setPreferencesListener(((username, password, portNumber) -> {
            this.preferences.put("username", username);
            this.preferences.put("password", password);
            this.preferences.putInt("port", portNumber);

            this.database.configure(username, password, port);
        }));

        this.fileChooser = new JFileChooser();
        this.fileChooser.addChoosableFileFilter(new PersonFileFilter());

        this.frame = new JFrame();
        this.frame.setTitle("User App");
        this.frame.setLayout(new BorderLayout());
        this.frame.setJMenuBar(this.setMenu());

        this.formPanel = new FormPanel();
        this.textPanel = new TextPanel();
        this.toolbarPanel = new ToolbarPanel();
        this.tablePanel = new TablePanel(new ArrayList<>(), this.database);
        this.messagePanel = new MessagePanel(this.frame);

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.addTab("Database Users", this.tablePanel);
        this.tabbedPane.addTab("Messages", this.messagePanel);
        this.tabbedPane.addChangeListener(e -> {
            int index = App.this.tabbedPane.getSelectedIndex();
            if (index == 1) {
                this.messagePanel.retrieveMessage();
            }
        });

        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.formPanel, this.tabbedPane);
        this.splitPane.setOneTouchExpandable(true);

        this.formPanel.setListener((FormModel f) -> {
            JTable table = this.tablePanel.getTable();
            int rowCount = table.getModel().getRowCount();

            int lastIndex = rowCount > 0 ? (int) table.getValueAt(table.getModel().getRowCount() - 1, 0) : 0;

            Person p = new Person(
                    lastIndex + 1, f.getName(), f.getOccupation(), f.getAge(),
                    f.getEmployment(), f.isUsCitizen(), f.getTaxID(),
                    f.getGender()
            );

            this.tablePanel.addPerson(p);
            this.tablePanel.refresh();
        });

        this.toolbarPanel.setSaveListener(() -> {
            try {
                this.database.connect();
                this.database.save();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(App.this.frame, "Cannot save data", "Database error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.toolbarPanel.setReloadListener(() -> {
            try {
                this.database.connect();
                this.database.load();
                this.tablePanel.refresh();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(App.this.frame, "Cannot load data", "Database error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(this.toolbarPanel, BorderLayout.PAGE_START);
        frame.add(this.splitPane, BorderLayout.CENTER);

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                App.this.frame.dispose();
                System.gc();
            }
        });

        frame.setSize(new Dimension(700, 500));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setIconImage(Utility.loadIcon("/icon.png").getImage());
        frame.setVisible(true);
    }

    private JMenuBar setMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem importData = new JMenuItem("Import Data");
        importData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
        importData.addActionListener((e) -> {
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    this.tablePanel.loadFromFile(fileChooser.getSelectedFile());
                    this.tablePanel.refresh();
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this.frame, "Error import\n" + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem exportData = new JMenuItem("Export Data");
        exportData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        exportData.addActionListener((e) -> {
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    this.tablePanel.saveToFile(fileChooser.getSelectedFile());
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this.frame, "Error export\n" + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        exit.addActionListener((e) -> {
            var choice = JOptionPane.showConfirmDialog(frame, "Are you sure to exit?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                WindowListener[] listeners = App.this.frame.getWindowListeners();

                for (WindowListener l : listeners) {
                    l.windowClosing(new WindowEvent(App.this.frame, 0));
                }
            }
        });

        fileMenu.add(importData);
        fileMenu.add(exportData);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        JMenu toolMenu = new JMenu("Toolbar");
        JMenuItem project = new JMenuItem("Project");
        JMenuItem favourites = new JMenuItem("Favourites");
        JMenuItem structure = new JMenuItem("Structure");
        JMenuItem build = new JMenuItem("Build");
        JMenuItem activeWindow = new JMenuItem("Active Window");

        JCheckBoxMenuItem personForm = new JCheckBoxMenuItem("Person Form");
        personForm.setSelected(true);

        JMenuItem preferences = new JMenuItem("Preferences...");

        preferences.addActionListener((e) -> {
            this.preferencesDialog.setVisible(true);
        });

        preferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));

        toolMenu.add(project);
        toolMenu.add(favourites);
        toolMenu.add(structure);
        toolMenu.add(build);
        viewMenu.add(toolMenu);
        viewMenu.add(activeWindow);
        viewMenu.add(personForm);
        viewMenu.add(preferences);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem checkForUpdate = new JMenuItem("Check For Update");

        JMenuItem about = new JMenuItem("About");
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        about.addActionListener((e) -> {
            JOptionPane.showMessageDialog(frame, "This view.App Is Developed For Mco.\nDeveloped By Sobri", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        helpMenu.add(checkForUpdate);
        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        personForm.addActionListener((e) -> {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            formPanel.setVisible(item.isSelected());
            splitPane.setDividerLocation((int) this.formPanel.getSize().getWidth());
        });

        return menuBar;
    }

    private static class PersonFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String fileName = f.getName();
            if (fileName == null) {
                return false;
            }

            return getFileExtension(fileName).equals("per");
        }

        @Override
        public String getDescription() {
            return "Person Database File (*.per)";
        }

        private String getFileExtension(String fileName) {
            if (fileName.lastIndexOf(".") == -1) {
                return "";
            }

            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
    }
}
