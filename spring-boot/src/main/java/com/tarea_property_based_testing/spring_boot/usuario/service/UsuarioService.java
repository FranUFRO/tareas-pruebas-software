package com.tarea_property_based_testing.spring_boot.usuario.service;

import com.tarea_property_based_testing.spring_boot.usuario.entity.Usuario;
import com.tarea_property_based_testing.spring_boot.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario actualizar(Long id, String nuevoNombre) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(nuevoNombre);
            return usuarioRepository.save(usuarioExistente);
        }).orElseThrow(() -> new IllegalArgumentException("No se puede actualizar: Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No se puede eliminar: Usuario no encontrado");
        }
    }


}