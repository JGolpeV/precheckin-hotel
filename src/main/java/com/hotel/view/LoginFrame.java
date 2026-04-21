package com.hotel.view;

import com.hotel.model.Usuario;
import com.hotel.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private final UsuarioService usuarioService = new UsuarioService();

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panelFormulario.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Entrar");

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnLogin);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> hacerLogin());
        getRootPane().setDefaultButton(btnLogin);
    }

    private void hacerLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario usuario = usuarioService.login(username, password);

            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
                return;
            }

            MainFrame mainFrame = new MainFrame(usuario);
            mainFrame.setVisible(true);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
