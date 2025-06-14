package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.DescuentoRecargoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class DescuentoRecargoDao implements Dao<DescuentoRecargoDto> {

    @Override
    public List<DescuentoRecargoDto> listarTodos() {
        List<DescuentoRecargoDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM DescuentoRecargo ORDER BY habilitado DESC";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DescuentoRecargoDto dto = new DescuentoRecargoDto();
                dto.medioPagoId = rs.getInt("medioPagoId");
                dto.codigoDescuentoRecargo = rs.getString("codigoDescuentoRecargo");
                dto.nombre = rs.getString("nombre");
                dto.tipo = rs.getString("tipo");
                dto.monto = rs.getFloat("monto");
                dto.fechaInicio = CommonUtils.stringToDate(rs.getString("fechaInicio"));
                dto.fechaFin = CommonUtils.stringToDate(rs.getString("fechaFin"));
                dto.habilitado = rs.getBoolean("habilitado");
                lista.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public DescuentoRecargoDto buscar(int id) {
        return null;
    }

    public List<DescuentoRecargoDto> obtenerPorMedioPago(int medioPagoId) {
        List<DescuentoRecargoDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM DescuentoRecargo WHERE medioPagoId = ? ORDER BY habilitado DESC";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, medioPagoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DescuentoRecargoDto dto = new DescuentoRecargoDto();
                dto.medioPagoId = rs.getInt("medioPagoId");
                dto.codigoDescuentoRecargo = rs.getString("codigoDescuentoRecargo");
                dto.nombre = rs.getString("nombre");
                dto.tipo = rs.getString("tipo");
                dto.monto = rs.getFloat("monto");
                dto.fechaInicio = CommonUtils.stringToDate(rs.getString("fechaInicio"));
                dto.fechaFin = CommonUtils.stringToDate(rs.getString("fechaFin"));
                dto.habilitado = rs.getBoolean("habilitado");
                dto.medioPagoId = medioPagoId;

                lista.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }


    @Override
    public DescuentoRecargoDto actualizar(DescuentoRecargoDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.medioPagoId > 0 && !obj.codigoDescuentoRecargo.isEmpty()) {
                StringBuilder sql = new StringBuilder("UPDATE DescuentoRecargo SET ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE medioPagoId = ? AND codigoDescuentoRecargo = ?");

                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql.toString());

                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "nombre":
                            stmt.setString(index++, obj.nombre);
                            break;
                        case "monto":
                            stmt.setFloat(index++, obj.monto);
                            break;
                        case "fechaInicio":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaInicio));
                            break;
                        case "fechaFin":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaFin));
                            break;
                        case "habilitado":
                            stmt.setBoolean(index++, obj.habilitado);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.medioPagoId);
                stmt.setString(index++, obj.codigoDescuentoRecargo);
                stmt.executeUpdate();

            } else {
                int nextDescuentoRecargo = obtenerPorMedioPago(obj.medioPagoId).size() + 1;
                String sqlInsert = "INSERT INTO DescuentoRecargo (medioPagoId, codigoDescuentoRecargo, nombre, tipo, monto, fechaInicio, fechaFin, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, obj.medioPagoId);
                stmt.setInt(2, nextDescuentoRecargo);
                stmt.setString(3, obj.nombre);
                stmt.setString(4, obj.tipo);
                stmt.setFloat(5, obj.monto);
                stmt.setString(6, CommonUtils.dateToString(obj.fechaInicio));
                stmt.setString(7, CommonUtils.dateToString(obj.fechaFin));
                stmt.setBoolean(8, obj.habilitado);

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public DescuentoRecargoDto borrar(DescuentoRecargoDto obj) {
        String sql = "DELETE FROM DescuentoRecargo WHERE medioPagoId = ? AND codigoDescuentoRecargo = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.medioPagoId);
            stmt.setString(2, obj.codigoDescuentoRecargo);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public List<DescuentoRecargoDto> buscar(DescuentoRecargoDto obj, List<String> params) {
        return null;
    }
}
