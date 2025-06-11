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
                dto.productoId = rs.getInt("productoId");
                dto.precioId = rs.getInt("precioId");
                dto.monto = rs.getFloat("monto");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public PrecioDto buscar(int id) {
        return null;
    }

    public List<PrecioDto> listarPorProducto(int productoId) {
        List<PrecioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Precio WHERE productoId = ? ORDER BY monto DESC";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrecioDto dto = new PrecioDto();
                dto.productoId = rs.getInt("productoId");
                dto.precioId = rs.getInt("precioId");
                dto.monto = rs.getFloat("monto");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
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
            if (params != null && !params.isEmpty() && obj.productoId > 0 && obj.precioId > 0) {
                StringBuilder sql = new StringBuilder("UPDATE Precio SET ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE productoId = ? AND precioId = ?");

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
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.productoId);
                stmt.setInt(index++, obj.precioId);
                stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Precio (productoId, precioId, monto, fecha) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, obj.productoId);
                stmt.setInt(2, obj.precioId);
                stmt.setFloat(3, obj.monto);
                stmt.setString(4, CommonUtils.dateToString(obj.fecha));
                
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public PrecioDto borrar(PrecioDto obj) {
        String sql = "DELETE FROM Precio WHERE productoId = ? AND precioId = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.productoId);
                stmt.setInt(2, obj.precioId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public List<PrecioDto> buscar(PrecioDto obj, List<String> params) {
        return null;
    }
}

