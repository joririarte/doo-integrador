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
                dto.medioPagoId = rs.getInt("medioPagoId");
                dto.nombre = rs.getString("nombre");
                dto.habilitado = rs.getBoolean("habilitado");
                dto.fechaHabilitadoDesde = CommonUtils.stringToDate(rs.getString("fechaHabilitadoDesde"));
                dto.fechaHabilitadoHasta = CommonUtils.stringToDate(rs.getString("fechaHabilitadoHasta"));
                dto.codigoMedioPago = rs.getString("codigoMedioPago");

                // Relación: obtener lista de descuentos/recargos asociados
                dto.descuentoRecargo = new DescuentoRecargoDao().listarPorMedioPago(dto.medioPagoId);

                lista.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public MedioPagoDto buscar(int id) {
        String sql = "SELECT * FROM MedioPago WHERE medioPagoId = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MedioPagoDto obj = new MedioPagoDto();
                obj.nombre = rs.getString("nombre");
                obj.habilitado = rs.getBoolean("habilitado");
                obj.fechaHabilitadoDesde = CommonUtils.stringToDate(rs.getString("fechaHabilitadoDesde"));
                obj.fechaHabilitadoHasta = CommonUtils.stringToDate(rs.getString("fechaHabilitadoHasta"));
                obj.descuentoRecargo = new DescuentoRecargoDao().listarPorMedioPago(obj.medioPagoId);
                obj.codigoMedioPago = rs.getString("codigoMedioPago");
                return obj;

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public MedioPagoDto actualizar(MedioPagoDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.medioPagoId > 0) {
                StringBuilder sql = new StringBuilder("UPDATE MedioPago SET ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE medioPagoId = ?");

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

                stmt.setInt(index, obj.medioPagoId);
                stmt.executeUpdate();

            } else {
                String sqlInsert = "INSERT INTO MedioPago (nombre, habilitado, fechaHabilitadoDesde, fechaHabilitadoHasta, codigoMedioPago) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, obj.nombre);
                stmt.setBoolean(2, obj.habilitado);
                stmt.setString(3, CommonUtils.dateToString(obj.fechaHabilitadoDesde));
                stmt.setString(4, CommonUtils.dateToString(obj.fechaHabilitadoHasta));
                stmt.setString(5, obj.codigoMedioPago);

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.medioPagoId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public MedioPagoDto borrar(MedioPagoDto obj) {
        String sql = "DELETE FROM MedioPago WHERE codigoMedioPago = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setString(1, obj.codigoMedioPago);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public List<MedioPagoDto> buscar(MedioPagoDto obj, List<String> params) {
        List<MedioPagoDto> lista = new ArrayList<>();
        try{
            if (params != null && !params.isEmpty()) {
                StringBuilder sql = new StringBuilder("SELECT * FROM MedioPago WHERE ");
                for (int i = 0; i < params.size(); i++) {
                    switch (params.get(i)) {
                        case "fechaHabilitadoDesde":
                            sql.append("fechaHabilitadoDesde >= ?");
                            break;
                        case "fechaHabilitadoHasta":
                            sql.append("fechaHabilitadoHasta <= ?");
                            break;
                        case "nombre":
                            sql.append("nombre LIKE ?");
                            break;
                        case "habilitado":
                            sql.append("habilitado = ?");
                            break;
                        case "codigoMedioPago":
                            sql.append("codigoMedioPago = ?");
                            break;
                        default:
                            sql.append(params.get(i)).append(" = ?");
                            break;
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
                        case "habilitado":
                            stmt.setBoolean(index++, obj.habilitado);
                            break;
                        case "nombre":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.nombre));
                            break;
                        case "fechaHabilitadoDesde":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaHabilitadoDesde));
                            break;
                        case "fechaHabilitadoHasta":
                            stmt.setString(index++, CommonUtils.dateToString(obj.fechaHabilitadoHasta));
                            break;
                        case "codigoMedioPago":
                            stmt.setString(index++, obj.codigoMedioPago);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }
                
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    obj.medioPagoId = rs.getInt("medioPagoId");
                    obj.nombre = rs.getString("nombre");
                    obj.codigoMedioPago = rs.getString("codigoMedioPago");
                    obj.habilitado = rs.getBoolean("habilitado");
                    obj.fechaHabilitadoDesde = CommonUtils.stringToDate(rs.getString("fechaHabilitadoDesde"));
                    obj.fechaHabilitadoHasta = CommonUtils.stringToDate(rs.getString("fechaHabilitadoHasta"));
                    obj.descuentoRecargo = new DescuentoRecargoDao().listarPorMedioPago(obj.medioPagoId);

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
}
