package com.hotel.view;

import com.hotel.dao.EstanciaDAO;
import com.hotel.dao.HabitacionDAO;
import com.hotel.dao.HuespedDAO;
import com.hotel.model.Estancia;
import com.hotel.model.Habitacion;
import com.hotel.model.HabitacionItem;
import com.hotel.model.Huesped;
import com.hotel.model.HuespedItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelEstancias extends JPanel {

    private JComboBox<HuespedItem> cbHuesped;
    private JComboBox<HabitacionItem> cbHabitacion;
    private JTextField txtFechaEntrada;
    private JTextField txtFechaSalida;
    private JComboBox<String> cbEstado;
    private JTextArea txtObservaciones;

    private final HuespedDAO huespedDAO = new HuespedDAO();
    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private final EstanciaDAO estanciaDAO = new EstanciaDAO();

    public PanelEstancias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva estancia"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbHuesped = new JComboBox<>();
        cbHabitacion = new JComboBox<>();
        txtFechaEntrada = new JTextField(12);
        txtFechaSalida = new JTextField(12);
        cbEstado = new JComboBox<>(new String[]{"PENDIENTE", "CHECKIN_REALIZADO"});
        txtObservaciones = new JTextArea(4, 20);

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Huésped:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(cbHuesped, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Habitación libre:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(cbHabitacion, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha entrada (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(txtFechaEntrada, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha salida (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(txtFechaSalida, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(cbEstado, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(new JScrollPane(txtObservaciones), gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGuardar = new JButton("Guardar estancia");
        JButton btnRecargar = new JButton("Recargar combos");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnRecargar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarEstancia());
        btnRecargar.addActionListener(e -> cargarCombos());

        cargarCombos();
    }

    private void cargarCombos() {
        try {
            cbHuesped.removeAllItems();
            cbHabitacion.removeAllItems();

            List<Huesped> huespedes = huespedDAO.listarParaCombo();
            for (Huesped h : huespedes) {
                String nombreCompleto = h.getNombre() + " " + h.getApellido1()
                        + (h.getApellido2() != null ? " " + h.getApellido2() : "");
                cbHuesped.addItem(new HuespedItem(h.getId(), nombreCompleto));
            }

            List<Habitacion> habitaciones = habitacionDAO.listarLibres();
            for (Habitacion h : habitaciones) {
                String texto = h.getNumero() + " - " + h.getTipo() + " (" + h.getCapacidad() + " pax)";
                cbHabitacion.addItem(new HabitacionItem(h.getId(), texto));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guardarEstancia() {
        HuespedItem huespedSeleccionado = (HuespedItem) cbHuesped.getSelectedItem();
        HabitacionItem habitacionSeleccionada = (HabitacionItem) cbHabitacion.getSelectedItem();

        String fechaEntrada = txtFechaEntrada.getText().trim();
        String fechaSalida = txtFechaSalida.getText().trim();

        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un huésped.");
            return;
        }

        if (habitacionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una habitación libre.");
            return;
        }

        if (fechaEntrada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de entrada es obligatoria.");
            return;
        }

        if (fechaSalida.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de salida es obligatoria.");
            return;
        }

        try {
            Estancia estancia = new Estancia();
            estancia.setFechaEntrada(fechaEntrada);
            estancia.setFechaSalida(fechaSalida);
            estancia.setHabitacionId(habitacionSeleccionada.getId());
            estancia.setEstado((String) cbEstado.getSelectedItem());
            estancia.setObservaciones(txtObservaciones.getText().trim());

            int id = estanciaDAO.insertar(estancia, huespedSeleccionado.getId());

            JOptionPane.showMessageDialog(this, "Estancia guardada correctamente. ID: " + id);

            limpiarFormulario();
            cargarCombos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error guardando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        cbHuesped.setSelectedIndex(-1);
        cbHabitacion.setSelectedIndex(-1);
        txtFechaEntrada.setText("");
        txtFechaSalida.setText("");
        cbEstado.setSelectedIndex(0);
        txtObservaciones.setText("");
    }
}
