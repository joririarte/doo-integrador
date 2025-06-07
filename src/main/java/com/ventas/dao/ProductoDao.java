package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.ProductoDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;

public class ProductoDao implements Dao<ProductoDto> {

    @Override
    public List<ProductoDto> listarTodos() {
        List<ProductoDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductoDto dto = new ProductoDto();
                dto.id = rs.getInt("id");
                dto.nombre = rs.getString("nombre");
                dto.marca = rs.getString("Marca");
                dto.codigoBarras = rs.getString("codigoBarras");

                dto.precio = new PrecioDao().listarPorProducto(dto.id);
                dto.stock = new StockDao().listarPorProducto(dto.id);

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public ProductoDto buscar(ProductoDto obj) {
        String sql = "SELECT * FROM Producto WHERE id = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                obj.nombre = rs.getString("nombre");
                obj.marca = rs.getString("Marca");
                obj.codigoBarras = rs.getString("codigoBarras");

                obj.precio = new PrecioDao().listarPorProducto(obj.id);
                obj.stock = new StockDao().listarPorProducto(obj.id);
            } else {
                obj = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public ProductoDto actualizar(ProductoDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.id > 0) {
                StringBuilder sql = new StringBuilder("UPDATE Producto SET ");
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
                        case "Marca":
                            stmt.setString(index++, obj.marca);
                            break;
                        case "codigoBarras":
                            stmt.setString(index++, obj.codigoBarras);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.id);
                stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Producto (nombre, Marca, codigoBarras) VALUES (?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, obj.nombre);
                stmt.setString(2, obj.marca);
                stmt.setString(3, obj.codigoBarras);
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
    public ProductoDto borrar(ProductoDto obj) {
        String sql = "DELETE FROM Producto WHERE id = ?";

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

