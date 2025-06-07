package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.MedioPagoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

public class MedioPagoDao implements Dao<MedioPagoDto> {

    @Override
    public List<MedioPagoDto> listarTodos() {
        List<MedioPagoDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM MedioPago";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MedioPagoDto dto = new MedioPagoDto();
                dto.id = rs.getInt("id");
                dto.nombre = rs.getString("nombre");
                dto.habilitado = rs.getBoolean("habilitado");
                dto.fechaHabilitadoDesde = CommonUtils.stringToDate(rs.getString("fechaHabilitadoDesde"));
                dto.fechaHabilitadoHasta = CommonUtils.stringToDate(rs.getString("fechaHabilitadoHasta"));

                // Relación: obtener lista de descuentos/recargos asociados
                dto.descuentoRecargo = new DescuentoRecargoDao().obtenerPorMedioPago(dto.id);

                lista.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public MedioPagoDto buscar(MedioPagoDto obj) {
        String sql = "SELECT * FROM MedioPago WHERE id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.nombre = rs.getString("nombre");
                obj.habilitado = rs.getBoolean("habilitado");
                obj.fechaHabilitadoDesde = CommonUtils.stringToDate(rs.getString("fechaHabilitadoDesde"));
                obj.fechaHabilitadoHasta = CommonUtils.stringToDate(rs.getString("fechaHabilitadoHasta"));
                obj.descuentoRecargo = new DescuentoRecargoDao().obtenerPorMedioPago(obj.id);

            } else {
                obj = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public MedioPagoDto actualizar(MedioPagoDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.id > 0) {
                StringBuilder sql = new StringBuilder("UPDATE MedioPago SET ");
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
                        case "habilitado":
                            stmt.setBoolean(index++, obj.habilitado);
                            break;
                        case "fechaHabilitadoDesde":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaHabilitadoDesde));
                            break;
                        case "fechaHabilitadoHasta":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaHabilitadoHasta));
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.id);
                stmt.executeUpdate();

            } else {
                String sqlInsert = "INSERT INTO MedioPago (nombre, habilitado, fechaHabilitadoDesde, fechaHabilitadoHasta) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, obj.nombre);
                stmt.setBoolean(2, obj.habilitado);
                stmt.setString(3, CommonUtils.dateToString(obj.fechaHabilitadoDesde));
                stmt.setString(4, CommonUtils.dateToString(obj.fechaHabilitadoHasta));

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
    public MedioPagoDto borrar(MedioPagoDto obj) {
        String sql = "DELETE FROM MedioPago WHERE id = ?";

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
