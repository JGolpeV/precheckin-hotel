package com.hotel.view;

import com.hotel.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(Usuario usuario) {
        setTitle("Pre-check-in Hotel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Huéspedes", new JScrollPane(new PanelHuespedes()));

        if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
            tabs.addTab("Habitaciones", new JScrollPane(new PanelHabitaciones()));
        }

        tabs.addTab("Estancias", new JScrollPane(new PanelEstancias()));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
