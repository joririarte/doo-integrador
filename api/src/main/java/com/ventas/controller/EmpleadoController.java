package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ventas.model.Empleado;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @GetMapping
    public List<Empleado> listar() {
        return new Empleado().listar();
    }

    @GetMapping("/{id}")
    public Empleado buscarPorId(@PathVariable String id) {
        return new Empleado().buscar(List.of(id)).getFirst();
    }

    @PostMapping
    public Empleado crear(@RequestBody Empleado empleado) {
        return empleado.registrar();
    }

    @PutMapping("/{id}")
    public Empleado actualizar(@PathVariable String id, @RequestBody Empleado empleado) {
        return empleado.actualizar(List.of(id));
    }

    @DeleteMapping("/{id}")
    public Empleado eliminar(@PathVariable String id) {
        return new Empleado().eliminar();
    }
}