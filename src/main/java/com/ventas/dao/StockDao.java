package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.StockDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class StockDao implements Dao<StockDto> {

    @Override
    public List<StockDto> listarTodos() {
        List<StockDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Stock";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StockDto dto = new StockDto();
                dto.productoId = rs.getInt("productoId");
                dto.stockId = rs.getInt("stockId");
                dto.cantidad = rs.getFloat("cantidad");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<StockDto> listarPorProducto(int productoId) {
        List<StockDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Stock WHERE productoId = ? AND stockId = ? ORDER BY monto DESC";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StockDto dto = new StockDto();
                dto.productoId = rs.getInt("productoId");
                dto.stockId = rs.getInt("stockId");
                dto.cantidad = rs.getFloat("cantidad");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public StockDto buscar(int id) {
        return null;
    }

    @Override
    public StockDto actualizar(StockDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.productoId > 0 && obj.stockId > 0) {
                StringBuilder sql = new StringBuilder("UPDATE Stock SET ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE productoId = ? AND stockId = ?");

                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql.toString());

                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "cantidad":
                            stmt.setFloat(index++, obj.cantidad);
                            break;
                        case "fecha":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fecha));
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.productoId);
                stmt.setInt(index++, obj.stockId);
                stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Stock (productoId, stockId, cantidad, fecha) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, obj.productoId);
                stmt.setInt(2, obj.stockId);
                stmt.setFloat(3, obj.cantidad);
                stmt.setString(4, CommonUtils.dateToString(obj.fecha));
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public StockDto borrar(StockDto obj) {
        String sql = "DELETE FROM Stock WHERE productoId = ? AND stockId = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.productoId);
            stmt.setInt(2, obj.stockId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public List<StockDto> buscar(StockDto obj, List<String> params) {
        return null;
    }
}
