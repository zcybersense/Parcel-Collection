package org.example.ui;

import org.example.model.Parcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Queue;

class CustomerPanel extends JPanel {
    private static Queue<String> customerData;
    private JTable customerTable;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Table model for customers (formatted for better presentation)
        String[] columnNames = {"Customer Name", "Details"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);

        // Populate table with data
        if (customerData != null) {
            for (String customer : customerData) {
                String[] parts = customer.split(": ");
                String name = parts.length > 0 ? parts[0] : "Unknown";
                String details = parts.length > 1 ? parts[1] : "";
                Object[] row = {name, details};
                tableModel.addRow(row);
            }
        }

        // Add table to the panel
        add(new JScrollPane(customerTable), BorderLayout.CENTER);
    }

    public static void setCustomerData(Queue<String> data) {
        customerData = data;
    }
}