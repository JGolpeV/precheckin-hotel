package com.hotel.view;

import com.hotel.dao.HuespedDAO;
import com.hotel.model.DocumentoIdentidad;
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
    private JComboBox<String> cbSexo;
    private JTextField txtNacionalidad;
    private JTextField txtDireccion;
    private JTextField txtCodigoPostal;
    private JTextField txtPaisResidencia;
    private JTextField txtFechaNacimiento;
    private JTextField txtLugarNacimiento;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JComboBox<String> cbTipoDocumento;
    private JTextField txtNumeroDocumento;
    private JTextField txtSoporteDocumento;
    private JTextField txtFechaCaducidad;

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
        cbSexo = new JComboBox<>(new String[]{"F", "M", "Otro", "n/c"});
        txtNacionalidad = new JTextField(20);
        txtDireccion = new JTextField(20);
        txtCodigoPostal = new JTextField(20);
        txtPaisResidencia = new JTextField(20);
        txtFechaNacimiento = new JTextField(20);
        txtLugarNacimiento = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtEmail = new JTextField(20);
        cbTipoDocumento = new JComboBox<>(new String[]{"", "NIF", "NIE", "PASAPORTE"});
        txtNumeroDocumento = new JTextField(20);
        txtSoporteDocumento = new JTextField(20);
        txtFechaCaducidad = new JTextField(20);

        int fila = 0;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtNombre, gbc);

        // Apellido 1
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido 1:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtApellido1, gbc);

        // Apellido 2
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Apellido 2:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtApellido2, gbc);

        // Sexo
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Sexo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(cbSexo, gbc);

        // Nacionalidad
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nacionalidad:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtNacionalidad, gbc);

        // Fecha nacimiento
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha nacimiento (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtFechaNacimiento, gbc);

        // Lugar nacimiento
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Lugar nacimiento:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtLugarNacimiento, gbc);

        // Dirección
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Dirección:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtDireccion, gbc);

        // Código postal
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Código postal:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtCodigoPostal, gbc);

        // País residencia
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("País residencia:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtPaisResidencia, gbc);

        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtTelefono, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtEmail, gbc);

        // Tipo documento
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Tipo documento:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(cbTipoDocumento, gbc);

        // Número documento
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Número documento:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtNumeroDocumento, gbc);

        // Soporte documento
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Soporte documento:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtSoporteDocumento, gbc);

        // Fecha de caducidad documento
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha caducidad (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weightx = 1;
        panelFormulario.add(txtFechaCaducidad, gbc);

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
            huesped.setSexo((String) cbSexo.getSelectedItem());
            huesped.setNacionalidad(txtNacionalidad.getText().trim());
            huesped.setFechaNacimiento(txtFechaNacimiento.getText().trim());
            huesped.setLugarNacimiento(txtLugarNacimiento.getText().trim());
            huesped.setDireccion(txtDireccion.getText().trim());
            huesped.setCodigoPostal(txtCodigoPostal.getText().trim());
            huesped.setPaisResidencia(txtPaisResidencia.getText().trim());
            huesped.setTelefono(txtTelefono.getText().trim());
            huesped.setEmail(txtEmail.getText().trim());

            DocumentoIdentidad documento = new DocumentoIdentidad();

            documento.setTipoDocumento((String) cbTipoDocumento.getSelectedItem());
            documento.setNumeroDocumento(txtNumeroDocumento.getText().trim());
            documento.setSoporteDocumento(txtSoporteDocumento.getText().trim());
            documento.setFechaCaducidad(txtFechaCaducidad.getText().trim());

            String errorValidacion = huespedService.validarHuesped(huesped);
            if (errorValidacion != null) {
                JOptionPane.showMessageDialog(this, errorValidacion);
                return;
            }

            String errorDocumento = huespedService.validarDocumento(huesped, documento);
            if (errorDocumento != null) {
                JOptionPane.showMessageDialog(this, errorDocumento);
                return;
            }

            huespedService.guardarHuespedConDocumento(huesped, documento);

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
        cbSexo.setSelectedIndex(0);
        txtNacionalidad.setText("");
        idSeleccionado = null;
        tabla.clearSelection();
        txtFechaNacimiento.setText("");
        txtLugarNacimiento.setText("");
        txtDireccion.setText("");
        txtCodigoPostal.setText("");
        txtPaisResidencia.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cbTipoDocumento.setSelectedIndex(0);
        txtNumeroDocumento.setText("");
        txtSoporteDocumento.setText("");
        txtFechaCaducidad.setText("");
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

        int filaModelo = tabla.convertRowIndexToModel(fila);
        idSeleccionado = (Integer) modeloTabla.getValueAt(filaModelo, 0);

        try {
            Huesped h = huespedService.buscarPorId(idSeleccionado);

            if (h == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el huésped.");
                return;
            }

            txtNombre.setText(h.getNombre() != null ? h.getNombre() : "");
            txtApellido1.setText(h.getApellido1() != null ? h.getApellido1() : "");
            txtApellido2.setText(h.getApellido2() != null ? h.getApellido2() : "");
            cbSexo.setSelectedItem(h.getSexo() != null ? h.getSexo() : "");
            txtNacionalidad.setText(h.getNacionalidad() != null ? h.getNacionalidad() : "");
            txtFechaNacimiento.setText(h.getFechaNacimiento() != null ? h.getFechaNacimiento() : "");
            txtLugarNacimiento.setText(h.getLugarNacimiento() != null ? h.getLugarNacimiento() : "");
            txtDireccion.setText(h.getDireccion() != null ? h.getDireccion() : "");
            txtCodigoPostal.setText(h.getCodigoPostal() != null ? h.getCodigoPostal() : "");
            txtPaisResidencia.setText(h.getPaisResidencia() != null ? h.getPaisResidencia() : "");
            txtTelefono.setText(h.getTelefono() != null ? h.getTelefono() : "");
            txtEmail.setText(h.getEmail() != null ? h.getEmail() : "");

            DocumentoIdentidad doc = huespedService.buscarDocumentoPorHuespedId(idSeleccionado);

            if (doc != null) {
                cbTipoDocumento.setSelectedItem(doc.getTipoDocumento() != null ? doc.getTipoDocumento() : "");
                txtNumeroDocumento.setText(doc.getNumeroDocumento() != null ? doc.getNumeroDocumento() : "");
                txtSoporteDocumento.setText(doc.getSoporteDocumento() != null ? doc.getSoporteDocumento() : "");
                txtFechaCaducidad.setText(doc.getFechaCaducidad() != null ? doc.getFechaCaducidad() : "");
            } else {
                cbTipoDocumento.setSelectedIndex(0);
                txtNumeroDocumento.setText("");
                txtSoporteDocumento.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando huésped: " + e.getMessage());
            e.printStackTrace();
        }
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
            h.setSexo((String) cbSexo.getSelectedItem());
            h.setNacionalidad(txtNacionalidad.getText().trim());
            h.setFechaNacimiento(txtFechaNacimiento.getText().trim());
            h.setLugarNacimiento(txtLugarNacimiento.getText().trim());
            h.setDireccion(txtDireccion.getText().trim());
            h.setCodigoPostal(txtCodigoPostal.getText().trim());
            h.setPaisResidencia(txtPaisResidencia.getText().trim());
            h.setTelefono(txtTelefono.getText().trim());
            h.setEmail(txtEmail.getText().trim());

            DocumentoIdentidad documento = new DocumentoIdentidad();
            documento.setTipoDocumento((String) cbTipoDocumento.getSelectedItem());
            documento.setNumeroDocumento(txtNumeroDocumento.getText().trim());
            documento.setSoporteDocumento(txtSoporteDocumento.getText().trim());

            String errorValidacion = huespedService.validarHuesped(h);
            if (errorValidacion != null) {
                JOptionPane.showMessageDialog(this, errorValidacion);
                return;
            }

            String errorDocumento = huespedService.validarDocumento(h, documento);
            if (errorDocumento != null) {
                JOptionPane.showMessageDialog(this, errorDocumento);
                return;
            }

            boolean ok = huespedService.actualizarHuespedConDocumento(h, documento);

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