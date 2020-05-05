package view;

import javax.swing.*;
import java.awt.*;

public abstract class AppPanel extends JPanel {
    protected GridBagConstraints gc;

    public AppPanel() {
        super();
        this.setLayout(new GridBagLayout());
        this.gc = new GridBagConstraints();
    }

    public void addComponent(int gridy, int gridx, JComponent component) {
        this.gc.gridy = gridy;
        this.gc.gridx = gridx;
        this.add(component, this.gc);
    }

    public void addComponent(int gridy, int gridx, JComponent component, int anchor) {
        this.gc.anchor = anchor;
        this.addComponent(gridy, gridx, component);
    }

    public void addComponent(int gridy, int gridx, JComponent component, int anchor, double weighty, double weightx) {
        this.gc.weighty = weighty;
        this.gc.weightx = weighty;
        this.addComponent(gridy, gridx, component);
    }

    public void addComponent(int gridy, int gridx, JComponent component, int anchor, Insets insets) {
        this.gc.insets = insets;
        this.addComponent(gridy, gridx, component, anchor);
    }

    public void addComponent(int gridy, int gridx, JComponent component, int anchor, Insets insets, double weighty) {
        this.gc.weighty = weighty;
        this.addComponent(gridy, gridx, component, anchor, insets);
    }

    public void addComponent(int gridy, int gridx, JComponent component, int anchor, Insets insets, double weighty, double weightx) {
        this.gc.weightx = weightx;
        this.addComponent(gridy, gridx, component, anchor, insets, weighty);
    }

    abstract protected void setupComponents();

    abstract protected void layoutComponents();
}
