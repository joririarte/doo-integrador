package com.ventas.dao;

import com.ventas.dto.VentaDto;
import com.ventas.dto.ClienteDto;
import com.ventas.dto.DescuentoRecargoDto;
import com.ventas.dto.DetalleVentaDto;
import com.ventas.dto.EmpleadoDto;
import com.ventas.dto.MedioPagoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                dto.codigoVenta = rs.getString("codigoVenta");
                dto.codigoDescuentoRecargo = rs.getString("codigoDescuentoRecargo");

                // Cargar detalle de venta
                dto.detalleVenta = new DetalleVentaDao().listarPorVenta(dto.ventaId);
                dto.cliente = new ClienteDao().buscar(dto.clienteId);
                dto.vendedor = new EmpleadoDao().buscar(dto.vendedorId);
                dto.medioPago = new MedioPagoDao().buscar(dto.medioPagoId);
                List<DescuentoRecargoDto> listado = new DescuentoRecargoDao().listarPorMedioPago(dto.medioPagoId);
                if(listado != null && !listado.isEmpty()){
                    dto.descuentoRecargo = listado.stream()
                                                    .filter(
                                                        dr -> dr.codigoDescuentoRecargo.equals(dto.codigoDescuentoRecargo)
                                                    )
                                                    .findFirst().get();
                }

                ventas.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return ventas;
    }

    @Override
    public VentaDto buscar(int id) {
        String sql = "SELECT * FROM Venta WHERE ventaId = ?";

        try (PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                final VentaDto dto = new VentaDto();
                dto.ventaId = rs.getInt("ventaId");
                dto.vendedorId = rs.getInt("vendedorId");
                dto.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                dto.estado = rs.getString("estado");
                dto.montoPagado = rs.getFloat("montoPagado");
                dto.medioPagoId = rs.getInt("medioPagoId");
                dto.clienteId = rs.getInt("clienteId");
                dto.codigoVenta = rs.getString("codigoVenta");
                dto.codigoDescuentoRecargo = rs.getString("codigoDescuentoRecargo");

                // Detalles
                dto.detalleVenta = new DetalleVentaDao().listarPorVenta(dto.ventaId);
                dto.cliente = new ClienteDao().buscar(dto.clienteId);
                dto.vendedor = new EmpleadoDao().buscar(dto.vendedorId);
                dto.medioPago = new MedioPagoDao().buscar(dto.medioPagoId);
                
                List<DescuentoRecargoDto> listado = new DescuentoRecargoDao().listarPorMedioPago(dto.medioPagoId);
                dto.descuentoRecargo = listado.stream()
                                                .filter(
                                                    dr -> dr.codigoDescuentoRecargo.equals(dto.codigoDescuentoRecargo)
                                                 )
                                                .findFirst().get();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<VentaDto> buscar(VentaDto obj, List<String> params) {
        List<VentaDto> lista = new ArrayList<>();
        try{
            if (params != null && !params.isEmpty()) {
                StringBuilder sql = new StringBuilder("SELECT * FROM Venta WHERE ");
                for (int i = 0; i < params.size(); i++) {
                    if(params.get(i).equals("codigoVenta")){
                        sql.append(params.get(i)).append(" LIKE ?");
                    }
                    else{
                        sql.append(params.get(i)).append(" = ?");
                    }
                    if (i < params.size() - 1) {
                        sql.append(" AND ");
                    }
                }

                String query = sql.toString();

                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(query);
                
                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "codigoVenta":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.codigoVenta));
                            break;
                        case "fecha":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fecha));
                            break;
                        case "estado":
                            stmt.setString(index++, obj.estado);
                            break;    
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }
                
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    obj.ventaId = rs.getInt("ventaId");
                    obj.vendedorId = rs.getInt("vendedorId");
                    obj.medioPagoId = rs.getInt("medioPagoId");
                    obj.clienteId = rs.getInt("clienteId");
                    obj.codigoDescuentoRecargo = rs.getString("codigoDescuentoRecargo");

                    obj.codigoVenta = rs.getString("codigoVenta");
                    obj.fecha = CommonUtils.stringToDate(rs.getString("fecha"));
                    obj.estado = rs.getString("estado");
                    obj.montoPagado = rs.getFloat("montoPagado");

                    obj.vendedor = new EmpleadoDao().buscar(obj.vendedorId);
                    obj.medioPago = new MedioPagoDao().buscar(obj.medioPagoId);
                    obj.cliente = new ClienteDao().buscar(obj.clienteId);
                    
                    List<DescuentoRecargoDto> listado = new DescuentoRecargoDao().listarPorMedioPago(obj.medioPagoId);
                    obj.descuentoRecargo = listado.stream()
                                                  .filter(
                                                    dr -> dr.codigoDescuentoRecargo.equals(obj.codigoDescuentoRecargo)
                                                   )
                                                  .findFirst().get();

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

    @Override
    public VentaDto actualizar(VentaDto obj, List<String> campos) {
        try {
            Connection conn = ConexionSQLite.getInstance().getConnection();
            ClienteDto cliente = new ClienteDao().buscar(obj.cliente, Arrays.asList("nroCliente")).getFirst();
            EmpleadoDto vendedor = new EmpleadoDao().buscar(obj.vendedor, Arrays.asList("legajo")).getFirst();
            MedioPagoDto medioPago = new MedioPagoDao().buscar(obj.medioPago,Arrays.asList("codigoMedioPago")).getFirst();
            if (campos == null || campos.isEmpty()) {
                // Insertar
                String sql = "INSERT INTO Venta (vendedorId, codigoVenta, fecha, estado, montoPagado, medioPagoId, codigoDescuentoRecargo, clienteId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, vendedor.personaId);
                    stmt.setString(2, obj.codigoVenta);
                    stmt.setString(3, CommonUtils.dateToString(obj.fecha));
                    stmt.setString(4, obj.estado);
                    stmt.setFloat(5, obj.montoPagado);
                    stmt.setInt(6, medioPago.medioPagoId);
                    stmt.setString(7, obj.codigoDescuentoRecargo);
                    stmt.setInt(8, cliente.personaId);
                    
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
                    detalle = new DetalleVentaDao().actualizar(detalle, null);
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
                sql.append(" WHERE codigoVenta = ?");

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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }

        return obj;
    }
}
