package com.ventas.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.ClienteDto;
import com.ventas.dto.EmpleadoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class ClienteDao implements Dao<ClienteDto> {

    @Override
    public ClienteDto buscar(int id) {
        String sql = "SELECT c.personaId, c.nroCliente, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email " +
                     "FROM Cliente AS c JOIN Persona AS p ON c.personaId = p.personaId WHERE c.personaId = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ClienteDto obj = new ClienteDto();
                obj.personaId = rs.getInt("personaId");
                obj.nroCliente = rs.getString("nroCliente");
                obj.nombreApellido = rs.getString("nombreApellido");
                obj.tipoDocumento = rs.getString("tipoDocumento");
                obj.nroDocumento = rs.getString("nroDocumento");
                obj.CUIT = rs.getString("CUIT");
                obj.condicionAfip = rs.getString("condicionAfip");
                obj.genero = rs.getString("genero");
                obj.fechaNacimiento = CommonUtils.stringToDate(rs.getString("fechaNacimiento"));
                obj.domicilio = rs.getString("domicilio");
                obj.email = rs.getString("email");
                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public ClienteDto actualizar(ClienteDto obj, List<String> params) {
        try (Connection conn = ConexionSQLite.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Verificamos si ya existe la persona
            String checkSql = "SELECT COUNT(*) FROM Persona WHERE personaId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, obj.personaId);
                ResultSet rs = checkStmt.executeQuery();
                boolean exists = rs.next() && rs.getInt(1) > 0;

                if (exists) {
                    String updatePersona = "UPDATE Persona SET nombreApellido=?, tipoDocumento=?, nroDocumento=?, CUIT=?, condicionAfip=?, genero=?, fechaNacimiento=?, domicilio=?, email=? WHERE personaId=?";
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
                        stmt.setInt(10, obj.personaId);
                        stmt.executeUpdate();
                    }

                    String updateCliente = "UPDATE Cliente SET nro_cliente=? WHERE personaId=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateCliente)) {
                        stmt.setString(1, obj.nroCliente);
                        stmt.setInt(2, obj.personaId);
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
                            obj.personaId = keys.getInt(1);
                        }
                    }

                    String insertCliente = "INSERT INTO Cliente (personaId, nroCliente) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertCliente)) {
                        stmt.setInt(1, obj.personaId); // mismo ID que Persona
                        stmt.setString(2, obj.nroCliente);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public ClienteDto borrar(ClienteDto obj) {
        try (Connection conn = ConexionSQLite.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            String deleteCliente = "DELETE FROM Cliente WHERE personaId=?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteCliente)) {
                stmt.setInt(1, obj.personaId);
                stmt.executeUpdate();
            }

            String deletePersona = "DELETE FROM Persona WHERE personaId=?";
            try (PreparedStatement stmt = conn.prepareStatement(deletePersona)) {
                stmt.setInt(1, obj.personaId);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public List<ClienteDto> listarTodos() {
        List<ClienteDto> lista = new ArrayList<>();
        String sql = "SELECT c.personaId, c.nroCliente, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email " +
                     "FROM Cliente AS c JOIN Persona AS p ON c.personaId = p.personaId";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ClienteDto obj = new ClienteDto();
                obj.personaId = rs.getInt("personaId");
                obj.nroCliente = rs.getString("nroCliente");

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
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public List<ClienteDto> buscar(ClienteDto obj, List<String> params) {
        List<ClienteDto> lista = new ArrayList<>();
        try{
            if (params != null && !params.isEmpty()) {
                String query = "SELECT c.personaId, c.nroCliente, p.nombreApellido, p.tipoDocumento, p.nroDocumento, p.CUIT, p.condicionAfip, p.genero, p.fechaNacimiento, p.domicilio, p.email FROM Cliente AS c JOIN Persona AS p ON c.personaId = p.personaId WHERE ";
                StringBuilder sql = new StringBuilder(query);
                for (int i = 0; i < params.size(); i++) {
                    if(params.get(i).equals("fechaNacimiento")){
                        sql.append(params.get(i)).append(" = ?");
                    }
                    else{
                        sql.append(params.get(i)).append(" LIKE ?");
                    }
                    if (i < params.size() - 1) {
                        sql.append(" AND ");
                    }
                }

                query = sql.toString();

                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(query);
                
                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "nombreApellido":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.nombreApellido));
                            break;
                        case "tipoDocumento":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.tipoDocumento));
                            break;
                        case "nroDocumento":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.nroDocumento));
                            break;
                        case "CUIT":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.CUIT));
                            break;
                        case "condicionAfip":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.condicionAfip));
                            break;
                        case "genero":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.genero));
                            break;
                        case "fechaNacimiento":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaNacimiento));
                            break;
                        case "domicilio":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.domicilio));
                            break;
                        case "email":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.email));
                            break;
                        case "nroCliente":
                            stmt.setString(index++, obj.nroCliente);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }
                
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    obj.personaId = rs.getInt("personaId");
                    obj.nroCliente = rs.getString("nroCliente");

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
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }
}
