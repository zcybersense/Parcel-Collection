package org.example.ui;

import org.example.model.DepotWorker;
import org.example.model.Parcel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private JButton button;
    private DepotWorker depotWorker;
    private Parcel currentParcel;

    public ButtonEditor(JCheckBox checkBox, DepotWorker depotWorker) {
        this.depotWorker = depotWorker;
        button = new JButton("Action");
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonClick();
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return currentParcel; // This value will be returned when editing is finished
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentParcel = (Parcel) value; // Get the parcel for the current row
        button.setText(currentParcel.getStatus().equals("processing") ? "Collect" : "Wait");
        return button;
    }

    private void onButtonClick() {
        // Check the status and decide the action
        if ("processing".equals(currentParcel.getStatus())) {
            depotWorker.handleCollect(currentParcel);
            JOptionPane.showMessageDialog(button, "Parcel collected successfully!");
        } else if ("waiting".equals(currentParcel.getStatus())) {
            depotWorker.handleWait(currentParcel);
            JOptionPane.showMessageDialog(button, "Parcel marked as waiting!");
        }
        fireEditingStopped(); // Close the editor
    }
}
