package com.ventas.dao;

import com.ventas.dto.VentaDto;
import com.ventas.dto.ClienteDto;
import com.ventas.dto.DetalleVentaDto;
import com.ventas.dto.EmpleadoDto;
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
                dto.ventaId = rs.getInt("ventaId");
                dto.vendedorId = rs.getInt("vendedorId");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.estado = rs.getString("estado");
                dto.montoPagado = rs.getFloat("montoPagado");
                dto.medioPagoId = rs.getInt("medioPagoId");
                dto.clienteId = rs.getInt("clienteId");

                // Cargar detalle de venta
                dto.detalleVenta = new DetalleVentaDao().obtenerPorVenta(dto.ventaId);

                ClienteDto clienteDto = new ClienteDto();
                clienteDto.personaId = dto.clienteId;
                dto.cliente = new ClienteDao().buscar(clienteDto);

                EmpleadoDto vendedorDto = new EmpleadoDto();
                vendedorDto.personaId = dto.vendedorId;
                dto.vendedor = new EmpleadoDao().buscar(vendedorDto);

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
        String sql = "SELECT * FROM Venta WHERE ventaId = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.ventaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new VentaDto();
                dto.ventaId = rs.getInt("ventaId");
                dto.vendedorId = rs.getInt("vendedorId");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.estado = rs.getString("estado");
                dto.montoPagado = rs.getFloat("montoPagado");
                dto.medioPagoId = rs.getInt("medioPagoId");
                dto.clienteId = rs.getInt("clienteId");

                // Detalles
                dto.detalleVenta = new DetalleVentaDao().obtenerPorVenta(dto.ventaId);

                ClienteDto clienteDto = new ClienteDto();
                clienteDto.personaId = dto.clienteId;
                dto.cliente = new ClienteDao().buscar(clienteDto);

                EmpleadoDto vendedorDto = new EmpleadoDto();
                vendedorDto.personaId = dto.vendedorId;
                dto.vendedor = new EmpleadoDao().buscar(vendedorDto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public List<VentaDto> buscar(VentaDto obj, List<String> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VentaDto actualizar(VentaDto obj, List<String> campos) {
        try {
            Connection conn = ConexionSQLite.getInstance().getConnection();

            if (campos == null || campos.isEmpty()) {
                // Insertar
                String sql = "INSERT INTO Venta (vendedorId, fecha, estado, montoPagado, medioPagoId, clienteId) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, obj.vendedorId);
                    stmt.setString(2, CommonUtils.dateToString(obj.fecha));
                    stmt.setString(3, obj.estado);
                    stmt.setFloat(4, obj.montoPagado);
                    stmt.setInt(5, obj.medioPagoId);
                    stmt.setInt(6, obj.clienteId);
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        obj.ventaId = rs.getInt(1);
                    }
                }

                // Guardar detalles
                int detalleId = 1;
                for (DetalleVentaDto detalle : obj.detalleVenta) {
                    detalle.ventaId = obj.ventaId;
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
                sql.append(" WHERE ventaId = ?");

                try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                    for (int i = 0; i < campos.size(); i++) {
                        switch (campos.get(i)) {
                            case "vendedorId" -> stmt.setInt(i + 1, obj.vendedorId);
                            case "fecha" -> stmt.setString(i + 1, CommonUtils.dateToString(obj.fecha));
                            case "estado" -> stmt.setString(i + 1, obj.estado);
                            case "montoPagado" -> stmt.setFloat(i + 1, obj.montoPagado);
                            case "medioPagoId" -> stmt.setInt(i + 1, obj.medioPagoId);
                            case "clienteId" -> stmt.setInt(i + 1, obj.clienteId);
                        }
                    }
                    stmt.setInt(campos.size() + 1, obj.ventaId);
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
        String sql = "DELETE FROM Venta WHERE ventaId = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, obj.ventaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
