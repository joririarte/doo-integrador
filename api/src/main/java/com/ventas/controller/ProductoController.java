package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.ventas.Utils.ApiResponse;
import com.ventas.model.Producto;
import com.ventas.model.Producto.ProductoBuilder;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public List<Producto> listar() {
        return new Producto().listar();
    }

    @GetMapping("/{id}")
    public ApiResponse<Producto> buscar(@PathVariable("id") String id) {
        ApiResponse<Producto> response = new ApiResponse<Producto>();

        Producto p = ProductoBuilder.getBuilder()
                                    .conCodigoBarras(id)
                                    .build();
        List<Producto> productos = p.buscar(List.of("codigoBarras"));
        if(productos != null && !productos.isEmpty()){
            response.setSuccessResponse(productos.getFirst());
            return response;
        }
        return response;
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return producto.registrar();
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable String id, @RequestBody Producto producto) {
        return producto.actualizar(List.of(id));
    }

    @DeleteMapping("/{id}")
    public Producto eliminar(@PathVariable String id) {
        return new Producto().eliminar();
    }
}