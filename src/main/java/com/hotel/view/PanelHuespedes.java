package com.hotel.view;

import com.hotel.dao.HuespedDAO;
import com.hotel.model.Huesped;
import com.hotel.service.HuespedService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.RowFilter;

public class PanelHuespedes extends JPanel {

    private JTextField txtNombre;
    private JTextField txtApellido1;
    private JTextField txtApellido2;
    private JTextField txtNacionalidad;

    private Integer idSeleccionado = null;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtBuscar;
    private TableRowSorter<DefaultTableModel> sorter;

    private HuespedService huespedService = new HuespedService();

    public PanelHuespedes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel contenedor arriba (formulario + botones)
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del huésped"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(20);
        txtApellido1 = new JTextField(20);
        txtApellido2 = new JTextField(20);
        txtNacionalidad = new JTextField(20);

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelFormulario.add(txtNombre, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido 1:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        panelFormulario.add(txtApellido1, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido 2:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        panelFormulario.add(txtApellido2, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nacionalidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1;
        panelFormulario.add(txtNacionalidad, gbc);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCargar = new JButton("Cargar seleccionado");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        //Buscador huesped
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Búsqueda"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panelBusqueda.add(new JLabel("Buscar huésped:"));
        txtBuscar = new JTextField(20);
        panelBusqueda.add(txtBuscar);

        JPanel contenedorSuperior = new JPanel(new BorderLayout());
        contenedorSuperior.add(panelSuperior, BorderLayout.NORTH);
        contenedorSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        add(contenedorSuperior, BorderLayout.NORTH);


        // Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido 1", "Apellido 2", "Nacionalidad"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarHuesped());
        btnCargar.addActionListener(e -> cargarTabla());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnCargar.addActionListener(e -> cargarSeleccionadoEnFormulario());
        btnActualizar.addActionListener(e -> actualizarHuesped());

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabla();
            }
        });

        cargarTabla();
    }

    private void guardarHuesped() {

        try {
            Huesped huesped = new Huesped();

            huesped.setNombre(txtNombre.getText().trim());
            huesped.setApellido1(txtApellido1.getText().trim());
            huesped.setApellido2(txtApellido2.getText().trim());
            huesped.setNacionalidad(txtNacionalidad.getText().trim());

            String errorValidacion = huespedService.validarHuesped(huesped);
            if (errorValidacion != null) {
                JOptionPane.showMessageDialog(this, errorValidacion);
                return;
            }

            huespedService.guardarHuesped(huesped);

            JOptionPane.showMessageDialog(this, "Huésped guardado correctamente");

            limpiarCampos();
            cargarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error guardando huésped: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {

        try {
            modeloTabla.setRowCount(0);

            List<Huesped> lista = huespedService.listarTodos();

            for (Huesped h : lista) {
                modeloTabla.addRow(new Object[]{
                        h.getId(),
                        h.getNombre(),
                        h.getApellido1(),
                        h.getApellido2(),
                        h.getNacionalidad()
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido1.setText("");
        txtApellido2.setText("");
        txtNacionalidad.setText("");
        idSeleccionado = null;
        tabla.clearSelection();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un huésped en la tabla primero.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar el huésped con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = huespedService.eliminarHuesped(id);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Huésped eliminado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar (¿ya no existe?).");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error eliminando: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cargarSeleccionadoEnFormulario() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un huésped en la tabla.");
            return;
        }

        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);

        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtApellido1.setText((String) modeloTabla.getValueAt(fila, 2));
        txtApellido2.setText((String) modeloTabla.getValueAt(fila, 3));
        txtNacionalidad.setText((String) modeloTabla.getValueAt(fila, 4));
    }

    private void actualizarHuesped() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Primero carga un huésped seleccionado para editarlo.");
            return;
        }

        try {
            Huesped h = new Huesped();
            h.setId(idSeleccionado);
            h.setNombre(txtNombre.getText().trim());
            h.setApellido1(txtApellido1.getText().trim());
            h.setApellido2(txtApellido2.getText().trim());
            h.setNacionalidad(txtNacionalidad.getText().trim());

            String errorValidacion = huespedService.validarHuesped(h);
            if (errorValidacion != null) {
                JOptionPane.showMessageDialog(this, errorValidacion);
                return;
            }

            boolean ok = huespedService.actualizarHuesped(h);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Huésped actualizado correctamente.");
                limpiarCampos();
                idSeleccionado = null;
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error actualizando huésped: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filtrarTabla() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

}