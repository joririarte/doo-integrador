package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.EmpleadoDto;
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
                usuario.ultimoAcceso = CommonUtils.stringToDate(rs.getString("ultimoAcceso"));
                
                EmpleadoDto emp = new EmpleadoDto();
                emp.personaId = usuario.empleadoId;
                usuario.empleado = new EmpleadoDao().buscar(emp);
                
                lista.add(usuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public UsuarioDto buscar(UsuarioDto obj) {
        String sql = "SELECT * FROM Usuario WHERE username = ? AND password = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);

            stmt.setString(1, obj.username);
            stmt.setString(2, obj.password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.empleadoId = rs.getInt("empleadoId");
                obj.username = rs.getString("username");
                obj.password = rs.getString("password");
                obj.ultimoAcceso = CommonUtils.stringToDate(rs.getString("ultimoAcceso"));

                EmpleadoDto emp = new EmpleadoDto();
                emp.personaId = obj.empleadoId;
                obj.empleado = new EmpleadoDao().buscar(emp);
            }
            else
                obj = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public UsuarioDto actualizar(UsuarioDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.empleadoId > 0) {
                StringBuilder sql = new StringBuilder("UPDATE Usuario SET ");
            for (int i = 0; i < params.size(); i++) {
                sql.append(params.get(i)).append(" = ?");
                if (i < params.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(" WHERE empleadoId = ?");

            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql.toString());

            int index = 1;
            for (String param : params) {
                switch (param) {
                    case "ultimoAcceso":
                        stmt.setString(index++, CommonUtils.dateToString(obj.ultimoAcceso));
                        break;
                    case "password":
                        stmt.setString(index++, obj.password);
                        break;
                    default:
                        throw new IllegalArgumentException("Campo no soportado: " + param);
                }
            }

            stmt.setInt(index, obj.empleadoId);
            stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Usuario (empleadoId, username, password) VALUES (?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, obj.empleadoId);
                stmt.setString(2, obj.username);
                stmt.setString(3, obj.password);
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.empleadoId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }

        return obj;
    }

    @Override
    public List<UsuarioDto> buscar(UsuarioDto obj, List<String> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscar'");
    }
}
