package view;

import controller.PreferencesListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.concurrent.Flow;

public class PreferencesDialog extends JDialog {

    private JSpinner portSpinner;
    private SpinnerNumberModel spinnerModel;
    private JLabel spinnerLabel;
    private JLabel userLabel;
    private JTextField userInput;
    private JLabel passwordLabel;
    private JPasswordField passwordInput;
    private JButton okButton;
    private JButton cancelButton;
    private PreferencesListener preferencesListener;


    public PreferencesDialog(JFrame context) {
        super(context, "Preferences", true);
        setSize(250, 250);
        setLocationRelativeTo(context);

        this.setupComponents();
        this.layoutComponents();
    }

    private void setupComponents() {
        spinnerModel = new SpinnerNumberModel(3306, 0, 9999, 1);
        portSpinner = new JSpinner(spinnerModel);
        spinnerLabel = new JLabel("Port Number:");
        userLabel = new JLabel("User:");
        userInput = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordInput = new JPasswordField(10);

        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");

        okButton.addActionListener((e) -> {
            int portNumber = (Integer) portSpinner.getValue();
            String user = userInput.getText();
            String password = String.valueOf(passwordInput.getPassword());

            this.preferencesListener.execute(user, password, portNumber);
            this.setVisible(false);
        });

        cancelButton.addActionListener((e) -> {
            this.setVisible(false);
        });
    }

    private void layoutComponents() {
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.weighty = 1;
        gc.weightx = 1;

        gc.gridy = 0;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(0, 0, 0, 5);
        inputPanel.add(userLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        inputPanel.add(userInput, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(0, 0, 0, 5);
        inputPanel.add(passwordLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        inputPanel.add(passwordInput, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(0, 0, 0, 5);
        inputPanel.add(spinnerLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        inputPanel.add(portSpinner, gc);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton, gc);
        buttonPanel.add(cancelButton, gc);

        Border buttonBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(buttonBorder, emptyBorder));

        this.setLayout(new BorderLayout());
        this.add(inputPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setPreferencesListener(PreferencesListener l) {
        this.preferencesListener = l;
    }

    public void setDefaults(String user, String password, int port) {
        this.userInput.setText(user);
        this.passwordInput.setText(password);
        this.portSpinner.setValue(port);
    }
}
