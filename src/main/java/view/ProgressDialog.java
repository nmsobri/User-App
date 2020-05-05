package view;

import controller.Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProgressDialog extends JDialog {
    private JProgressBar progressBar;
    private JButton cancelButton;
    private Listener listener;

    public ProgressDialog(Window parent) {
        super(parent, "Messages downloading", ModalityType.APPLICATION_MODAL);
        this.setSize(new Dimension(400, 200));
        this.setLocationRelativeTo(parent);

        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.progressBar = new JProgressBar();
        this.progressBar.setStringPainted(true);
        this.cancelButton = new JButton("Cancel");

        this.cancelButton.addActionListener((e) -> {
            this.listener.execute();
        });

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ProgressDialog.this.listener.execute();
            }
        });

        Dimension size = this.cancelButton.getPreferredSize();
        size.width = 250;
        this.progressBar.setPreferredSize(size);

        this.add(this.progressBar);
        this.add(this.cancelButton);
        this.pack();
    }

    public void setMaximum(int value) {
        this.progressBar.setMaximum(value);
    }

    public void setProgress(int value) {
        int progress = value * 100 / this.progressBar.getMaximum();
        this.setValue(value);
        this.progressBar.setString(String.format("%d%%", progress));
    }

    public void setValue(int value) {
        this.progressBar.setValue(value);
    }

    @Override
    public void setVisible(boolean b) {
        SwingUtilities.invokeLater(() -> {
            if (b) {
                this.setProgress(0);
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (b) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            } else {
                this.setCursor(Cursor.getDefaultCursor());
            }

            ProgressDialog.super.setVisible(b);
        });
    }

    public void setListener(Listener l) {
        this.listener = l;
    }
}


