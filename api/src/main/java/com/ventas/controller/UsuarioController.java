package com.ventas.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.ventas.Utils.ApiResponse;
import com.ventas.model.Usuario;
import com.ventas.model.Usuario.UsuarioBuilder;
import com.ventas.model.Usuario;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @GetMapping
    public List<Usuario> listar() {
        return new Usuario().listar();
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable("id") String id) {
        return new Usuario().buscar(List.of(id)).getFirst();
    }

    @PostMapping("/login")
    public ApiResponse<Usuario> iniciarSesion(@RequestBody Usuario user){
        ApiResponse<Usuario> response = new ApiResponse<Usuario>();

        user = user.iniciarSesion();
        
        if (user != null) {
            response.setSuccessResponse(user);
            
            return response;
        }

        return response;
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario empleado) {
        return empleado.registrar();
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable String id, @RequestBody Usuario empleado) {
        return empleado.actualizar(List.of(id));
    }

    @DeleteMapping("/{id}")
    public Usuario eliminar(@PathVariable String id) {
        return new Usuario().eliminar();
    }
}