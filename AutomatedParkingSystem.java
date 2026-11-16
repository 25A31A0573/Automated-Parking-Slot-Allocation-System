import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AutomatedParkingSystem extends JFrame {

    JTextField vehicleNumber;
    JComboBox<String> vehicleType;
    JTable table;
    DefaultTableModel model;

    JLabel totalLabel, availableLabel, occupiedLabel;

    ArrayList<String> parkedVehicles = new ArrayList<>();

    final int TOTAL_SLOTS = 15;

    AutomatedParkingSystem() {

        setTitle("Automated Parking Slot Allocation System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Heading
        JLabel heading = new JLabel(
            "AUTOMATED PARKING SLOT ALLOCATION SYSTEM",
            JLabel.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(35, 55, 80));

        add(heading, BorderLayout.NORTH);

        // Dashboard
        JPanel dashboard = new JPanel(new GridLayout(1, 3, 10, 10));
        dashboard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        totalLabel = new JLabel("Total Slots: 15", JLabel.CENTER);
        availableLabel = new JLabel("Available: 15", JLabel.CENTER);
        occupiedLabel = new JLabel("Occupied: 0", JLabel.CENTER);

        dashboard.add(totalLabel);
        dashboard.add(availableLabel);
        dashboard.add(occupiedLabel);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Vehicle Details"));

        vehicleNumber = new JTextField();

        vehicleType = new JComboBox<>(
            new String[]{"Car", "Bike", "Truck", "Scooter", "Van"}
        );

        JButton entryButton = new JButton("Vehicle Entry");
        JButton exitButton = new JButton("Vehicle Exit");

        inputPanel.add(new JLabel("Vehicle Number:"));
        inputPanel.add(vehicleNumber);

        inputPanel.add(new JLabel("Vehicle Type:"));
        inputPanel.add(vehicleType);

        inputPanel.add(entryButton);
        inputPanel.add(exitButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {
            "Slot Number",
            "Vehicle Number",
            "Vehicle Type"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        entryButton.addActionListener(e -> vehicleEntry());
        exitButton.addActionListener(e -> vehicleExit());

        // Center Layout
        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(dashboard, BorderLayout.NORTH);
        centerPanel.add(mainPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
            "Welcome to Automated Parking Management",
            JLabel.CENTER
        );

        footer.setFont(new Font("Arial", Font.ITALIC, 14));

        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Vehicle Entry
    void vehicleEntry() {

        String number = vehicleNumber.getText().trim().toUpperCase();
        String type = (String) vehicleType.getSelectedItem();

        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter vehicle number!"
            );
            return;
        }

        if (parkedVehicles.contains(number)) {
            JOptionPane.showMessageDialog(
                this, "This vehicle is already parked!"
            );
            return;
        }

        if (parkedVehicles.size() >= TOTAL_SLOTS) {
            JOptionPane.showMessageDialog(
                this, "Parking is Full!"
            );
            return;
        }

        int slot = 1;

        // Find the first available slot
        for (int i = 0; i < model.getRowCount(); i++) {

            int existingSlot =
                Integer.parseInt(model.getValueAt(i, 0).toString());

            if (existingSlot == slot) {
                slot++;
                i = -1;
            }
        }

        parkedVehicles.add(number);

        model.addRow(new Object[]{
            slot, number, type
        });

        vehicleNumber.setText("");

        updateDashboard();

        JOptionPane.showMessageDialog(
            this, "Vehicle Parked Successfully!\nAllocated Slot: " + slot
        );
    }

    // Vehicle Exit
    void vehicleExit() {

        String number = vehicleNumber.getText().trim().toUpperCase();

        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Enter vehicle number to exit!"
            );
            return;
        }

        boolean found = false;

        for (int i = 0; i < model.getRowCount(); i++) {

            String parkedNumber =
                model.getValueAt(i, 1).toString();

            if (parkedNumber.equals(number)) {

                model.removeRow(i);
                parkedVehicles.remove(number);

                found = true;

                JOptionPane.showMessageDialog(
                    this,
                    "Vehicle Exit Successful!\nParking Fee: Rs. 50"
                );

                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(
                this, "Vehicle Not Found!"
            );
        }

        updateDashboard();
        vehicleNumber.setText("");
    }

    // Update Dashboard
    void updateDashboard() {

        int occupied = model.getRowCount();
        int available = TOTAL_SLOTS - occupied;

        totalLabel.setText("Total Slots: " + TOTAL_SLOTS);
        availableLabel.setText("Available: " + available);
        occupiedLabel.setText("Occupied: " + occupied);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new AutomatedParkingSystem()
        );
    }
}