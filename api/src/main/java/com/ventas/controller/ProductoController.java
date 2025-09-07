package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public Producto buscarPorId(@PathVariable("id") String id) {
        Producto p = ProductoBuilder.getBuilder()
                                    .conCodigoBarras(id)
                                    .build();
        return p.buscar(List.of("codigoBarras")).getFirst();
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