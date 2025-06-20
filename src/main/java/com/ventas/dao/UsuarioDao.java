package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.UsuarioDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class UsuarioDao implements Dao<UsuarioDto> {

    @Override
    public List<UsuarioDto> listarTodos() {
        List<UsuarioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioDto usuario = new UsuarioDto();
                usuario.empleadoId = rs.getInt("empleadoId");
                usuario.username = rs.getString("username");
                usuario.password = rs.getString("password");
                usuario.ultimoAcceso = CommonUtils.stringToDateTime(rs.getString("ultimoAcceso"));
                usuario.empleado = new EmpleadoDao().buscar(usuario.empleadoId);
                
                lista.add(usuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public UsuarioDto buscar(int id) {
        String sql = "SELECT * FROM Usuario WHERE empleadoId = ?";
        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioDto obj = new UsuarioDto();
                obj.empleadoId = rs.getInt("empleadoId");
                obj.username = rs.getString("username");
                obj.password = rs.getString("password");
                obj.ultimoAcceso = CommonUtils.stringToDateTime(rs.getString("ultimoAcceso"));

                obj.empleado = new EmpleadoDao().buscar(obj.empleadoId);
                return obj;
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public UsuarioDto actualizar(UsuarioDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty()) {
                StringBuilder sql = new StringBuilder("UPDATE Usuario SET ");
            for (int i = 0; i < params.size(); i++) {
                sql.append(params.get(i)).append(" = ?");
                if (i < params.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(" WHERE username = ?");

            String query = sql.toString();
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(query);

            int index = 1;
            for (String param : params) {
                switch (param) {
                    case "ultimoAcceso":
                        String ultimoAcceso = CommonUtils.dateTimeToString(obj.ultimoAcceso);
                        stmt.setString(index++, ultimoAcceso);
                        break;
                    case "password":
                        stmt.setString(index++, obj.password);
                        break;
                    default:
                        throw new IllegalArgumentException("Campo no soportado: " + param);
                }
            }

            stmt.setString(index, obj.username);
            stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Usuario (empleadoId, username, password, ultimoAcceso) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, obj.empleadoId);
                stmt.setString(2, obj.username);
                stmt.setString(3, obj.password);
                stmt.setString(4, CommonUtils.dateTimeToString(obj.ultimoAcceso));
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.empleadoId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }


    @Override
    public UsuarioDto borrar(UsuarioDto obj) {
        String sql = "DELETE FROM Usuario WHERE empleadoId = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.empleadoId);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public List<UsuarioDto> buscar(UsuarioDto obj, List<String> params) {
        List<UsuarioDto> lista = new ArrayList<>();
        try{
            if (params != null && !params.isEmpty()) {
                StringBuilder sql = new StringBuilder("SELECT * FROM Usuario WHERE ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(" AND ");
                    }
                }

                String query = sql.toString();

                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(query);
                
                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "password":
                            stmt.setString(index++, obj.password);
                            break;
                        case "username":
                            stmt.setString(index++, obj.username);
                            break;  
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }
                
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    obj.empleadoId = rs.getInt("empleadoId");
                    obj.username = rs.getString("username");
                    obj.password = rs.getString("password");
                    obj.ultimoAcceso = CommonUtils.stringToDateTime(rs.getString("ultimoAcceso"));

                    obj.empleado = new EmpleadoDao().buscar(obj.empleadoId);

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
