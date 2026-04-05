package com.hotel.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Pre-check-in Hotel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Huéspedes", new PanelHuespedes());
        tabs.addTab("Habitaciones", new PanelHabitaciones());
        tabs.addTab("Estancias", new PanelEstancias());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
