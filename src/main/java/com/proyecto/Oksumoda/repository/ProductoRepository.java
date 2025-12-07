package com.proyecto.Oksumoda.repository;

import com.proyecto.Oksumoda.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar productos por categoría
    List<Producto> findByCategoria(String categoria);
    
    // Buscar productos activos
    List<Producto> findByEstado(String estado);
    
    // Buscar productos por categoría y estado
    List<Producto> findByCategoriaAndEstado(String categoria, String estado);
}