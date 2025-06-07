package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.PrecioDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class PrecioDao implements Dao<PrecioDto> {

    @Override
    public List<PrecioDto> listarTodos() {
        List<PrecioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Precio";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrecioDto dto = new PrecioDto();
                dto.id = rs.getInt("id");
                dto.monto = rs.getFloat("monto");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.productoId = rs.getInt("producto_id");
                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public PrecioDto buscar(PrecioDto obj) {
        String sql = "SELECT * FROM Precio WHERE id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.monto = rs.getFloat("monto");
                obj.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                obj.productoId = rs.getInt("producto_id");
            } else {
                obj = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public List<PrecioDto> listarPorProducto(int productoId) {
        List<PrecioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Precio WHERE producto_id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrecioDto dto = new PrecioDto();
                dto.id = rs.getInt("id");
                dto.monto = rs.getFloat("monto");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.productoId = productoId;
                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public PrecioDto actualizar(PrecioDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.id > 0) {
                StringBuilder sql = new StringBuilder("UPDATE Precio SET ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE id = ?");

                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql.toString());

                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "monto":
                            stmt.setFloat(index++, obj.monto);
                            break;
                        case "fecha":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fecha));
                            break;
                        case "producto_id":
                            stmt.setInt(index++, obj.productoId);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.id);
                stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Precio (monto, fecha, producto_id) VALUES (?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setFloat(1, obj.monto);
                stmt.setString(2, CommonUtils.dateToString(obj.fecha));
                stmt.setInt(3, obj.productoId);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.id = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public PrecioDto borrar(PrecioDto obj) {
        String sql = "DELETE FROM Precio WHERE id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}

