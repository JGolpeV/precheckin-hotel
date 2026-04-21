package com.hotel.view;

import com.hotel.model.DocumentoIdentidad;
import com.hotel.model.Huesped;
import com.hotel.service.HuespedService;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class PanelHuespedes extends JPanel {

    private JTextField txtNombre;
    private JTextField txtApellido1;
    private JTextField txtApellido2;
    private JComboBox<String> cbSexo;
    private JTextField txtNacionalidad;
    private JTextField txtFechaNacimiento;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JTextField txtDireccion;
    private JTextField txtMunicipio;
    private JTextField txtCodigoPostal;
    private JTextField txtPaisResidencia;

    private JComboBox<String> cbTipoDocumento;
    private JTextField txtNumeroDocumento;
    private JTextField txtSoporteDocumento;
    private JTextField txtFechaCaducidad;

    private JTextField txtBuscar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private Integer idSeleccionado = null;

    private final HuespedService huespedService = new HuespedService();

    public PanelHuespedes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();

        JPanel panelDatosPersonales = crearPanelDatosPersonales();
        JPanel panelDireccion = crearPanelDireccion();
        JPanel panelDocumento = crearPanelDocumento();

        JPanel panelFormulario = new JPanel(new GridLayout(1, 3, 10, 10));
        panelFormulario.add(panelDatosPersonales);
        panelFormulario.add(panelDireccion);
        panelFormulario.add(panelDocumento);

        JPanel panelBotones = crearPanelBotones();
        JPanel panelBusqueda = crearPanelBusqueda();

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelFormulario, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        crearTabla();
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarTabla();
        configurarFiltroBusqueda();
    }

    private void inicializarComponentes() {
        txtNombre = new JTextField(20);
        txtApellido1 = new JTextField(20);
        txtApellido2 = new JTextField(20);
        cbSexo = new JComboBox<>(new String[]{"", "M", "F"});
        txtNacionalidad = new JTextField(20);
        txtFechaNacimiento = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtEmail = new JTextField(20);

        txtDireccion = new JTextField(20);
        txtMunicipio = new JTextField(20);
        txtCodigoPostal = new JTextField(20);
        txtPaisResidencia = new JTextField(20);

        cbTipoDocumento = new JComboBox<>(new String[]{"", "NIF", "NIE", "PASAPORTE"});
        txtNumeroDocumento = new JTextField(20);
        txtSoporteDocumento = new JTextField(20);
        txtFechaCaducidad = new JTextField(20);

        txtBuscar = new JTextField(20);
    }

    private JPanel crearPanelDatosPersonales() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos personales"));

        GridBagConstraints gbc = crearGbc();

        int fila = 0;

        addCampo(panel, gbc, fila++, "Nombre:", txtNombre);
        addCampo(panel, gbc, fila++, "Apellido 1:", txtApellido1);
        addCampo(panel, gbc, fila++, "Apellido 2:", txtApellido2);
        addCampo(panel, gbc, fila++, "Sexo:", cbSexo);
        addCampo(panel, gbc, fila++, "Nacionalidad:", txtNacionalidad);
        addCampo(panel, gbc, fila++, "Fecha nacimiento (YYYY-MM-DD):", txtFechaNacimiento);
        addCampo(panel, gbc, fila++, "Teléfono:", txtTelefono);
        addCampo(panel, gbc, fila++, "Email:", txtEmail);

        return panel;
    }

    private JPanel crearPanelDireccion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dirección"));

        GridBagConstraints gbc = crearGbc();

        int fila = 0;

        addCampo(panel, gbc, fila++, "Dirección:", txtDireccion);
        addCampo(panel, gbc, fila++, "Municipio:", txtMunicipio);
        addCampo(panel, gbc, fila++, "Código postal:", txtCodigoPostal);
        addCampo(panel, gbc, fila++, "País residencia:", txtPaisResidencia);

        return panel;
    }

    private JPanel crearPanelDocumento() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Documento de identidad"));

        GridBagConstraints gbc = crearGbc();

        int fila = 0;

        addCampo(panel, gbc, fila++, "Tipo documento:", cbTipoDocumento);
        addCampo(panel, gbc, fila++, "Número documento:", txtNumeroDocumento);
        addCampo(panel, gbc, fila++, "Soporte documento:", txtSoporteDocumento);
        addCampo(panel, gbc, fila++, "Fecha caducidad (YYYY-MM-DD):", txtFechaCaducidad);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCargarSeleccionado = new JButton("Cargar seleccionado");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        btnGuardar.addActionListener(e -> guardarHuesped());
        btnCargarSeleccionado.addActionListener(e -> cargarSeleccionadoEnFormulario());
        btnActualizar.addActionListener(e -> actualizarHuesped());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        panel.add(btnGuardar);
        panel.add(btnCargarSeleccionado);
        panel.add(btnActualizar);
        panel.add(btnEliminar);

        return panel;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        panel.add(new JLabel("Buscar huésped:"));
        panel.add(txtBuscar);

        return panel;
    }

    private void crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido 1", "Apellido 2", "Nacionalidad"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);
    }

    private void configurarFiltroBusqueda() {
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
    }

    private void filtrarTabla() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
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
            JOptionPane.showMessageDialog(this, "Error cargando huéspedes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarHuesped() {
        try {
            Huesped huesped = construirHuespedDesdeFormulario();
            DocumentoIdentidad documento = construirDocumentoDesdeFormulario();

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

            JOptionPane.showMessageDialog(this, "Huésped guardado correctamente.");

            limpiarCampos();
            cargarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error guardando huésped: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void actualizarHuesped() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Primero carga un huésped seleccionado para editarlo.");
            return;
        }

        try {
            Huesped huesped = construirHuespedDesdeFormulario();
            huesped.setId(idSeleccionado);

            DocumentoIdentidad documento = construirDocumentoDesdeFormulario();

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

            boolean ok = huespedService.actualizarHuespedConDocumento(huesped, documento);

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

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un huésped en la tabla.");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        int id = (Integer) modeloTabla.getValueAt(filaModelo, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar el huésped seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean ok = huespedService.eliminarHuesped(id);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Huésped eliminado correctamente.");
                limpiarCampos();
                idSeleccionado = null;
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el huésped.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error eliminando huésped: " + e.getMessage());
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

            txtNombre.setText(valor(h.getNombre()));
            txtApellido1.setText(valor(h.getApellido1()));
            txtApellido2.setText(valor(h.getApellido2()));
            cbSexo.setSelectedItem(h.getSexo() != null ? h.getSexo() : "");
            txtNacionalidad.setText(valor(h.getNacionalidad()));
            txtFechaNacimiento.setText(valor(h.getFechaNacimiento()));
            txtTelefono.setText(valor(h.getTelefono()));
            txtEmail.setText(valor(h.getEmail()));

            txtDireccion.setText(valor(h.getDireccion()));
            txtMunicipio.setText(valor(h.getMunicipio()));
            txtCodigoPostal.setText(valor(h.getCodigoPostal()));
            txtPaisResidencia.setText(valor(h.getPaisResidencia()));

            DocumentoIdentidad doc = huespedService.buscarDocumentoPorHuespedId(idSeleccionado);

            if (doc != null) {
                cbTipoDocumento.setSelectedItem(doc.getTipoDocumento() != null ? doc.getTipoDocumento() : "");
                txtNumeroDocumento.setText(valor(doc.getNumeroDocumento()));
                txtSoporteDocumento.setText(valor(doc.getSoporteDocumento()));
                txtFechaCaducidad.setText(valor(doc.getFechaCaducidad()));
            } else {
                cbTipoDocumento.setSelectedIndex(0);
                txtNumeroDocumento.setText("");
                txtSoporteDocumento.setText("");
                txtFechaCaducidad.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando huésped: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Huesped construirHuespedDesdeFormulario() {
        Huesped h = new Huesped();

        h.setNombre(txtNombre.getText().trim());
        h.setApellido1(txtApellido1.getText().trim());
        h.setApellido2(txtApellido2.getText().trim());
        h.setSexo((String) cbSexo.getSelectedItem());
        h.setNacionalidad(txtNacionalidad.getText().trim());
        h.setFechaNacimiento(txtFechaNacimiento.getText().trim());
        h.setTelefono(txtTelefono.getText().trim());
        h.setEmail(txtEmail.getText().trim());

        h.setDireccion(txtDireccion.getText().trim());
        h.setMunicipio(txtMunicipio.getText().trim());
        h.setCodigoPostal(txtCodigoPostal.getText().trim());
        h.setPaisResidencia(txtPaisResidencia.getText().trim());

        return h;
    }

    private DocumentoIdentidad construirDocumentoDesdeFormulario() {
        DocumentoIdentidad documento = new DocumentoIdentidad();
        documento.setTipoDocumento((String) cbTipoDocumento.getSelectedItem());
        documento.setNumeroDocumento(txtNumeroDocumento.getText().trim());
        documento.setSoporteDocumento(txtSoporteDocumento.getText().trim());
        documento.setFechaCaducidad(txtFechaCaducidad.getText().trim());
        return documento;
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido1.setText("");
        txtApellido2.setText("");
        cbSexo.setSelectedIndex(0);
        txtNacionalidad.setText("");
        txtFechaNacimiento.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");

        txtDireccion.setText("");
        txtMunicipio.setText("");
        txtCodigoPostal.setText("");
        txtPaisResidencia.setText("");

        cbTipoDocumento.setSelectedIndex(0);
        txtNumeroDocumento.setText("");
        txtSoporteDocumento.setText("");
        txtFechaCaducidad.setText("");

        idSeleccionado = null;
    }

    private GridBagConstraints crearGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
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

    private String valor(String texto) {
        return texto == null ? "" : texto;
    }
}