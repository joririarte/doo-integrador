package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ventas.model.Producto;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public List<Producto> listar() {
        return new Producto().listar();
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable String id) {
        return new Producto().buscar(List.of(id)).getFirst();
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