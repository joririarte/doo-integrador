package com.ventas.dao;

import com.ventas.dto.VentaDto;
import com.ventas.dto.DetalleVentaDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDao implements Dao<VentaDto> {




    @Override
    public List<VentaDto> listarTodos() {
        List<VentaDto> ventas = new ArrayList<>();
        String sql = "SELECT * FROM Venta";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                VentaDto dto = new VentaDto();
                dto.id = rs.getInt("id");
                dto.empleadoId = rs.getInt("vendedor_id");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.estado = rs.getString("estado");
                dto.montoPagado = rs.getFloat("montoPagado");
                dto.medioPagoId = rs.getInt("medio_pago_id");
                dto.clienteId = rs.getInt("cliente_id");

                // Cargar detalle de venta
                dto.detalleVenta = new DetalleVentaDao().obtenerPorVenta(dto.id);

                ventas.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ventas;
    }

    @Override
    public VentaDto buscar(VentaDto obj) {
        VentaDto dto = null;
        String sql = "SELECT * FROM Venta WHERE id = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new VentaDto();
                dto.id = rs.getInt("id");
                dto.empleadoId = rs.getInt("vendedor_id");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.estado = rs.getString("estado");
                dto.montoPagado = rs.getFloat("montoPagado");
                dto.medioPagoId = rs.getInt("medio_pago_id");
                dto.clienteId = rs.getInt("cliente_id");

                // Detalles
                dto.detalleVenta = new DetalleVentaDao().obtenerPorVenta(dto.id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public VentaDto actualizar(VentaDto obj, List<String> campos) {
        try {
            Connection conn = ConexionSQLite.getInstance().getConnection();

            if (campos == null || campos.isEmpty()) {
                // Insertar
                String sql = "INSERT INTO Venta (vendedor_id, fecha, estado, montoPagado, medio_pago_id, cliente_id) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, obj.empleadoId);
                    stmt.setString(2, CommonUtils.dateToString(obj.fecha));
                    stmt.setString(3, obj.estado);
                    stmt.setFloat(4, obj.montoPagado);
                    stmt.setInt(5, obj.medioPagoId);
                    stmt.setInt(6, obj.clienteId);
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        obj.id = rs.getInt(1);
                    }
                }

                // Guardar detalles
                int detalleId = 1;
                for (DetalleVentaDto detalle : obj.detalleVenta) {
                    detalle.ventaId = obj.id;
                    detalle.detalleVentaId = detalleId++;
                    new DetalleVentaDao().actualizar(detalle, null);
                }

            } else {
                // Actualizar dinámicamente
                StringBuilder sql = new StringBuilder("UPDATE Venta SET ");
                for (int i = 0; i < campos.size(); i++) {
                    sql.append(campos.get(i)).append(" = ?");
                    if (i < campos.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE id = ?");

                try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                    for (int i = 0; i < campos.size(); i++) {
                        switch (campos.get(i)) {
                            case "vendedor_id" -> stmt.setInt(i + 1, obj.empleadoId);
                            case "fecha" -> stmt.setString(i + 1, CommonUtils.dateToString(obj.fecha));
                            case "estado" -> stmt.setString(i + 1, obj.estado);
                            case "montoPagado" -> stmt.setFloat(i + 1, obj.montoPagado);
                            case "medio_pago_id" -> stmt.setInt(i + 1, obj.medioPagoId);
                            case "cliente_id" -> stmt.setInt(i + 1, obj.clienteId);
                        }
                    }
                    stmt.setInt(campos.size() + 1, obj.id);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public VentaDto borrar(VentaDto obj) {
        String sql = "DELETE FROM Venta WHERE id = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
