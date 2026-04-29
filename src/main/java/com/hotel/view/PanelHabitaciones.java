package com.hotel.view;

import com.hotel.model.Habitacion;
import com.hotel.service.HabitacionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelHabitaciones extends JPanel {

    private JTextField txtNumero;
    private JComboBox<String> cbTipo;
    private JSpinner spCapacidad;
    private JComboBox<String> cbEstado;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private Integer idSeleccionado = null;

    private final HabitacionService habitacionService = new HabitacionService();

    public PanelHabitaciones() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 10));

        JPanel panelIzquierdo = crearPanelFormulario();
        JPanel panelDerecho = crearPanelTabla();

        panelPrincipal.add(panelIzquierdo, BorderLayout.WEST);
        panelPrincipal.add(panelDerecho, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);

        cargarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 10));
        panelContenedor.setPreferredSize(new Dimension(390, 0));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la habitación"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNumero = new JTextField();
        cbTipo = new JComboBox<>(new String[]{"Individual", "Doble", "Triple"});
        spCapacidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spCapacidad.setEnabled(false);
        cbEstado = new JComboBox<>(new String[]{"LIBRE", "OCUPADA"});

        Dimension campoDimension = new Dimension(210, 26);
        txtNumero.setPreferredSize(campoDimension);
        cbTipo.setPreferredSize(campoDimension);
        spCapacidad.setPreferredSize(campoDimension);
        cbEstado.setPreferredSize(campoDimension);

        cbTipo.addActionListener(e -> actualizarCapacidadSegunTipo());

        int fila = 0;

        addCampo(panelFormulario, gbc, fila++, "Número:", txtNumero);
        addCampo(panelFormulario, gbc, fila++, "Tipo:", cbTipo);
        addCampo(panelFormulario, gbc, fila++, "Capacidad:", spCapacidad);
        addCampo(panelFormulario, gbc, fila++, "Estado:", cbEstado);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCargarSel = new JButton("Cargar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargarSel);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        btnGuardar.addActionListener(e -> guardarHabitacion());
        btnCargarSel.addActionListener(e -> cargarSeleccionadoEnFormulario());
        btnActualizar.addActionListener(e -> actualizarHabitacion());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        JPanel panelFormularioCompleto = new JPanel(new BorderLayout());
        panelFormularioCompleto.add(panelFormulario, BorderLayout.NORTH);
        panelFormularioCompleto.add(panelBotones, BorderLayout.CENTER);

        panelContenedor.add(panelFormularioCompleto, BorderLayout.NORTH);

        return panelContenedor;
    }

    private JPanel crearPanelTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Número", "Tipo", "Capacidad", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        configurarTabla();

        JScrollPane scrollTabla = new JScrollPane(tabla);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder("Habitaciones registradas"));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        return panelTabla;
    }

    private void configurarTabla() {
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(140);
    }

    private void addCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.weightx = 1;
        panel.add(componente, gbc);
    }

    private void guardarHabitacion() {
        String numero = txtNumero.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de habitación es obligatorio.");
            return;
        }

        try {
            Habitacion h = new Habitacion();
            h.setNumero(numero);
            h.setTipo((String) cbTipo.getSelectedItem());
            h.setCapacidad((Integer) spCapacidad.getValue());
            h.setEstado((String) cbEstado.getSelectedItem());

            habitacionService.guardarHabitacion(h);

            JOptionPane.showMessageDialog(this, "Habitación guardada.");
            limpiarCampos();
            cargarTabla();

        } catch (Exception ex) {
            String mensaje = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

            if (mensaje.contains("unique") && mensaje.contains("habitacion.numero")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ya existe una habitación con ese número.",
                        "Número duplicado",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Error guardando: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);

            List<Habitacion> lista = habitacionService.listarTodas();

            for (Habitacion h : lista) {
                modeloTabla.addRow(new Object[]{
                        h.getId(),
                        h.getNumero(),
                        h.getTipo(),
                        h.getCapacidad(),
                        h.getEstado()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando tabla: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void cargarSeleccionadoEnFormulario() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una habitación en la tabla.");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);

        idSeleccionado = (Integer) modeloTabla.getValueAt(filaModelo, 0);

        txtNumero.setText((String) modeloTabla.getValueAt(filaModelo, 1));
        cbTipo.setSelectedItem((String) modeloTabla.getValueAt(filaModelo, 2));
        spCapacidad.setValue((Integer) modeloTabla.getValueAt(filaModelo, 3));
        cbEstado.setSelectedItem((String) modeloTabla.getValueAt(filaModelo, 4));
    }

    private void actualizarHabitacion() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Primero carga una habitación seleccionada para editarla.");
            return;
        }

        String numero = txtNumero.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de habitación es obligatorio.");
            return;
        }

        try {
            Habitacion h = new Habitacion();
            h.setId(idSeleccionado);
            h.setNumero(numero);
            h.setTipo((String) cbTipo.getSelectedItem());
            h.setCapacidad((Integer) spCapacidad.getValue());
            h.setEstado((String) cbEstado.getSelectedItem());

            boolean ok = habitacionService.actualizarHabitacion(h);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Habitación actualizada.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la habitación.");
            }

        } catch (Exception ex) {
            String mensaje = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

            if (mensaje.contains("unique") && mensaje.contains("habitacion.numero")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ese número ya está asignado a otra habitación.",
                        "Número duplicado",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Error actualizando: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una habitación en la tabla.");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        int id = (Integer) modeloTabla.getValueAt(filaModelo, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar la habitación seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean ok = habitacionService.eliminarHabitacion(id);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Habitación eliminada.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la habitación.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se puede eliminar la habitación porque tiene estancias asociadas.",
                    "Habitación en uso",
                    JOptionPane.WARNING_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNumero.setText("");
        cbTipo.setSelectedIndex(0);
        spCapacidad.setValue(1);
        cbEstado.setSelectedItem("LIBRE");

        idSeleccionado = null;
        tabla.clearSelection();
    }

    private void actualizarCapacidadSegunTipo() {
        String tipo = (String) cbTipo.getSelectedItem();

        switch (tipo) {
            case "Individual":
                spCapacidad.setValue(1);
                break;
            case "Doble":
                spCapacidad.setValue(2);
                break;
            case "Triple":
                spCapacidad.setValue(3);
                break;
        }
    }
}