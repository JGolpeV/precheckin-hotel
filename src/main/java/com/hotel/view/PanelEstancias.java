package com.hotel.view;

import com.hotel.dao.HabitacionDAO;
import com.hotel.dao.HuespedDAO;
import com.hotel.model.Estancia;
import com.hotel.model.Habitacion;
import com.hotel.model.HabitacionItem;
import com.hotel.model.Huesped;
import com.hotel.model.HuespedItem;
import com.hotel.service.EstanciaService;
import com.hotel.model.Huesped;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelEstancias extends JPanel {

    private JComboBox<HabitacionItem> cbHabitacion;
    private JSpinner spFechaEntrada;
    private JSpinner spFechaSalida;
    private JComboBox<String> cbEstado;
    private JTextArea txtObservaciones;
    private JLabel lblHuespedSeleccionado;
    private Huesped huespedSeleccionado;


    private final HuespedDAO huespedDAO = new HuespedDAO();
    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private final EstanciaService estanciaService = new EstanciaService();


    private JTable tablaEstancias;
    private javax.swing.table.DefaultTableModel modeloTablaEstancias;

    public PanelEstancias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva estancia"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbHabitacion = new JComboBox<>();

        spFechaEntrada = new JSpinner(new SpinnerDateModel());
        spFechaSalida = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorEntrada = new JSpinner.DateEditor(spFechaEntrada, "dd-MM-yyyy");
        JSpinner.DateEditor editorSalida = new JSpinner.DateEditor(spFechaSalida, "dd-MM-yyyy");
        spFechaEntrada.setEditor(editorEntrada);
        spFechaSalida.setEditor(editorSalida);

        // Por defecto fecha entrada hoy y salida +1 día
        Date hoy = new Date();
        spFechaEntrada.setValue(hoy);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        spFechaSalida.setValue(cal.getTime());

        cbEstado = new JComboBox<>(new String[]{"PENDIENTE", "CHECKIN_REALIZADO"});
        txtObservaciones = new JTextArea(4, 20);

        JButton btnBuscarHabitaciones = new JButton("Buscar habitaciones disponibles");
        JButton btnGuardar = new JButton("Guardar estancia");
        JButton btnEliminar = new JButton("Eliminar estancia");
        JButton btnSeleccionarHuesped = new JButton("Seleccionar huésped");
        lblHuespedSeleccionado = new JLabel("Ningún huésped seleccionado");

        int fila = 0;

        // Huésped

        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Huésped:"), gbc);

        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(btnSeleccionarHuesped, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(lblHuespedSeleccionado, gbc);

        // Fecha entrada
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha entrada:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(spFechaEntrada, gbc);

        // Fecha salida
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha salida:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(spFechaSalida, gbc);

        // Botón buscar habitaciones
        fila++;
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(btnBuscarHabitaciones, gbc);

        // Habitación
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Habitación disponible:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(cbHabitacion, gbc);

        // Estado
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(cbEstado, gbc);

        // Observaciones
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila; gbc.weightx = 1;
        panelFormulario.add(new JScrollPane(txtObservaciones), gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTablaEstancias = new javax.swing.table.DefaultTableModel(
                new Object[]{"ID", "Huésped", "Habitación", "Fecha entrada", "Fecha salida", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEstancias = new JTable(modeloTablaEstancias);
        add(new JScrollPane(tablaEstancias), BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarEstancia());
        btnBuscarHabitaciones.addActionListener(e -> buscarHabitacionesDisponibles());
        btnEliminar.addActionListener(e -> eliminarEstanciaSeleccionada());
        btnSeleccionarHuesped.addActionListener(e -> abrirSelectorHuesped());

        cargarCombos();
        cargarTablaEstancias();
    }

    //Verificar que fecha entrada es menos que fecha salida
    private boolean fechasValidas() {
        Date entrada = (Date) spFechaEntrada.getValue();
        Date salida = (Date) spFechaSalida.getValue();

        if (!estanciaService.fechasValidas(entrada, salida)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha de salida debe ser posterior a la fecha de entrada."
            );
            return false;
        }

        return true;
        }

    private void cargarCombos() {
        cbHabitacion.removeAllItems();
    }

    private void cargarTablaEstancias() {
        try {
            modeloTablaEstancias.setRowCount(0);

            java.util.List<String[]> lista = estanciaService.listarResumenEstancias();

            for (String[] fila : lista) {
                modeloTablaEstancias.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando estancias: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guardarEstancia() {
        if (!fechasValidas()) {
            return;
        }

        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un huésped.");
            return;
        }
        HabitacionItem habitacionSeleccionada = (HabitacionItem) cbHabitacion.getSelectedItem();

        String fechaEntrada = formatearFecha((Date) spFechaEntrada.getValue());
        String fechaSalida = formatearFecha((Date) spFechaSalida.getValue());

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

            int id = estanciaService.guardarEstancia(estancia, huespedSeleccionado.getId());

            JOptionPane.showMessageDialog(this, "Estancia guardada correctamente. ID: " + id);

            limpiarFormulario();
            cargarCombos();
            cargarTablaEstancias();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error guardando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscarHabitacionesDisponibles() {

        if (!fechasValidas()) {
            return;
        }

        String fechaEntrada = formatearFecha((Date) spFechaEntrada.getValue());
        String fechaSalida = formatearFecha((Date) spFechaSalida.getValue());

        if (fechaEntrada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes introducir la fecha de entrada.");
            return;
        }

        if (fechaSalida.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes introducir la fecha de salida.");
            return;
        }

        try {
            cbHabitacion.removeAllItems();

            List<Habitacion> habitaciones = estanciaService.buscarHabitacionesDisponibles(fechaEntrada, fechaSalida);

            if (habitaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles para esas fechas.");
                return;
            }

            for (Habitacion h : habitaciones) {
                String texto = h.getNumero() + " - " + h.getTipo() + " (" + h.getCapacidad() + " pax)";
                cbHabitacion.addItem(new HabitacionItem(h.getId(), texto));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error buscando habitaciones disponibles: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void eliminarEstanciaSeleccionada() {
        int fila = tablaEstancias.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una estancia en la tabla.");
            return;
        }

        int idEstancia = Integer.parseInt(modeloTablaEstancias.getValueAt(fila, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar la estancia con ID " + idEstancia + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean ok = estanciaService.eliminarEstancia(idEstancia);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Estancia eliminada correctamente.");
                cargarTablaEstancias();
                cargarCombos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la estancia.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error eliminando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        cbHabitacion.setSelectedIndex(-1);
        spFechaEntrada.setValue(new Date());
        spFechaSalida.setValue(new Date());
        cbEstado.setSelectedIndex(0);
        txtObservaciones.setText("");
        huespedSeleccionado = null;
        lblHuespedSeleccionado.setText("Ningún huésped seleccionado");
    }

    private String formatearFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }

    private void abrirSelectorHuesped() {
        SeleccionHuespedDialog dialog = new SeleccionHuespedDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        Huesped seleccionado = dialog.getHuespedSeleccionado();

        if (seleccionado != null) {
            huespedSeleccionado = seleccionado;

            String texto = seleccionado.getNombre() + " " + seleccionado.getApellido1();
            if (seleccionado.getApellido2() != null && !seleccionado.getApellido2().isBlank()) {
                texto += " " + seleccionado.getApellido2();
            }

            lblHuespedSeleccionado.setText(texto);
        }
    }
}
