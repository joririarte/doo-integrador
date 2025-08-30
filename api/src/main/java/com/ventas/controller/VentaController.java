package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ventas.model.Venta;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @GetMapping
    public List<Venta> listar() {
        return new Venta().listar();
    }

    @GetMapping("/{id}")
    public Venta buscarPorId(@PathVariable String id) {
        return new Venta().buscar(List.of(id)).getFirst();
    }

    @PostMapping
    public Venta crear(@RequestBody Venta venta) {
        return venta.registrar();
    }

    @PutMapping("/{id}")
    public Venta actualizar(@PathVariable String id, @RequestBody Venta venta) {
        return venta.actualizar(List.of(id));
    }

    @DeleteMapping("/{id}")
    public Venta eliminar(@PathVariable String id) {
        return new Venta().eliminar();
    }
}