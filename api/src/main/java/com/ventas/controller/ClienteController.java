package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ventas.model.Cliente;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @GetMapping
    public List<Cliente> listar() {
        return new Cliente().listar();
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable String id) {
        return new Cliente().buscar(List.of(id)).getFirst();
    }

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return cliente.registrar();
    }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable String id, @RequestBody Cliente cliente) {
        return cliente.actualizar(List.of(id));
    }

    @DeleteMapping("/{id}")
    public Cliente eliminar(@PathVariable String id) {
        return new Cliente().eliminar();
    }
}