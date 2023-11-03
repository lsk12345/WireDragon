package com.lsk.app;

import com.lsk.Capture;
import com.lsk.Main;
import com.lsk.model.MyPacket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ComponentUtils {
    public static JTable getMyTable(DefaultTableModel model,
                                    Font headerFont,
                                    int headerHeight,
                                    Font rowFont,
                                    int rowHeight){
        JTable table = new JTable(model);
        JTableHeader tabHeader = table.getTableHeader();
        tabHeader.setFont(headerFont);
        tabHeader.setPreferredSize(new Dimension(tabHeader.getWidth(), headerHeight));
        table.setDefaultEditor(Object.class, null);
        table.setFont(rowFont);
        table.setRowHeight(rowHeight);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.green);
        table.setSelectionForeground(Color.blue);
        return table;
    }


    public static JTable getMyTable(DefaultTableModel model,
                                    Font headerFont,
                                    int headerHeight
    ) {
        JTable table = new JTable(model);
        JTableHeader tabHeader = table.getTableHeader();
        tabHeader.setFont(headerFont);
        tabHeader.setPreferredSize(new Dimension(tabHeader.getWidth(), headerHeight));
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.green);
        table.setSelectionForeground(Color.blue);
        return table;
    }

    public static JTable getMyTable(DefaultTableModel model
    ) {
        JTable table = new JTable(model);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.green);
        table.setSelectionForeground(Color.blue);
        return table;
    }
}
