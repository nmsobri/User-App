package view;

import lib.Utility;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private JTextArea textArea;

    public TextPanel() {
        super();

        this.textArea = new JTextArea();
        this.textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.textArea.setFont(Utility.loadFont("/qs.otf", Font.PLAIN, 22));

        this.setLayout(new GridLayout());
        this.add(new JScrollPane(this.textArea));
    }

    public void appendText(String what) {
        this.textArea.append(what);
    }

    public void setText(String what) {
        this.textArea.setText(what);
    }
}
