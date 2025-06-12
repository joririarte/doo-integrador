package com.ventas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ventas.dto.PrecioDto;
import com.ventas.dto.ProductoDto;
import com.ventas.dto.StockDto;
import com.ventas.singletonSqlConnection.ConexionSQLite;
import com.ventas.util.CommonUtils;

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
                dto.productoId = rs.getInt("productoId");
                dto.nombre = rs.getString("nombre");
                dto.marca = rs.getString("marca");
                dto.codigoBarras = rs.getString("codigoBarras");

                dto.precio = new PrecioDao().listarPorProducto(dto.productoId);
                dto.stock = new StockDao().listarPorProducto(dto.productoId);

                lista.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public ProductoDto buscar(int id) {
        String sql = "SELECT * FROM Producto WHERE productoId = ?";

        try {
            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ProductoDto obj = new ProductoDto();
                obj.nombre = rs.getString("nombre");
                obj.marca = rs.getString("marca");
                obj.codigoBarras = rs.getString("codigoBarras");

                obj.precio = new PrecioDao().listarPorProducto(obj.productoId);
                obj.stock = new StockDao().listarPorProducto(obj.productoId);
                return obj;
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    @Override
    public List<ProductoDto> buscar(ProductoDto obj, List<String> params) {
        List<ProductoDto> lista = new ArrayList<>();
        try{
            if (params != null && !params.isEmpty()) {
                StringBuilder sql = new StringBuilder("SELECT * FROM Producto WHERE ");
                for (int i = 0; i < params.size(); i++) {
                    if(params.get(i).equals("codigoBarras")){
                        sql.append(params.get(i)).append(" = ?");
                    }
                    else{
                        sql.append(params.get(i)).append(" LIKE ?");
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
                        case "nombre":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.nombre));
                            break;
                        case "marca":
                            stmt.setString(index++, CommonUtils.setWildcard(obj.marca));
                            break;
                        case "codigoBarras":
                            stmt.setString(index++, obj.codigoBarras);
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }
                
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    obj.productoId = rs.getInt("productoId");
                    obj.nombre = rs.getString("nombre");
                    obj.marca = rs.getString("marca");
                    obj.codigoBarras = rs.getString("codigoBarras");

                    obj.precio = new PrecioDao().listarPorProducto(obj.productoId);
                    obj.stock = new StockDao().listarPorProducto(obj.productoId);

                    lista.add(obj);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return lista;
    }

    @Override
    public ProductoDto actualizar(ProductoDto obj, List<String> params) {
        try {
            if (params != null && !params.isEmpty() && obj.productoId > 0) {
                StringBuilder sql = new StringBuilder("UPDATE Producto SET ");
                for (int i = 0; i < params.size(); i++) {
                    sql.append(params.get(i)).append(" = ?");
                    if (i < params.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(" WHERE productoId = ?");
                String query = sql.toString();
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(query);

                int index = 1;
                for (String param : params) {
                    switch (param) {
                        case "nombre":
                            stmt.setString(index++, obj.nombre);
                            break;
                        case "monto":
                            {
                                PrecioDao precioDao = new PrecioDao();
                                PrecioDto precio = obj.precio.getFirst();
                                precio.productoId = obj.productoId;
                                precio.precioId = obj.precio.size() + 1;
                                precioDao.actualizar(precio, null);
                            }
                            break;
                        case "cantidad":
                            {
                                StockDao stockDao = new StockDao();
                                StockDto stock = obj.stock.getFirst();
                                stock.productoId = obj.productoId;
                                stock.stockId = obj.stock.size() + 1;
                                stockDao.actualizar(stock, null);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Campo no soportado: " + param);
                    }
                }

                stmt.setInt(index, obj.productoId);
                stmt.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Producto (nombre, marca, codigoBarras) VALUES (?, ?, ?)";
                PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, obj.nombre);
                stmt.setString(2, obj.marca);
                stmt.setString(3, obj.codigoBarras);
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.productoId = rs.getInt(1);
                    
                    StockDao stockDao = new StockDao();
                    int index = 1;
                    for (StockDto s : obj.stock){
                        s.productoId = obj.productoId;
                        s.stockId = index++;
                        stockDao.actualizar(s, null);
                    }

                    PrecioDao precioDao = new PrecioDao();
                    index = 1;
                    for(PrecioDto p : obj.precio){
                        p.productoId = obj.productoId;
                        p.precioId = index++;
                        precioDao.actualizar(p, null);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public ProductoDto borrar(ProductoDto obj) {
        String sql = "DELETE FROM Producto WHERE productoId = ?";

        try {
            StockDao stockDao = new StockDao();
            for(StockDto s : obj.stock){
                stockDao.borrar(s);
            }

            PrecioDao precioDao = new PrecioDao();
            for(PrecioDto p : obj.precio){
                precioDao.borrar(p);
            }

            PreparedStatement stmt = ConexionSQLite.getInstance().getConnection().prepareStatement(sql);
            stmt.setInt(1, obj.productoId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}

