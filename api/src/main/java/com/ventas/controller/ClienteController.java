package com.ventas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import com.ventas.model.Cliente;
import com.ventas.model.Cliente.ClienteBuilder;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @GetMapping
    public List<Cliente> listar() {
        return new Cliente().listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable("id") String id) {
        Cliente c = ClienteBuilder.getBuilder()
                                  .conNroCliente(id)
                                  .build();
        List<Cliente> clientes = c.buscar(List.of("nroCliente"));
        if (clientes != null && !clientes.isEmpty()) {
            return ResponseEntity.ok(clientes.getFirst()); // 200 + Cliente en JSON
        } else {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
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