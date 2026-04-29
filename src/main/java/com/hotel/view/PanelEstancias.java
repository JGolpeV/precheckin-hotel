package com.hotel.view;

import com.hotel.model.Estancia;
import com.hotel.model.Habitacion;
import com.hotel.model.HabitacionItem;
import com.hotel.model.Huesped;
import com.hotel.service.EstanciaService;
import com.hotel.service.XMLService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PanelEstancias extends JPanel {

    private JComboBox<HabitacionItem> cbHabitacion;
    private JSpinner spFechaEntrada;
    private JSpinner spFechaSalida;
    private JComboBox<String> cbEstado;
    private JTextArea txtObservaciones;

    private JLabel lblHuespedSeleccionado;
    private Huesped huespedSeleccionado;

    private DefaultListModel<Huesped> modeloAcompanantes;
    private JList<Huesped> listaAcompanantes;

    private JTable tablaEstancias;
    private DefaultTableModel modeloTablaEstancias;

    private JTextField txtFechaXML;
    private JTextField txtFiltroFechaEntrada;

    private Integer idEstanciaSeleccionada = null;

    private final EstanciaService estanciaService = new EstanciaService();

    public PanelEstancias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();

        JButton btnSeleccionarHuesped = new JButton("Seleccionar huésped");
        JButton btnAgregarAcompanante = new JButton("Añadir acompañante");
        JButton btnEliminarAcompanante = new JButton("Quitar acompañante");
        JButton btnBuscarHabitaciones = new JButton("Buscar");
        JButton btnGuardar = new JButton("Guardar estancia");
        JButton btnLimpiar = new JButton("Limpiar");

        JButton btnCargarEstancia = new JButton("Cargar seleccionada");
        JButton btnActualizarEstancia = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        JButton btnExportarXML = new JButton("Exportar XML");

        JPanel panelFormulario = crearPanelFormulario(
                btnSeleccionarHuesped,
                btnAgregarAcompanante,
                btnEliminarAcompanante,
                btnBuscarHabitaciones,
                btnGuardar,
                btnLimpiar
        );

        JPanel panelTabla = crearPanelTabla(btnCargarEstancia, btnActualizarEstancia, btnEliminar);
        JPanel panelXML = crearPanelXML(btnExportarXML);

        add(panelFormulario, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelXML, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarEstancia());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnBuscarHabitaciones.addActionListener(e -> buscarHabitacionesDisponibles());

        btnSeleccionarHuesped.addActionListener(e -> abrirSelectorHuesped());
        btnAgregarAcompanante.addActionListener(e -> agregarAcompanante());
        btnEliminarAcompanante.addActionListener(e -> quitarAcompanante());

        btnCargarEstancia.addActionListener(e -> cargarEstanciaSeleccionada());
        btnActualizarEstancia.addActionListener(e -> actualizarEstancia());
        btnEliminar.addActionListener(e -> eliminarEstanciaSeleccionada());

        btnExportarXML.addActionListener(e -> exportarXML());

        cargarCombos();
        cargarTablaEstancias();
    }

    private void inicializarComponentes() {
        cbHabitacion = new JComboBox<>();
        cbHabitacion.setEnabled(false);

        spFechaEntrada = new JSpinner(new SpinnerDateModel());
        spFechaSalida = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor editorEntrada = new JSpinner.DateEditor(spFechaEntrada, "dd-MM-yyyy");
        JSpinner.DateEditor editorSalida = new JSpinner.DateEditor(spFechaSalida, "dd-MM-yyyy");

        spFechaEntrada.setEditor(editorEntrada);
        spFechaSalida.setEditor(editorSalida);

        Date hoy = new Date();
        spFechaEntrada.setValue(hoy);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        spFechaSalida.setValue(cal.getTime());

        cbEstado = new JComboBox<>(new String[]{"PENDIENTE", "CHECKIN_REALIZADO"});

        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        lblHuespedSeleccionado = new JLabel("Ningún huésped seleccionado");

        modeloAcompanantes = new DefaultListModel<>();
        listaAcompanantes = new JList<>(modeloAcompanantes);

        txtFechaXML = new JTextField(10);
        txtFiltroFechaEntrada = new JTextField(10);
    }

    private JPanel crearPanelFormulario(
            JButton btnSeleccionarHuesped,
            JButton btnAgregarAcompanante,
            JButton btnEliminarAcompanante,
            JButton btnBuscarHabitaciones,
            JButton btnGuardar,
            JButton btnLimpiar
    ) {
        JPanel panelFormulario = new JPanel(new GridLayout(1, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la estancia"));

        JPanel panelIzquierdo = crearPanelHuespedes(
                btnSeleccionarHuesped,
                btnAgregarAcompanante,
                btnEliminarAcompanante
        );

        JPanel panelDerecho = crearPanelReserva(
                btnBuscarHabitaciones,
                btnGuardar,
                btnLimpiar
        );

        panelFormulario.add(panelIzquierdo);
        panelFormulario.add(panelDerecho);

        return panelFormulario;
    }

    private JPanel crearPanelHuespedes(
            JButton btnSeleccionarHuesped,
            JButton btnAgregarAcompanante,
            JButton btnEliminarAcompanante
    ) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Huéspedes"));

        GridBagConstraints gbc = crearGbc();

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel("Titular:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.weightx = 1;
        panel.add(lblHuespedSeleccionado, gbc);

        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(btnSeleccionarHuesped, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Acompañantes:"), gbc);

        JScrollPane scrollAcompanantes = new JScrollPane(listaAcompanantes);
        scrollAcompanantes.setPreferredSize(new Dimension(350, 80));

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollAcompanantes, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        fila++;

        JPanel panelBotonesAcompanantes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBotonesAcompanantes.add(btnAgregarAcompanante);
        panelBotonesAcompanantes.add(btnEliminarAcompanante);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(panelBotonesAcompanantes, gbc);

        gbc.gridwidth = 1;

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Observaciones:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(txtObservaciones), gbc);

        return panel;
    }

    private JPanel crearPanelReserva(
            JButton btnBuscarHabitaciones,
            JButton btnGuardar,
            JButton btnLimpiar
    ) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Reserva y habitación"));

        GridBagConstraints gbc = crearGbc();

        int fila = 0;

        addCampo(panel, gbc, fila++, "Fecha entrada:", spFechaEntrada);
        addCampo(panel, gbc, fila++, "Fecha salida:", spFechaSalida);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel("Habitación:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.weightx = 1;
        panel.add(cbHabitacion, gbc);

        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(btnBuscarHabitaciones, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(cbEstado, gbc);

        gbc.gridwidth = 1;

        fila++;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelTabla(
            JButton btnCargarEstancia,
            JButton btnActualizarEstancia,
            JButton btnEliminar
    ) {
        modeloTablaEstancias = new DefaultTableModel(
                new Object[]{"ID Estancia", "Huésped", "Habitación", "Fecha entrada", "Fecha salida", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEstancias = new JTable(modeloTablaEstancias);
        tablaEstancias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstancias.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tablaEstancias);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder("Estancias registradas"));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnFiltrarFecha = new JButton("Filtrar");
        JButton btnVerTodas = new JButton("Ver todas");

        panelFiltro.add(new JLabel("Fecha entrada (YYYY-MM-DD):"));
        panelFiltro.add(txtFiltroFechaEntrada);
        panelFiltro.add(btnFiltrarFecha);
        panelFiltro.add(btnVerTodas);

        JPanel panelBotonesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelBotonesTabla.add(btnCargarEstancia);
        panelBotonesTabla.add(btnActualizarEstancia);
        panelBotonesTabla.add(btnEliminar);

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout());
        panelSuperiorTabla.add(panelFiltro, BorderLayout.NORTH);
        panelSuperiorTabla.add(panelBotonesTabla, BorderLayout.SOUTH);

        panelTabla.add(panelSuperiorTabla, BorderLayout.NORTH);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        btnFiltrarFecha.addActionListener(e -> filtrarTablaPorFechaEntrada());
        btnVerTodas.addActionListener(e -> {
            txtFiltroFechaEntrada.setText("");
            cargarTablaEstancias();
        });

        return panelTabla;
    }

    private JPanel crearPanelXML(JButton btnExportarXML) {
        JPanel panelXML = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelXML.setBorder(BorderFactory.createTitledBorder("Exportación XML"));

        panelXML.add(new JLabel("Fecha entrada (YYYY-MM-DD):"));
        panelXML.add(txtFechaXML);
        panelXML.add(btnExportarXML);

        return panelXML;
    }

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
        cbHabitacion.setEnabled(false);
    }

    private void cargarTablaEstancias() {
        try {
            modeloTablaEstancias.setRowCount(0);

            List<String[]> lista = estanciaService.listarResumenEstancias();

            for (String[] fila : lista) {
                modeloTablaEstancias.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando estancias: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void filtrarTablaPorFechaEntrada() {
        String fecha = txtFiltroFechaEntrada.getText().trim();

        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una fecha de entrada.");
            return;
        }

        try {
            modeloTablaEstancias.setRowCount(0);

            List<String[]> lista = estanciaService.listarResumenEstancias();

            for (String[] fila : lista) {
                String fechaEntrada = fila[3];

                if (fecha.equals(fechaEntrada)) {
                    modeloTablaEstancias.addRow(fila);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error filtrando estancias: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void guardarEstancia() {
        if (!fechasValidas()) {
            return;
        }

        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un huésped titular.");
            return;
        }

        HabitacionItem habitacionSeleccionada = (HabitacionItem) cbHabitacion.getSelectedItem();

        if (habitacionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una habitación disponible.");
            return;
        }

        if (!capacidadValida()) {
            return;
        }

        String fechaEntrada = formatearFecha((Date) spFechaEntrada.getValue());
        String fechaSalida = formatearFecha((Date) spFechaSalida.getValue());

        try {
            List<Integer> acompanantesIds = new java.util.ArrayList<>();

            for (int i = 0; i < modeloAcompanantes.size(); i++) {
                acompanantesIds.add(modeloAcompanantes.get(i).getId());
            }

            Estancia estancia = new Estancia();
            estancia.setFechaEntrada(fechaEntrada);
            estancia.setFechaSalida(fechaSalida);
            estancia.setHabitacionId(habitacionSeleccionada.getId());
            estancia.setEstado((String) cbEstado.getSelectedItem());
            estancia.setObservaciones(txtObservaciones.getText().trim());

            int id = estanciaService.guardarEstanciaConHuespedes(
                    estancia,
                    huespedSeleccionado.getId(),
                    acompanantesIds
            );

            JOptionPane.showMessageDialog(this, "Estancia guardada correctamente. ID: " + id);

            limpiarFormulario();
            cargarTablaEstancias();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error guardando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void buscarHabitacionesDisponibles() {
        if (!fechasValidas()) {
            return;
        }

        String fechaEntrada = formatearFecha((Date) spFechaEntrada.getValue());
        String fechaSalida = formatearFecha((Date) spFechaSalida.getValue());

        try {
            cbHabitacion.removeAllItems();

            List<Habitacion> habitaciones;

            if (idEstanciaSeleccionada != null) {
                habitaciones = estanciaService.buscarHabitacionesDisponiblesExcluyendoEstancia(
                        fechaEntrada,
                        fechaSalida,
                        idEstanciaSeleccionada
                );
            } else {
                habitaciones = estanciaService.buscarHabitacionesDisponibles(fechaEntrada, fechaSalida);
            }

            if (habitaciones.isEmpty()) {
                cbHabitacion.setEnabled(false);
                JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles para esas fechas.");
                return;
            }

            for (Habitacion h : habitaciones) {
                String texto = h.getNumero() + " - " + h.getTipo() + " (" + h.getCapacidad() + " pax)";
                cbHabitacion.addItem(new HabitacionItem(h.getId(), texto, h.getCapacidad()));
            }

            cbHabitacion.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error buscando habitaciones disponibles: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void eliminarEstanciaSeleccionada() {
        int fila = tablaEstancias.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una estancia en la tabla.");
            return;
        }

        int filaModelo = tablaEstancias.convertRowIndexToModel(fila);
        int idEstancia = Integer.parseInt(modeloTablaEstancias.getValueAt(filaModelo, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar la estancia seleccionada?",
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
                limpiarFormulario();
                cargarTablaEstancias();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la estancia.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error eliminando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
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
            lblHuespedSeleccionado.setText(seleccionado.toString());
        }
    }

    private void agregarAcompanante() {
        SeleccionHuespedDialog dialog = new SeleccionHuespedDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        Huesped seleccionado = dialog.getHuespedSeleccionado();

        if (seleccionado == null) {
            return;
        }

        if (huespedSeleccionado != null && seleccionado.getId().equals(huespedSeleccionado.getId())) {
            JOptionPane.showMessageDialog(this, "El huésped titular no puede añadirse como acompañante.");
            return;
        }

        for (int i = 0; i < modeloAcompanantes.size(); i++) {
            if (modeloAcompanantes.get(i).getId().equals(seleccionado.getId())) {
                JOptionPane.showMessageDialog(this, "Ese huésped ya está añadido como acompañante.");
                return;
            }
        }

        modeloAcompanantes.addElement(seleccionado);
    }

    private void quitarAcompanante() {
        Huesped seleccionado = listaAcompanantes.getSelectedValue();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un acompañante para quitarlo.");
            return;
        }

        modeloAcompanantes.removeElement(seleccionado);
    }

    private boolean capacidadValida() {
        HabitacionItem habitacionSeleccionada = (HabitacionItem) cbHabitacion.getSelectedItem();

        if (habitacionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una habitación.");
            return false;
        }

        int totalHuespedes = 1 + modeloAcompanantes.size();
        int capacidadHabitacion = habitacionSeleccionada.getCapacidad();

        if (totalHuespedes > capacidadHabitacion) {
            JOptionPane.showMessageDialog(
                    this,
                    "La habitación seleccionada tiene capacidad para " + capacidadHabitacion +
                            " huésped(es), pero has seleccionado " + totalHuespedes + "."
            );
            return false;
        }

        return true;
    }

    private void cargarEstanciaSeleccionada() {
        int fila = tablaEstancias.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una estancia en la tabla.");
            return;
        }

        int filaModelo = tablaEstancias.convertRowIndexToModel(fila);
        int idEstancia = Integer.parseInt(modeloTablaEstancias.getValueAt(filaModelo, 0).toString());

        try {
            com.hotel.model.EstanciaDetalle detalle = estanciaService.obtenerDetalleEstancia(idEstancia);

            if (detalle == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la estancia.");
                return;
            }

            idEstanciaSeleccionada = idEstancia;

            huespedSeleccionado = detalle.getTitular();

            if (huespedSeleccionado != null) {
                lblHuespedSeleccionado.setText(huespedSeleccionado.toString());
            } else {
                lblHuespedSeleccionado.setText("Ningún huésped seleccionado");
            }

            modeloAcompanantes.clear();

            for (Huesped h : detalle.getAcompanantes()) {
                modeloAcompanantes.addElement(h);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            spFechaEntrada.setValue(sdf.parse(detalle.getEstancia().getFechaEntrada()));
            spFechaSalida.setValue(sdf.parse(detalle.getEstancia().getFechaSalida()));

            cbEstado.setSelectedItem(detalle.getEstancia().getEstado());
            txtObservaciones.setText(
                    detalle.getEstancia().getObservaciones() != null
                            ? detalle.getEstancia().getObservaciones()
                            : ""
            );

            buscarHabitacionesDisponibles();

            for (int i = 0; i < cbHabitacion.getItemCount(); i++) {
                HabitacionItem item = cbHabitacion.getItemAt(i);

                if (item.getId() == detalle.getEstancia().getHabitacionId()) {
                    cbHabitacion.setSelectedIndex(i);
                    break;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void actualizarEstancia() {
        if (idEstanciaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Primero carga una estancia para editarla.");
            return;
        }

        if (!fechasValidas()) {
            return;
        }

        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un huésped titular.");
            return;
        }

        HabitacionItem habitacionSeleccionada = (HabitacionItem) cbHabitacion.getSelectedItem();

        if (habitacionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una habitación disponible.");
            return;
        }

        if (!capacidadValida()) {
            return;
        }

        String fechaEntrada = formatearFecha((Date) spFechaEntrada.getValue());
        String fechaSalida = formatearFecha((Date) spFechaSalida.getValue());

        try {
            Estancia estancia = new Estancia();
            estancia.setId(idEstanciaSeleccionada);
            estancia.setFechaEntrada(fechaEntrada);
            estancia.setFechaSalida(fechaSalida);
            estancia.setHabitacionId(habitacionSeleccionada.getId());
            estancia.setEstado((String) cbEstado.getSelectedItem());
            estancia.setObservaciones(txtObservaciones.getText().trim());

            List<Integer> acompanantesIds = new java.util.ArrayList<>();

            for (int i = 0; i < modeloAcompanantes.size(); i++) {
                acompanantesIds.add(modeloAcompanantes.get(i).getId());
            }

            boolean ok = estanciaService.actualizarEstanciaConHuespedes(
                    estancia,
                    huespedSeleccionado.getId(),
                    acompanantesIds
            );

            if (ok) {
                JOptionPane.showMessageDialog(this, "Estancia actualizada correctamente.");
                limpiarFormulario();
                cargarTablaEstancias();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la estancia.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error actualizando estancia: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        cbHabitacion.removeAllItems();
        cbHabitacion.setEnabled(false);

        Date hoy = new Date();
        spFechaEntrada.setValue(hoy);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        spFechaSalida.setValue(cal.getTime());

        cbEstado.setSelectedIndex(0);
        txtObservaciones.setText("");

        huespedSeleccionado = null;
        lblHuespedSeleccionado.setText("Ningún huésped seleccionado");

        modeloAcompanantes.clear();

        idEstanciaSeleccionada = null;
        tablaEstancias.clearSelection();
    }

    private void exportarXML() {
        String fecha = txtFechaXML.getText().trim();

        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una fecha.");
            return;
        }

        try {
            XMLService xmlService = new XMLService();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar XML");
            fileChooser.setSelectedFile(new java.io.File("PV_" + fecha.replace("-", "_") + ".xml"));

            int seleccion = fileChooser.showSaveDialog(this);

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                java.nio.file.Path ruta = fileChooser.getSelectedFile().toPath();

                if (!ruta.toString().endsWith(".xml")) {
                    ruta = java.nio.file.Path.of(ruta.toString() + ".xml");
                }

                xmlService.guardarXMLPVPorFecha(fecha, "480354", ruta);

                JOptionPane.showMessageDialog(this, "XML generado correctamente.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando XML: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private GridBagConstraints crearGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        return gbc;
    }

    private void addCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        panel.add(componente, gbc);

        gbc.gridwidth = 1;
    }
}