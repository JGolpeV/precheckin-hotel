package com.hotel.view;

import com.hotel.model.Huesped;
import com.hotel.service.HuespedService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class SeleccionHuespedDialog extends JDialog {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;

    private Huesped huespedSeleccionado;

    private final HuespedService huespedService = new HuespedService();

    public SeleccionHuespedDialog(Window owner) {
        super(owner, "Seleccionar huésped", ModalityType.APPLICATION_MODAL);

        setSize(700, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(20);
        panelBusqueda.add(txtBuscar);

        add(panelBusqueda, BorderLayout.NORTH);

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
        ocultarColumnaId();
        tabla.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });

        btnSeleccionar.addActionListener(e -> seleccionarHuesped());
        btnCancelar.addActionListener(e -> dispose());

        cargarTabla();
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
            JOptionPane.showMessageDialog(this,
                    "Error cargando huéspedes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
    }

    private void seleccionarHuesped() {
        int filaVista = tabla.getSelectedRow();

        if (filaVista == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un huésped.");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(filaVista);

        Huesped h = new Huesped();
        h.setId((Integer) modeloTabla.getValueAt(filaModelo, 0));
        h.setNombre((String) modeloTabla.getValueAt(filaModelo, 1));
        h.setApellido1((String) modeloTabla.getValueAt(filaModelo, 2));
        h.setApellido2((String) modeloTabla.getValueAt(filaModelo, 3));
        h.setNacionalidad((String) modeloTabla.getValueAt(filaModelo, 4));

        huespedSeleccionado = h;
        dispose();
    }

    public Huesped getHuespedSeleccionado() {
        return huespedSeleccionado;
    }

    private void ocultarColumnaId() {
        TableColumn columnaId = tabla.getColumnModel().getColumn(0);
        columnaId.setMinWidth(0);
        columnaId.setMaxWidth(0);
        columnaId.setPreferredWidth(0);
    }
}