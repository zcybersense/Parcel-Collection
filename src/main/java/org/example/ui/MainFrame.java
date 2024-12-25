package org.example.ui;

import org.example.model.Customer;
import org.example.model.DepotWorker;
import org.example.model.Parcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class MainFrame extends JFrame {

    private DepotWorker depotWorker;

    public MainFrame(DepotWorker depotWorker) {
        this.depotWorker = depotWorker;
        initialize();
    }

    private void initialize() {
        setTitle("Parcel Management System");
        setSize(1000, 600);  // Adjusted size for better appearance
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the main frame on the screen
        setLocationRelativeTo(null);

        // Table to display parcels
        String[] columnNames = {"Parcel ID", "Dimension", "Weight", "Days in Depot", "Status", "Fee", "Action"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), depotWorker));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons for various functionalities
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 10, 10));  // GridLayout with 3 columns, adjustable rows, and gaps between buttons
        add(panel, BorderLayout.SOUTH);

        // Adding buttons
        JButton btnShowParcelList = new JButton("Show Parcel List");
        btnShowParcelList.addActionListener(e -> showParcelList());
        panel.add(btnShowParcelList);

        JButton btnShowCustomerQueue = new JButton("Show Customer Queue");
        btnShowCustomerQueue.addActionListener(e -> showCustomerQueue());
        panel.add(btnShowCustomerQueue);

        JButton btnShowCollected = new JButton("Show Collected Parcels");
        btnShowCollected.addActionListener(e -> showCollectedParcels(tableModel));
        panel.add(btnShowCollected);

        JButton btnShowWaiting = new JButton("Show Waiting Parcels");
        btnShowWaiting.addActionListener(e -> showWaitingParcels(tableModel));
        panel.add(btnShowWaiting);

        JButton btnProcessCustomer = new JButton("Process Customer");
        btnProcessCustomer.addActionListener(e -> processCustomers());
        panel.add(btnProcessCustomer);

        JButton btnFindParcel = new JButton("Find Parcel");
        btnFindParcel.addActionListener(e -> findParcel());
        panel.add(btnFindParcel);

        JButton btnFindStatus = new JButton("Find Status");
        btnFindStatus.addActionListener(e -> findStatus());
        panel.add(btnFindStatus);

        // Buttons for report generation
        JButton btnGenerateCollectedReport = new JButton("Generate Collected Report");
        btnGenerateCollectedReport.addActionListener(e -> generateCollectedReport());
        panel.add(btnGenerateCollectedReport);

        JButton btnGenerateRevenueReport = new JButton("Generate Revenue Report");
        btnGenerateRevenueReport.addActionListener(e -> generateRevenueReport());
        panel.add(btnGenerateRevenueReport);

        JButton btnGenerateWaitingReport = new JButton("Generate Waiting Report");
        btnGenerateWaitingReport.addActionListener(e -> generateWaitingReport());
        panel.add(btnGenerateWaitingReport);

        setVisible(true);
    }

    private JFrame createCenteredFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);
        return frame;
    }

    private void showParcelList() {
        JFrame parcelListFrame = createCenteredFrame("Parcel List", 400, 300);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> parcelList = new JList<>(listModel);

        for (Parcel parcel : depotWorker.getParcelList()) {
            listModel.addElement(parcel.getParcelID());
        }

        parcelListFrame.add(new JScrollPane(parcelList));
        parcelListFrame.setVisible(true);
    }

    private void showCustomerQueue() {
        JFrame customerQueueFrame = createCenteredFrame("Customer Queue", 400, 300);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> customerList = new JList<>(listModel);

        for (Customer customer : depotWorker.getCustomerQueue()) {
            listModel.addElement(customer.getName() + " (Parcel ID: " + customer.getParcelId() + ")");
        }

        customerQueueFrame.add(new JScrollPane(customerList));
        customerQueueFrame.setVisible(true);
    }

    private void showCollectedParcels(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);  // Clear existing rows
        for (Parcel parcel : depotWorker.getCollectedParcels()) {
            tableModel.addRow(new Object[]{
                    parcel.getParcelID(),
                    parcel.getDimension(),
                    parcel.getWeight(),
                    parcel.getDaysInDepot(),
                    parcel.getStatus(),
                    parcel.getFee(),
                    "N/A"
            });
        }
    }

    private void showWaitingParcels(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);  // Clear existing rows
        for (Parcel parcel : depotWorker.getWaitingParcels()) {
            tableModel.addRow(new Object[]{
                    parcel.getParcelID(),
                    parcel.getDimension(),
                    parcel.getWeight(),
                    parcel.getDaysInDepot(),
                    parcel.getStatus(),
                    parcel.getFee(),
                    "N/A"
            });
        }
    }

    private void processCustomers() {
        while (!depotWorker.getCustomerQueue().isEmpty()) {
            Customer customer = depotWorker.getCustomerQueue().peek(); // Peek does not remove the customer
            Parcel matchedParcel = depotWorker.getParcelList().stream()
                    .filter(parcel -> parcel.getParcelID().equals(customer.getParcelId()))
                    .findFirst()
                    .orElse(null);

            if (matchedParcel == null) {
                depotWorker.getCustomerQueue().poll();
                JOptionPane.showMessageDialog(this, "No matching parcel found for customer: " + customer.getName() + ". Skipping to next.", "Warning", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            JFrame processCustomerFrame = createCenteredFrame("Process Customer", 500, 400);
            processCustomerFrame.setLayout(new BorderLayout());

            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel nameLabel = new JLabel("Customer Name: " + customer.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel parcelIdLabel = new JLabel("Parcel ID: " + customer.getParcelId());
            parcelIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel feeLabel = new JLabel("Fee: " + String.format("%.2f", matchedParcel.getFee()));
            feeLabel.setFont(new Font("Arial", Font.BOLD, 16));

            infoPanel.add(nameLabel);
            infoPanel.add(parcelIdLabel);
            infoPanel.add(feeLabel);

            processCustomerFrame.add(infoPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton collectButton = new JButton("Collect");
            JButton waitButton = new JButton("Wait");

            collectButton.setFont(new Font("Arial", Font.BOLD, 14));
            waitButton.setFont(new Font("Arial", Font.BOLD, 14));

            collectButton.addActionListener(e -> {
                depotWorker.handleCollect(matchedParcel);
                depotWorker.getCustomerQueue().poll(); // Remove the customer from the queue
                JOptionPane.showMessageDialog(processCustomerFrame, "Parcel collected successfully!");
                processCustomerFrame.dispose();
            });

            waitButton.addActionListener(e -> {
                depotWorker.handleWait(matchedParcel);
                JOptionPane.showMessageDialog(processCustomerFrame, "Parcel marked as waiting!");
                processCustomerFrame.dispose();
            });

            buttonPanel.add(collectButton);
            buttonPanel.add(waitButton);
            processCustomerFrame.add(buttonPanel, BorderLayout.SOUTH);

            processCustomerFrame.setVisible(true);
            break;
        }

        if (depotWorker.getCustomerQueue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No more customers in the queue!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void findParcel() {
        String parcelId = JOptionPane.showInputDialog(this, "Enter Parcel ID:", "Find Parcel", JOptionPane.QUESTION_MESSAGE);

        if (parcelId != null) {
            Optional<Parcel> parcel = depotWorker.getParcelList().stream()
                    .filter(p -> p.getParcelID().equals(parcelId))
                    .findFirst();

            if (parcel.isPresent()) {
                JOptionPane.showMessageDialog(this,
                        "Parcel Found!\nID: " + parcel.get().getParcelID() +
                                "\nDimension: " + parcel.get().getDimension() +
                                "\nWeight: " + parcel.get().getWeight() +
                                "\nStatus: " + parcel.get().getStatus() +
                                "\nFee: " + parcel.get().getFee(),
                        "Parcel Details",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Optional<Parcel> collectedParcel = depotWorker.getCollectedParcels().stream()
                    .filter(p -> p.getParcelID().equals(parcelId))
                    .findFirst();

            if (collectedParcel.isPresent()) {
                JOptionPane.showMessageDialog(this,
                        "Parcel Found!\nID: " + collectedParcel.get().getParcelID() +
                                "\nStatus: collected",
                        "Parcel Status",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Optional<Parcel> waitingParcel = depotWorker.getWaitingParcels().stream()
                    .filter(p -> p.getParcelID().equals(parcelId))
                    .findFirst();

            if (waitingParcel.isPresent()) {
                JOptionPane.showMessageDialog(this,
                        "Parcel Found!\nID: " + waitingParcel.get().getParcelID() +
                                "\nStatus: waiting",
                        "Parcel Status",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Parcel not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findStatus() {
        String parcelId = JOptionPane.showInputDialog(this, "Enter Parcel ID:", "Find Status", JOptionPane.QUESTION_MESSAGE);

        if (parcelId != null) {
            Optional<Parcel> parcel = depotWorker.getParcelList().stream()
                    .filter(p -> p.getParcelID().equals(parcelId))
                    .findFirst();

            if (parcel.isPresent()) {
                String status = parcel.get().getStatus();
                JOptionPane.showMessageDialog(this,
                        "Parcel ID: " + parcel.get().getParcelID() +
                                "\nStatus: " + status,
                        "Parcel Status",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Check if the parcel is in collected or waiting lists
                Optional<Parcel> collectedParcel = depotWorker.getCollectedParcels().stream()
                        .filter(p -> p.getParcelID().equals(parcelId))
                        .findFirst();
                Optional<Parcel> waitingParcel = depotWorker.getWaitingParcels().stream()
                        .filter(p -> p.getParcelID().equals(parcelId))
                        .findFirst();

                if (collectedParcel.isPresent()) {
                    JOptionPane.showMessageDialog(this,
                            "Parcel ID: " + collectedParcel.get().getParcelID() +
                                    "\nStatus: collected",
                            "Parcel Status",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (waitingParcel.isPresent()) {
                    JOptionPane.showMessageDialog(this,
                            "Parcel ID: " + waitingParcel.get().getParcelID() +
                                    "\nStatus: waiting",
                            "Parcel Status",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Parcel not found in any list
                    JOptionPane.showMessageDialog(this, "Parcel not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Generate collected report
    private void generateCollectedReport() {
        List<Parcel> collectedParcels = depotWorker.getCollectedParcels();
        StringBuilder report = new StringBuilder("Collected Parcels Report\n\n");
        for (Parcel parcel : collectedParcels) {
            report.append("Parcel ID: ").append(parcel.getParcelID())
                    .append("\nDimension: ").append(parcel.getDimension())
                    .append("\nWeight: ").append(parcel.getWeight())
                    .append("\nStatus: ").append(parcel.getStatus())
                    .append("\nFee: ").append(parcel.getFee())
                    .append("\n\n");
        }

        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(reportArea), "Collected Parcels Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Generate uncollected report
    private void generateUncollectedReport() {
        List<Parcel> waitingParcels = depotWorker.getWaitingParcels();
        StringBuilder report = new StringBuilder("Uncollected Parcels Report\n\n");
        for (Parcel parcel : waitingParcels) {
            report.append("Parcel ID: ").append(parcel.getParcelID())
                    .append("\nDimension: ").append(parcel.getDimension())
                    .append("\nWeight: ").append(parcel.getWeight())
                    .append("\nStatus: ").append(parcel.getStatus())
                    .append("\nFee: ").append(parcel.getFee())
                    .append("\n\n");
        }

        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(reportArea), "Uncollected Parcels Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Generate revenue report
    private void generateRevenueReport() {
        double totalRevenue = depotWorker.getCollectedParcels().stream()
                .mapToDouble(Parcel::getFee)
                .sum();

        String report = "Total Revenue Report\n\nTotal Revenue: " + totalRevenue;
        JOptionPane.showMessageDialog(this, report, "Revenue Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Generate waiting report
    private void generateWaitingReport() {
        List<Parcel> waitingParcels = depotWorker.getWaitingParcels();
        StringBuilder report = new StringBuilder("Waiting Parcels Report\n\n");
        for (Parcel parcel : waitingParcels) {
            report.append("Parcel ID: ").append(parcel.getParcelID())
                    .append("\nDimension: ").append(parcel.getDimension())
                    .append("\nWeight: ").append(parcel.getWeight())
                    .append("\nStatus: ").append(parcel.getStatus())
                    .append("\nFee: ").append(parcel.getFee())
                    .append("\n\n");
        }

        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(reportArea), "Waiting Parcels Report", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        DepotWorker depotWorker = new DepotWorker();
        depotWorker.initializeParcel();  // Load parcels
        depotWorker.initializeCustomers();  // Load customers
        new MainFrame(depotWorker);
    }
}
