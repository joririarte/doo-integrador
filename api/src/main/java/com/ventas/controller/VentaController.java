package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.ventas.Utils.ApiResponse;
import com.ventas.model.Venta;
import com.ventas.model.Venta.VentaBuilder;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @GetMapping
    public List<Venta> listar() {
        return new Venta().listar();
    }

    @GetMapping("/{id}")
    public ApiResponse<Venta> buscar(@PathVariable("id") String id) {
        ApiResponse<Venta> response = new ApiResponse<Venta>();

        Venta c = VentaBuilder.getBuilder()
                                  .conCodigoVenta(id)
                                  .build();
        
        List<Venta> clientes = c.buscar(List.of("codigoVenta"));
        
        if (clientes != null && !clientes.isEmpty()) {
            response.setSuccessResponse(clientes.get(0));
            
            return response;
        }

        return response;
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