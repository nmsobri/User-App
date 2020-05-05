package view;

import controller.FormListener;
import model.Employee;
import model.FormModel;

import javax.swing.*;
import java.awt.*;

public class FormPanel extends AppPanel {
    private JLabel nameLabel;
    private JTextField nameInput;
    private JLabel occupationLabel;
    private JTextField occupationInput;
    private JButton addButton;
    private JLabel ageLabel;
    private JList<AgeCategory> ageInput;
    private JLabel employmentLabel;
    private JComboBox<Employee> employmentInput;
    private JLabel isUsCitizenLabel;
    private JCheckBox isUsCitizenInput;
    private JLabel taxIDLabel;
    private JTextField taxIDInput;
    private JLabel genderLabel;
    private JRadioButton maleInput;
    private JRadioButton femaleInput;
    private ButtonGroup buttonGroup;
    private FormListener listener;

    public FormPanel() {
        super();

        var dimension = this.getPreferredSize();
        dimension.width = 250;
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);

        var innerBorder = BorderFactory.createTitledBorder("Add Person");
        var outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        var border = BorderFactory.createCompoundBorder(outterBorder, innerBorder);

        this.setBorder(border);
        this.setupComponents();
        this.layoutComponents();
    }

    protected void setupComponents() {
        this.nameLabel = new JLabel("Name:");
        this.nameInput = new JTextField(10);
        this.occupationLabel = new JLabel("Occupation:");
        this.occupationInput = new JTextField(10);
        this.addButton = new JButton("Add");
        this.ageLabel = new JLabel("Age:");
        this.ageInput = new JList<AgeCategory>();
        this.employmentLabel = new JLabel("Employment:");
        this.employmentInput = new JComboBox<>(Employee.values());
        this.isUsCitizenLabel = new JLabel("Us Citizen:");
        this.isUsCitizenInput = new JCheckBox();
        this.taxIDLabel = new JLabel("Tax ID:");
        this.taxIDInput = new JTextField(10);
        this.genderLabel = new JLabel("Gender:");

        this.maleInput = new JRadioButton("Male", true);
        this.maleInput.setActionCommand("male");

        this.femaleInput = new JRadioButton("Female");
        this.femaleInput.setActionCommand("female");

        this.buttonGroup = new ButtonGroup();

        var ageModel = new DefaultListModel<AgeCategory>();
        ageModel.addElement(new AgeCategory("Under 18", 1));
        ageModel.addElement(new AgeCategory("Under 25", 2));
        ageModel.addElement(new AgeCategory("Under 33", 3));
        ageModel.addElement(new AgeCategory("Under 44", 4));
        ageModel.addElement(new AgeCategory("Under 60", 5));

        this.ageInput.setModel(ageModel);
        this.ageInput.setBorder(BorderFactory.createEtchedBorder());
        this.ageInput.setPreferredSize(new Dimension(114, 100));
        this.ageInput.setSelectedIndex(0);

        this.taxIDLabel.setEnabled(false);
        this.taxIDInput.setEnabled(false);

        this.isUsCitizenInput.addActionListener((e) -> {
            this.taxIDLabel.setEnabled(this.isUsCitizenInput.isSelected());
            this.taxIDInput.setEnabled(this.isUsCitizenInput.isSelected());
        });

        this.buttonGroup.add(maleInput);
        this.buttonGroup.add(femaleInput);

        this.addButton.addActionListener((e) -> {
            String name = this.nameInput.getText();
            String occupation = this.occupationInput.getText();
            String age = this.ageInput.getSelectedValue().getText();
            Employee employment = (Employee) this.employmentInput.getSelectedItem();
            boolean isUsCitizen = this.isUsCitizenInput.isSelected();
            String taxID = this.taxIDInput.getText();
            String gender = this.buttonGroup.getSelection().getActionCommand();

            FormModel fm = new FormModel(name, occupation, age, employment, isUsCitizen, taxID, gender);
            this.listener.execute(fm);
        });
    }

    protected void layoutComponents() {
        this.gc.gridx = 0;
        this.gc.gridy = 0;
        this.gc.fill = GridBagConstraints.NONE;

        this.addComponent(1, 0, this.nameLabel, GridBagConstraints.FIRST_LINE_END, new Insets(0, 0, 0, 5), 0.1);
        this.addComponent(1, 1, this.nameInput, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(2, 0, this.occupationLabel, GridBagConstraints.FIRST_LINE_END, new Insets(0, 0, 0, 5));
        this.addComponent(2, 1, this.occupationInput, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(3, 0, this.ageLabel, GridBagConstraints.FIRST_LINE_END);
        this.addComponent(3, 1, this.ageInput, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(4, 0, this.employmentLabel, GridBagConstraints.FIRST_LINE_END);
        this.addComponent(4, 1, this.employmentInput, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(5, 0, this.isUsCitizenLabel, GridBagConstraints.FIRST_LINE_END);
        this.addComponent(5, 1, this.isUsCitizenInput, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(6, 0, this.taxIDLabel, GridBagConstraints.FIRST_LINE_END);
        this.addComponent(6, 1, this.taxIDInput, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(7, 0, this.genderLabel, GridBagConstraints.FIRST_LINE_END);

        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new BoxLayout(genderPanel, BoxLayout.X_AXIS));
        genderPanel.add(this.maleInput);
        genderPanel.add(this.femaleInput);

        this.addComponent(7, 1, genderPanel, GridBagConstraints.FIRST_LINE_START);
        this.addComponent(8, 1, this.addButton, GridBagConstraints.FIRST_LINE_START, 3, 1);
    }

    public void setListener(FormListener l) {
        this.listener = l;
    }

    static class AgeCategory {
        private String text;
        private int id;

        public AgeCategory(String text, int id) {
            this.text = text;
            this.id = id;
        }

        public String getText() {
            return this.text;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public String toString() {
            return this.text;
        }
    }
}


