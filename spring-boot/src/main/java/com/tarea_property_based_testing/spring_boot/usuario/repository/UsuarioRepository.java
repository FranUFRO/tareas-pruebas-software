package com.tarea_property_based_testing.spring_boot.usuario.repository;

import com.tarea_property_based_testing.spring_boot.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long>  {

}