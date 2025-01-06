package org.example.ui;

import org.example.model.Parcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Queue;
class ParcelPanel extends JPanel {
    private static List<Parcel> parcelData;
    private JTable parcelTable;

    public ParcelPanel() {
        setLayout(new BorderLayout());

        // Table model for parcels (only showing Parcel ID)
        String[] columnNames = {"Parcel ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        parcelTable = new JTable(tableModel);

        // Populate table with data
        if (parcelData != null) {
            for (Parcel parcel : parcelData) {
                Object[] row = {parcel.getParcelID()};
                tableModel.addRow(row);
            }
        }

        // Add table to the panel
        add(new JScrollPane(parcelTable), BorderLayout.CENTER);
    }

    public static void setParcelData(List<Parcel> data) {
        parcelData = data;
    }
}