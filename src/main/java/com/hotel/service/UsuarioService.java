package com.hotel.service;

import com.hotel.dao.UsuarioDAO;
import com.hotel.model.Usuario;

import java.sql.SQLException;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String username, String password) throws SQLException {
        if (username == null || username.isBlank()) {
            return null;
        }

        if (password == null || password.isBlank()) {
            return null;
        }

        return usuarioDAO.login(username.trim(), password.trim());
    }
}