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
        String sql = "SELECT * FROM DescuentoRecargo";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DescuentoRecargoDto dto = new DescuentoRecargoDto();
                dto.id = rs.getInt("id");
                dto.nombre = rs.getString("nombre");
                dto.tipo = rs.getString("tipo");
                dto.monto = rs.getFloat("monto");
                dto.fechaInicio = CommonUtils.stringToDate(rs.getString("fechaInicio"));
                dto.fechaFin = CommonUtils.stringToDate(rs.getString("fechaFin"));
                dto.habilitado = rs.getBoolean("habilitado");
                dto.medioPagoId = rs.getInt("medio_pago_id");

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public DescuentoRecargoDto buscar(DescuentoRecargoDto obj) {
        String sql = "SELECT * FROM DescuentoRecargo WHERE id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.nombre = rs.getString("nombre");
                obj.tipo = rs.getString("tipo");
                obj.monto = rs.getFloat("monto");
                obj.fechaInicio = CommonUtils.stringToDate(rs.getString("fechaInicio"));
                obj.fechaFin = CommonUtils.stringToDate(rs.getString("fechaFin"));
                obj.habilitado = rs.getBoolean("habilitado");
                obj.medioPagoId = rs.getInt("medio_pago_id");
            } else {
                obj = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public List<DescuentoRecargoDto> obtenerPorMedioPago(int medioPagoId) {
        List<DescuentoRecargoDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM DescuentoRecargo WHERE medio_pago_id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, medioPagoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DescuentoRecargoDto dto = new DescuentoRecargoDto();
                dto.id = rs.getInt("id");
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
        }

        return lista;
    }


    @Override
    public DescuentoRecargoDto actualizar(DescuentoRecargoDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.id > 0) {
                StringBuilder sql = new StringBuilder("UPDATE DescuentoRecargo SET ");
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

                stmt.setInt(index, obj.id);
                stmt.executeUpdate();

            } else {
                String sqlInsert = "INSERT INTO DescuentoRecargo (nombre, tipo, monto, fechaInicio, fechaFin, habilitado, medio_pago_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, obj.nombre);
                stmt.setString(2, obj.tipo);
                stmt.setFloat(3, obj.monto);
                stmt.setString(4, CommonUtils.dateToString(obj.fechaInicio));
                stmt.setString(5, CommonUtils.dateToString(obj.fechaFin));
                stmt.setBoolean(6, obj.habilitado);
                stmt.setInt(7, obj.medioPagoId);

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
    public DescuentoRecargoDto borrar(DescuentoRecargoDto obj) {
        String sql = "DELETE FROM DescuentoRecargo WHERE id = ?";

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
