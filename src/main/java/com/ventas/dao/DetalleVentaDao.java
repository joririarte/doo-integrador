package com.ventas.dao;

import com.ventas.dto.DetalleVentaDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDao implements Dao<DetalleVentaDto> {

    @Override
    public List<DetalleVentaDto> listarTodos() {
        List<DetalleVentaDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM DetalleVenta";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DetalleVentaDto dto = new DetalleVentaDto();
                dto.ventaId = rs.getInt("venta_id");
                dto.detalleVentaId = rs.getInt("id");
                dto.nombre = rs.getString("nombre");
                dto.cantidad = rs.getInt("cantidad");
                dto.precioVenta = rs.getFloat("precioVenta");
                dto.productoId = rs.getInt("producto_id");
                lista.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public DetalleVentaDto buscar(DetalleVentaDto obj) {
        String sql = "SELECT * FROM DetalleVenta WHERE venta_id = ? AND id = ?";
        DetalleVentaDto dto = null;

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.ventaId);
            stmt.setInt(2, obj.detalleVentaId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dto = new DetalleVentaDto();
                dto.ventaId = rs.getInt("venta_id");
                dto.detalleVentaId = rs.getInt("id");
                dto.nombre = rs.getString("nombre");
                dto.cantidad = rs.getInt("cantidad");
                dto.productoId = rs.getInt("producto_id");
                dto.precioVenta = rs.getFloat("precioVenta");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dto;
    }
    
    public List<DetalleVentaDto> obtenerPorVenta(int ventaId) {
        DetalleVentaDto dto = null;
        List<DetalleVentaDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM DetalleVenta WHERE venta_id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, ventaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dto = new DetalleVentaDto();
                dto.ventaId = rs.getInt("venta_id");
                dto.detalleVentaId = rs.getInt("id");
                dto.nombre = rs.getString("nombre");
                dto.cantidad = rs.getInt("cantidad");
                dto.productoId = rs.getInt("producto_id");
                dto.precioVenta = rs.getFloat("precioVenta");

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public DetalleVentaDto actualizar(DetalleVentaDto obj, List<String> campos) {
        try {
            Connection conn = ConexionSQLite.getInstance().getConnection();

            if (campos == null || campos.isEmpty()) {
                // Insertar
                String sql = "INSERT INTO DetalleVenta (venta_id, id, nombre, cantidad, precioVenta, producto_id) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, obj.ventaId);
                    stmt.setInt(2, obj.detalleVentaId);
                    stmt.setString(3, obj.nombre);
                    stmt.setInt(4, obj.cantidad);
                    stmt.setFloat(5, obj.precioVenta);
                    stmt.setInt(6, obj.productoId);
                    stmt.executeUpdate();
                }
            } else {
                // Actualizar dinámicamente
                StringBuilder sql = new StringBuilder("UPDATE DetalleVenta SET ");
                for (int i = 0; i < campos.size(); i++) {
                    sql.append(campos.get(i)).append(" = ?");
                    if (i < campos.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE venta_id = ? AND id = ?");

                try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                    for (int i = 0; i < campos.size(); i++) {
                        switch (campos.get(i)) {
                            case "nombre" -> stmt.setString(i + 1, obj.nombre);
                            case "cantidad" -> stmt.setInt(i + 1, obj.cantidad);
                            case "precioVenta" -> stmt.setFloat(i + 1, obj.precioVenta);
                        }
                    }
                    stmt.setInt(campos.size() + 1, obj.ventaId);
                    stmt.setInt(campos.size() + 2, obj.detalleVentaId);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public DetalleVentaDto borrar(DetalleVentaDto obj) {
        String sql = "DELETE FROM DetalleVenta WHERE venta_id = ? AND id = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.ventaId);
            stmt.setInt(2, obj.detalleVentaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
