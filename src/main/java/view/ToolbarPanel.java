package view;

import controller.Listener;
import lib.Utility;

import javax.swing.*;
import java.awt.*;

public class ToolbarPanel extends JToolBar {

    private JButton saveButton;
    private JButton reloadButton;
    private Listener saveListener;
    private Listener reloadListener;

    public ToolbarPanel() {
        super();

        this.saveButton = new JButton("Save");
        this.saveButton.setIcon(Utility.loadIcon("/save.png"));
        this.saveButton.setToolTipText("Save");

        this.reloadButton = new JButton("Reload");
        this.reloadButton.setIcon(Utility.loadIcon("/refresh.png"));
        this.reloadButton.setToolTipText("Reload");

        this.saveButton.addActionListener((e) -> this.saveListener.execute());
        this.reloadButton.addActionListener((e) -> this.reloadListener.execute());

//        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(this.saveButton);
        this.add(this.reloadButton);
    }

    public void setSaveListener(Listener l) {
        this.saveListener = l;
    }

    public void setReloadListener(Listener l) {
        this.reloadListener = l;
    }
}
