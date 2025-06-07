package com.ventas.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.ClienteDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class ClienteDao implements Dao<ClienteDto> {

    @Override
    public ClienteDto buscar(ClienteDto obj) {
        String sql = "SELECT c.id, c.nro_cliente, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email " +
                     "FROM Cliente AS c JOIN Persona AS p ON c.id = p.id WHERE c.id = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.id = rs.getInt("id");
                obj.nroCliente = rs.getString("nro_cliente");

                obj.nombreApellido = rs.getString("nombreApellido");
                obj.tipoDocumento = rs.getString("tipoDocumento");
                obj.nroDocumento = rs.getString("nroDocumento");
                obj.CUIT = rs.getString("CUIT");
                obj.condicionAfip = rs.getString("condicionAfip");
                obj.genero = rs.getString("genero");
                obj.fechaNacimiento = CommonUtils.stringToDate(rs.getString("fechaNacimiento"));
                obj.domicilio = rs.getString("domicilio");
                obj.email = rs.getString("email");
            } else {
                obj = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }

        return obj;
    }

    @Override
    public ClienteDto actualizar(ClienteDto obj, List<String> params) {
        try (Connection conn = ConexionSQLite.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Verificamos si ya existe la persona
            String checkSql = "SELECT COUNT(*) FROM Persona WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, obj.id);
                ResultSet rs = checkStmt.executeQuery();
                boolean exists = rs.next() && rs.getInt(1) > 0;

                if (exists) {
                    String updatePersona = "UPDATE Persona SET nombreApellido=?, tipoDocumento=?, nroDocumento=?, CUIT=?, condicionAfip=?, genero=?, fechaNacimiento=?, domicilio=?, email=? WHERE id=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updatePersona)) {
                        stmt.setString(1, obj.nombreApellido);
                        stmt.setString(2, obj.tipoDocumento);
                        stmt.setString(3, obj.nroDocumento);
                        stmt.setString(4, obj.CUIT);
                        stmt.setString(5, obj.condicionAfip);
                        stmt.setString(6, obj.genero);
                        stmt.setString(7, CommonUtils.dateToString(obj.fechaNacimiento));
                        stmt.setString(8, obj.domicilio);
                        stmt.setString(9, obj.email);
                        stmt.setInt(10, obj.id);
                        stmt.executeUpdate();
                    }

                    String updateCliente = "UPDATE Cliente SET nro_cliente=? WHERE id=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateCliente)) {
                        stmt.setString(1, obj.nroCliente);
                        stmt.setInt(2, obj.id);
                        stmt.executeUpdate();
                    }
                } else {
                    String insertPersona = "INSERT INTO Persona (nombreApellido, tipoDocumento, nroDocumento, CUIT, condicionAfip, genero, fechaNacimiento, domicilio, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertPersona, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, obj.nombreApellido);
                        stmt.setString(2, obj.tipoDocumento);
                        stmt.setString(3, obj.nroDocumento);
                        stmt.setString(4, obj.CUIT);
                        stmt.setString(5, obj.condicionAfip);
                        stmt.setString(6, obj.genero);
                        stmt.setString(7, CommonUtils.dateToString(obj.fechaNacimiento));
                        stmt.setString(8, obj.domicilio);
                        stmt.setString(9, obj.email);
                        stmt.executeUpdate();

                        ResultSet keys = stmt.getGeneratedKeys();
                        if (keys.next()) {
                            obj.id = keys.getInt(1);
                        }
                    }

                    String insertCliente = "INSERT INTO Cliente (id, nro_cliente, persona_id) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertCliente)) {
                        stmt.setInt(1, obj.id); // mismo ID que Persona
                        stmt.setString(2, obj.nroCliente);
                        stmt.setInt(3, obj.id); // persona_id igual al id
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public ClienteDto borrar(ClienteDto obj) {
        try (Connection conn = ConexionSQLite.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            String deleteCliente = "DELETE FROM Cliente WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteCliente)) {
                stmt.setInt(1, obj.id);
                stmt.executeUpdate();
            }

            String deletePersona = "DELETE FROM Persona WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(deletePersona)) {
                stmt.setInt(1, obj.id);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public List<ClienteDto> listarTodos() {
        List<ClienteDto> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.nro_cliente, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email " +
                     "FROM Cliente AS c JOIN Persona AS p ON c.id = p.id";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ClienteDto obj = new ClienteDto();
                obj.id = rs.getInt("id");
                obj.nroCliente = rs.getString("nro_cliente");

                obj.nombreApellido = rs.getString("nombreApellido");
                obj.tipoDocumento = rs.getString("tipoDocumento");
                obj.nroDocumento = rs.getString("nroDocumento");
                obj.CUIT = rs.getString("CUIT");
                obj.condicionAfip = rs.getString("condicionAfip");
                obj.genero = rs.getString("genero");
                obj.fechaNacimiento = CommonUtils.stringToDate(rs.getString("fechaNacimiento"));
                obj.domicilio = rs.getString("domicilio");
                obj.email = rs.getString("email");

                lista.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
