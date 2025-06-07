package com.ventas.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.EmpleadoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class EmpleadoDao implements Dao<EmpleadoDto> {

    @Override
    public EmpleadoDto buscar(EmpleadoDto obj) {
        String sql = "SELECT e.id, e.cargo, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email " +
                     "FROM Empleado AS e JOIN Persona AS p ON e.id = p.id WHERE e.id = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.id = rs.getInt("id");
                obj.cargo = rs.getString("cargo");

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
    public EmpleadoDto actualizar(EmpleadoDto obj, List<String> params) {
        try (Connection conn = ConexionSQLite.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Verificamos si la persona ya existe
            String checkSql = "SELECT COUNT(*) FROM Persona WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, obj.id);
                ResultSet rs = checkStmt.executeQuery();
                boolean exists = rs.next() && rs.getInt(1) > 0;

                if (exists) {
                    // UPDATE Persona
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

                    // UPDATE Empleado
                    String updateEmpleado = "UPDATE Empleado SET cargo=? WHERE id=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateEmpleado)) {
                        stmt.setString(1, obj.cargo);
                        stmt.setInt(2, obj.id);
                        stmt.executeUpdate();
                    }

                } else {
                    // INSERT Persona
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

                    // INSERT Empleado
                    String insertEmpleado = "INSERT INTO Empleado (id, cargo, persona_id) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertEmpleado)) {
                        stmt.setInt(1, obj.id); // igual al id de Persona
                        stmt.setString(2, obj.cargo);
                        stmt.setInt(3, obj.id); // persona_id también igual
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
    public EmpleadoDto borrar(EmpleadoDto obj) {
        try (Connection conn = ConexionSQLite.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Borrar Empleado
            String deleteEmpleado = "DELETE FROM Empleado WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteEmpleado)) {
                stmt.setInt(1, obj.id);
                stmt.executeUpdate();
            }

            // Borrar Persona
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
    public List<EmpleadoDto> listarTodos() {
        List<EmpleadoDto> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.cargo, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email " +
                     "FROM Empleado AS e JOIN Persona AS p ON e.id = p.id";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EmpleadoDto obj = new EmpleadoDto();
                obj.id = rs.getInt("id");
                obj.cargo = rs.getString("cargo");

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
