package com.tarea_property_based_testing.spring_boot;

import com.tarea_property_based_testing.spring_boot.usuario.entity.Usuario;
import com.tarea_property_based_testing.spring_boot.usuario.service.UsuarioService;
import net.jqwik.api.*;
import org.assertj.core.api.Assertions;

public class UsuarioServicePropertyTest {
    @Provide
    Arbitrary<String> nombresValidos() {
        return Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> emailsValidos() {
        return Arbitraries.strings().alpha().ofMinLength(5).map(s -> s + "@test.com");
    }

    @Provide
    Arbitrary<String> contraseñasValidas() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .withCharRange('A', 'Z')
                .withCharRange('0', '9')
                .ofMinLength(8)
                .ofMaxLength(30);
    }
    // crea el usuario
    @Provide
    Arbitrary<Usuario> usuariosNuevos() {
        return Combinators.combine(nombresValidos(), emailsValidos(), contraseñasValidas())
                .as((nombre, email, contraseña) -> new Usuario(null, nombre, email, contraseña));
    }





}