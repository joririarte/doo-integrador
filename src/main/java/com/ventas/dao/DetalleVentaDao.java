package com.ventas.dao;

import com.ventas.dto.DetalleVentaDto;
import com.ventas.dto.ProductoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                dto.ventaId = rs.getInt("ventaId");
                dto.detalleVentaId = rs.getInt("detalleVentaId");
                dto.nombre = rs.getString("nombre");
                dto.cantidad = rs.getInt("cantidad");
                dto.precioVenta = rs.getFloat("precioVenta");
                dto.productoId = rs.getInt("productoId");
                dto.producto = new ProductoDao().buscar(dto.productoId);
                lista.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public DetalleVentaDto buscar(int id) {
        return null;
    }
    
    public List<DetalleVentaDto> listarPorVenta(int ventaId) {
        DetalleVentaDto dto = null;
        List<DetalleVentaDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM DetalleVenta WHERE ventaId = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, ventaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dto = new DetalleVentaDto();
                dto.ventaId = rs.getInt("ventaId");
                dto.detalleVentaId = rs.getInt("detalleVentaId");
                dto.nombre = rs.getString("nombre");
                dto.cantidad = rs.getInt("cantidad");
                dto.precioVenta = rs.getFloat("precioVenta");
                dto.productoId = rs.getInt("productoId");
                dto.producto = new ProductoDao().buscar(dto.productoId);

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public DetalleVentaDto actualizar(DetalleVentaDto obj, List<String> campos) {
        try {
            Connection conn = ConexionSQLite.getInstance().getConnection();
            ProductoDto productoDto = new ProductoDao().buscar(obj.producto, Arrays.asList("codigoBarras")).getFirst();
            if (campos == null || campos.isEmpty()) {
                // Insertar
                String sql = "INSERT INTO DetalleVenta (ventaId, detalleVentaId, nombre, cantidad, precioVenta, productoId) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, obj.ventaId);
                    stmt.setInt(2, obj.detalleVentaId);
                    stmt.setString(3, obj.nombre);
                    stmt.setInt(4, obj.cantidad);
                    stmt.setFloat(5, obj.precioVenta);
                    stmt.setInt(6, productoDto.productoId);
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
                sql.append(" WHERE ventaId = ? AND detalleVentaId = ?");

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
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public DetalleVentaDto borrar(DetalleVentaDto obj) {
        String sql = "DELETE FROM DetalleVenta WHERE ventaId = ? AND detalleVentaId = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.ventaId);
            stmt.setInt(2, obj.detalleVentaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public List<DetalleVentaDto> buscar(DetalleVentaDto obj, List<String> params) {
        return null;
    }
}
