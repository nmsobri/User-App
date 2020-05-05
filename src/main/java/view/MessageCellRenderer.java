package view;

import lib.Utility;
import model.Message;

import javax.swing.*;
import java.awt.*;

public class MessageCellRenderer implements ListCellRenderer<Message> {
    private JPanel panel;
    private JLabel label;
    private Color normalColor;
    private Color selectedColor;

    public MessageCellRenderer() {
        this.normalColor = Color.WHITE;
        this.selectedColor = Color.LIGHT_GRAY;

        this.panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.label = new JLabel();
        this.label.setIcon(Utility.loadIcon("/comment.png"));
        this.panel.add(this.label);
    }


    @Override
    //What is the thing inside each of the list? return it then
    public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index, boolean isSelected, boolean cellHasFocus) {
        String title = value.getTitle();
        this.label.setText(title);

        this.panel.setBackground(cellHasFocus ? selectedColor : normalColor);
        return this.panel;
    }
}
