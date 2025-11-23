package com.proyecto.Oksumoda.repository;

import com.proyecto.Oksumoda.entity.Rol; // O .model, según dónde lo tengas
import org.springframework.data.jpa.repository.JpaRepository;

// Rol tiene un ID de tipo Integer según tu definición SQL
public interface RolRepository extends JpaRepository<Rol, Integer> { 
    // JpaRepository ya provee findById(Integer id) y findAll()
}